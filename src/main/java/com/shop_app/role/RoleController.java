package com.shop_app.role;

import com.shop_app.role.request.RoleRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("${api.prefix}/roles")
public class RoleController {
    private final IRoleService roleService;

    @PostMapping
    public ResponseEntity<Void> createRole(@RequestBody @Valid RoleRequest req) {
        roleService.create(req);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping
    public ResponseEntity<List<RoleRequest>> getRoles() {
        return ResponseEntity.ok(roleService.findAll());
    }

//
//    @PutMapping
//    public ResponseEntity<RoleDTO> updateCategory(@RequestBody @Valid RoleDTO roleDTO) {
//        RoleDTO savedCategory = roleService.updateCategory(roleDTO);
//        return ResponseEntity.ok(savedCategory);
//    }
//
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteCategoryById(@PathVariable Long id) {
//        String deleted = roleService.deleteCategoryById(id);
//        return ResponseEntity.ok(deleted);
//    }
}

