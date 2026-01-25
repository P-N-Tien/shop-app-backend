package com.shop_app.role.mapper;

import com.shop_app.role.request.RoleRequest;
import com.shop_app.role.entity.Role;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface RoleMapper {
    
    Role toEntity(RoleRequest dto);

    RoleRequest toResponse(Role role);
}
