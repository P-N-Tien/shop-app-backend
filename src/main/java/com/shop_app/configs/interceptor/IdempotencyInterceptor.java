package com.shop_app.configs.interceptor;

//import com.shop_app.idempotency.IIdempotencyService;
//import com.shop_app.idempotency.IdempotencyKey;
//import com.sun.jdi.request.DuplicateRequestException;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang3.StringUtils;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.MediaType;
//import org.springframework.stereotype.Component;
//import org.springframework.web.servlet.HandlerInterceptor;
//
//import java.io.IOException;
//import java.util.Set;
//
//@Slf4j
//@Component
//@RequiredArgsConstructor
//public class IdempotencyInterceptor implements HandlerInterceptor {
//
//    private static final String IDEMPOTENCY_KEY_HEADER = "X-Idempotency-Key";
//    private static final Set<String> IDEMPOTENT_METHODS = Set.of("POST", "PUT", "PATCH");
//
//    private final IIdempotencyService idempotencyService;
//
//    @Override
//    public boolean preHandle(HttpServletRequest request,
//                             HttpServletResponse response,
//                             Object handler) throws Exception {
//
//        // Filter by Method
//        if (!IDEMPOTENT_METHODS.contains(request.getMethod().toUpperCase())) {
//            return true;
//        }
//
//        String keyId = request.getHeader(IDEMPOTENCY_KEY_HEADER);
//
//        if (StringUtils.isBlank(keyId)) {
//            // Log as warning or enforce 400 Bad Request depending on API strictness
//            return true;
//        }
//
//        try {
//            IdempotencyKey key = idempotencyService.checkAndStartProcessing(keyId);
//
//            if (key.getStatus() == IdempotencyKey.IdempotencyStatus.COMPLETED) {
//                log.warn("[IDEMPOTENCY][DUPLICATE] Duplicated request detected for keyId: {}", keyId);
//
//                renderCachedResponse(response, key);
//                return false;
//            }
//
//            request.setAttribute("idempotencyKeyId", keyId);
//            return true;
//
//        } catch (DuplicateRequestException e) {
//            log.warn("[IDEMPOTENCY][DUPLICATE] Request for keyId: {} is already in progress.", keyId);
//
//            response.sendError(HttpStatus.CONFLICT.value(), e.getMessage());
//            return false;
//        }
//    }
//
//    private void renderCachedResponse(
//            HttpServletResponse response, IdempotencyKey key)
//            throws IOException {
//
//        response.setStatus(HttpStatus.OK.value());
//        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
//        response.getWriter().write(key.getResponseBody());
//        response.getWriter().flush();
//    }
//}

public class IdempotencyInterceptor {
}