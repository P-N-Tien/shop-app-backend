package com.shop_app.role;

import com.shop_app.role.request.RoleRequest;

import java.util.List;

public interface IRoleService {
    void create(RoleRequest role);

    List<RoleRequest> findAll();
}
