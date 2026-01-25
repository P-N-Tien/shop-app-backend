package com.shop_app.auth;

import com.shop_app.auth.requests.UserLoginRequest;
import com.shop_app.auth.response.TokenPair;
import com.shop_app.user.request.UserRegisterRequest;

public interface AuthService {
    TokenPair login(UserLoginRequest req);

    void register(UserRegisterRequest req);

    TokenPair refreshToken(String refreshToken);

    void logout(String refreshToken);
}
