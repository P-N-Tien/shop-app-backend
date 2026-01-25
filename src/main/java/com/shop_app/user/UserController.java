package com.shop_app.user;

import com.shop_app.user.request.UserPatchRequest;
import com.shop_app.user.response.UserResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/users")
public class UserController {
    private final IUserService userService;

    @PatchMapping("/{id}")
    public ResponseEntity<Void> updateUser(
            @RequestBody UserPatchRequest req,
            @PathVariable long id) {
        userService.partialUpdate(id, req);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable long id) {
        userService.delete(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping
    public ResponseEntity<List<UserResponse>> getUsers() {
        return ResponseEntity.status(HttpStatus.OK).body(userService.findAll());
    }
}

