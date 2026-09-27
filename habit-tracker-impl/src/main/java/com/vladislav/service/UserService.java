package com.vladislav.service;

import com.vladislav.dto.request.LoginRq;
import com.vladislav.dto.request.RegisterRq;
import com.vladislav.dto.response.LoginRs;
import com.vladislav.dto.response.RegisterRs;

public interface UserService {

    RegisterRs register(RegisterRq registerRq);

    LoginRs login(LoginRq loginRq);
}
