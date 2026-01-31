package com.shop_app.auth;

import com.shop_app.auth.refresh_token.RefreshTokenService;
import com.shop_app.auth.requests.UserLoginRequest;
import com.shop_app.auth.response.TokenPair;
import com.shop_app.configs.security.exceptions.InvalidTokenException;
import com.shop_app.configs.security.jwt.JWTService;
import com.shop_app.configs.security.UserPrincipal;
import com.shop_app.configs.security.UserPrincipleDetailService;
import com.shop_app.role.RoleRepository;
import com.shop_app.role.entity.Role;
import com.shop_app.role.enums.UserRole;
import com.shop_app.shared.exceptions.DuplicateException;
import com.shop_app.shared.exceptions.SystemException;
import com.shop_app.shared.exceptions.enums.ErrorCode;
import com.shop_app.shared.validate.Validate;
import com.shop_app.user.UserRepository;
import com.shop_app.user.entity.User;
import com.shop_app.user.enums.UserStatus;
import com.shop_app.user.request.UserRegisterRequest;
import com.shop_app.user.validator.UserValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class AuthServiceImp implements AuthService {
    private final AuthenticationManager authManager;
    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserValidator userValidator;
    private final JWTService jwtService;
    private final UserPrincipleDetailService customUserDetailService;
    private final RefreshTokenService refreshTokenService;

    @Override
    @Transactional
    public void register(UserRegisterRequest req) {
        Validate.requiredNonNull(req, "User register request must be not null");

        // 1.  Check if phone number already exist
        if (userValidator.existsByPhone(req.phoneNumber())) {
            throw new DuplicateException("Phone Number has already been registered");
        }

        // 2. The role default is "USER"
        Role defaultRole = roleRepository
                .findByName(UserRole.USER)
                .orElseThrow(() ->
                        new SystemException(ErrorCode.INTERNAL_ERROR.getMessage()));

        // 3. Map DTO to Entity
        User newUser = User.builder()
                .phoneNumber(req.phoneNumber())
                .fullName(req.fullName())
                .password(passwordEncoder.encode(req.password())) // encode pwd
                .status(UserStatus.ACTIVE) // The register status must be "ACTIVE"
                .build();

        newUser.addRole(defaultRole);

        // 4. Flush to db
        userRepository.save(newUser);

        // 5. Log
        log.info("[AUTH][REGISTER] User register successful with ID: {}", newUser.getId());
    }

    @Override
    public TokenPair login(UserLoginRequest req) {
        Validate.requiredNonNull(req, "User login request must be not null");

        // 1. Initialize the authentication object (Token)
        var token = new UsernamePasswordAuthenticationToken(
                req.phoneNumber(),
                req.password()
        );

        // 2. Perform login authentication via AuthenticationManager
        Authentication authentication = authManager.authenticate(token);

        // 3. Get user details (UserDetails) after successful authentication
        UserPrincipal user = (UserPrincipal) authentication.getPrincipal();

        // 4. Create the Access Token and Refresh Token pair
        String accessToken = jwtService.generateAccessToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        // 5. Return an object containing the token pair to the user
        return new TokenPair(accessToken, refreshToken);
    }

    @Override
    @Transactional
    public TokenPair refreshToken(String refreshToken) {

        // 1. Validate token (expired, format)
        if (refreshToken == null || !jwtService.isRefreshTokenValid(refreshToken)) {
            throw new InvalidTokenException("Invalid or expired refresh token");
        }

        String username = jwtService.extractUsername(refreshToken);
        UUID jti = jwtService.extractJtiToken(refreshToken);

        //2. Revoke old refresh token
        refreshTokenService.revoke(jti);

        // 3. Load User
        UserPrincipal user =
                (UserPrincipal) customUserDetailService.loadUserByUsername(username);

        // 4. Generate new token pair
        String newAccessToken = jwtService.generateAccessToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);

        return new TokenPair(newAccessToken, newRefreshToken);
    }

    @Override
    @Transactional
    public void logout(String refreshToken) {
        if (refreshToken == null || refreshToken.isBlank()
                || !jwtService.isTokenWellFormed(refreshToken)) {
            return;
        }

        // Extract JTI and revoke
        UUID jti = jwtService.extractJtiToken(refreshToken);
        refreshTokenService.revoke(jti);

        log.info("Logout successful, token revoked: {}", jti);
    }
}
