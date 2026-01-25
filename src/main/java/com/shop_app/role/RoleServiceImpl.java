package com.shop_app.role;

import com.shop_app.role.mapper.RoleMapper;
import com.shop_app.role.request.RoleRequest;
import com.shop_app.shared.exceptions.DuplicateException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements IRoleService {
    private final RoleRepository roleRepository;
    private final RoleMapper roleMapper;

    @Override
    @Transactional
    public void create(RoleRequest req) {
        validateDuplicateBy(req.getName());

        roleRepository.save(roleMapper.toEntity(req));
    }

    @Override
    public List<RoleRequest> findAll() {
        return roleRepository.findAll().stream()
                .map(roleMapper::toResponse)
                .toList();
    }

    // ============ HELPER METHODS ============== //

    private void validateDuplicateBy(String name) {
        if (roleRepository.existsByName(name)) {
            throw new DuplicateException("Role already exists with name: " + name);
        }
    }
}
