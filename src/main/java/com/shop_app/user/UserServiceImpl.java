package com.shop_app.user;

import com.shop_app.user.enums.UserStatus;
import com.shop_app.user.mapper.UserMapper;
import com.shop_app.user.request.UserPatchRequest;
import com.shop_app.user.entity.User;
import com.shop_app.user.response.UserResponse;
import com.shop_app.user.validator.UserValidator;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements IUserService {
    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final UserValidator userValidator;

    @Override
    @Transactional
    public void partialUpdate(long id, UserPatchRequest req) {
        User user = userValidator.validateAndGet(id);
        String phone = req.getPhoneNumber();

        // update phone if it different db
        if (phone != null && !user.getPhoneNumber().equals(phone)) {
            userValidator.validateDuplicate(phone);
            user.setPhoneNumber(phone);
        }

        // update role if it different db
//        if (user.getRole() != null
//                && !user.getRole().getId().equals(req.getRoleId())) {
//            user.setRole(roleValidator.validateAndGet(req.getRoleId()));
//        }

        // update if not null
        userMapper.updatePartialUser(req, user);

        log.info("[USER][UPDATE] phoneNumber= {}", req.getPhoneNumber());
    }

    /**
     * Soft delete
     */
    @Override
    @Transactional
    public void delete(long id) {
        User user = userValidator.validateAndGet(id);
        user.setStatus(UserStatus.INACTIVE);

        log.info("[USER][SOFT_DELETE] id= {}", id);
    }

    @Override
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(userMapper::toResponse)
                .toList();
    }
}


