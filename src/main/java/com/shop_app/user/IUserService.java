package com.shop_app.user;

import com.shop_app.user.request.UserPatchRequest;
import com.shop_app.user.request.UserRegisterRequest;
import com.shop_app.user.response.UserResponse;

import java.util.List;

public interface IUserService {
    List<UserResponse> findAll();

    void partialUpdate(long id, UserPatchRequest req);

    void delete(long id);
}
