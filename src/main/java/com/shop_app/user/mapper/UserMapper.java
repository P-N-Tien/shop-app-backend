package com.shop_app.user.mapper;

import com.shop_app.user.request.UserPatchRequest;
import com.shop_app.user.entity.User;
import com.shop_app.user.response.UserResponse;
import org.mapstruct.*;

@Mapper(
        componentModel = "spring",
        nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE
)
public interface UserMapper {
    @Mapping(target = "roles", ignore = true)
    void updatePartialUser(UserPatchRequest req, @MappingTarget User entity);

    UserResponse toResponse(User entity);
}

