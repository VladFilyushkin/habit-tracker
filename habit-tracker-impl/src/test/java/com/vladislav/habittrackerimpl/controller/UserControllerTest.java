package com.vladislav.habittrackerimpl.controller;

import com.vladislav.dto.request.LoginRq;
import com.vladislav.dto.response.HabitRs;
import com.vladislav.dto.response.RegisterRs;
import com.vladislav.repository.HabitRepository;
import com.vladislav.repository.UserRepository;
import io.restassured.http.ContentType;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import static com.vladislav.habittrackerimpl.TestData.*;
import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class UserControllerTest extends AbstractIntegrationControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private HabitRepository habitRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void beforeEach() {
        habitRepository.deleteAll();
        userRepository.deleteAll();
    }

    @Test
    @SneakyThrows
    void shouldRegisterUserAndStorePasswordAsHash() {
        var request = getRegisterRqBody();

        var response = given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(201)
                .extract()
                .as(RegisterRs.class);

        assertThat(userRepository.existsByUsername(response.getUsername())).isTrue();
        assertEquals(request.getUsername(), response.getUsername());

        var savedUser = userRepository.findByUsername(response.getUsername()).orElseThrow();
        assertThat(savedUser.getPassword()).isNotEqualTo(request.getPassword());
        assertThat(passwordEncoder.matches(request.getPassword(), savedUser.getPassword())).isTrue();
    }

    @Test
    @SneakyThrows
    void shouldRejectRegistrationWithDuplicateUsername() {
        var request = getRegisterRqBody();

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(201);

        given()
                .contentType(ContentType.JSON)
                .body(request)
                .when()
                .post("/api/auth/register")
                .then()
                .statusCode(409);
    }

    @Test
    @SneakyThrows
    void shouldLoginWithCorrectPasswordAndReceiveToken() {
        var registerRq = getRegisterRqBody();
        given()
                .contentType(ContentType.JSON)
                .body(registerRq)
                .when()
                .post("/api/auth/register");

        var loginRq = getLoginRqBody();

        given()
                .contentType(ContentType.JSON)
                .body(loginRq)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .body("token", notNullValue());
    }

    @Test
    @SneakyThrows
    void shouldRejectLoginWithWrongPasswordAndReturn401() {
        var registerRq = getRegisterRqBody();
        given()
                .contentType(ContentType.JSON)
                .body(registerRq)
                .when()
                .post("/api/auth/register");

        var loginRq = getLoginRqBody();
        var wrongLoginRq = new LoginRq(loginRq.getUsername(), "totally-wrong-password");

        given()
                .contentType(ContentType.JSON)
                .body(wrongLoginRq)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    @SneakyThrows
    void shouldRejectLoginForNonExistentUser() {
        var loginRq = getLoginRqBody();

        given()
                .contentType(ContentType.JSON)
                .body(loginRq)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(401);
    }

    @Test
    @SneakyThrows
    void shouldReturn401WhenAccessingHabitsWithoutToken() {
        given()
                .when()
                .get("/api/habits")
                .then()
                .statusCode(401);
    }

    @Test
    @SneakyThrows
    void shouldReturn401ForMalformedToken() {
        given()
                .header("Authorization", "Bearer this.is.not.valid")
                .when()
                .get("/api/habits")
                .then()
                .statusCode(401);
    }

    @Test
    @SneakyThrows
    void shouldAllowAccessToProtectedEndpointWithValidToken() {
        var registerRq = getRegisterRqBody();
        given()
                .contentType(ContentType.JSON)
                .body(registerRq)
                .when()
                .post("/api/auth/register");

        var loginRq = getLoginRqBody();
        String token = given()
                .contentType(ContentType.JSON)
                .body(loginRq)
                .when()
                .post("/api/auth/login")
                .then()
                .statusCode(200)
                .extract()
                .jsonPath()
                .getString("token");

        given()
                .header("Authorization", "Bearer " + token)
                .when()
                .get("/api/habits")
                .then()
                .statusCode(200);
    }

    @Test
    @SneakyThrows
    void shouldIsolateHabitsBetweenUsers() {
        var aliceRegisterRq = getRegisterRqBody();
        given().contentType(ContentType.JSON).body(aliceRegisterRq).post("/api/auth/register");
        String aliceToken = given()
                .contentType(ContentType.JSON)
                .body(getLoginRqBody())
                .post("/api/auth/login")
                .jsonPath()
                .getString("token");

        var bobRegisterRq = getSecondRegisterRqBody();
        given().contentType(ContentType.JSON).body(bobRegisterRq).post("/api/auth/register");
        String bobToken = given()
                .contentType(ContentType.JSON)
                .body(getSecondLoginRqBody())
                .post("/api/auth/login")
                .jsonPath()
                .getString("token");

        var habitRq = getHabitRqBody();

        given()
                .header("Authorization", "Bearer " + aliceToken)
                .contentType(ContentType.JSON)
                .body(habitRq)
                .post("/api/habits");

        given()
                .header("Authorization", "Bearer " + bobToken)
                .contentType(ContentType.JSON)
                .body(habitRq)
                .post("/api/habits");

        var aliceHabits = given()
                .header("Authorization", "Bearer " + aliceToken)
                .when()
                .get("/api/habits")
                .then()
                .statusCode(200)
                .extract()
                .as(HabitRs[].class);

        assertThat(aliceHabits).hasSize(1);
        assertEquals(habitRq.getName(), aliceHabits[0].getName());

        var bobHabits = given()
                .header("Authorization", "Bearer " + bobToken)
                .when()
                .get("/api/habits")
                .then()
                .statusCode(200)
                .extract()
                .as(HabitRs[].class);

        assertThat(bobHabits).hasSize(1);
        assertEquals(habitRq.getName(), bobHabits[0].getName());
    }
}