package com.pharmaprocure.portal.security;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pharmaprocure.portal.dto.ApiErrorResponse;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Clock;
import java.time.Instant;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.http.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    private static final int MAX_REQUESTS_PER_MINUTE = 60;
    private final Map<String, Deque<Instant>> requestWindows = new ConcurrentHashMap<>();
    private final Clock clock;
    private final ObjectMapper objectMapper;

    @Autowired
    public RateLimitFilter(ObjectMapper objectMapper) {
        this(objectMapper, Clock.systemUTC());
    }

    RateLimitFilter(ObjectMapper objectMapper, Clock clock) {
        this.objectMapper = objectMapper;
        this.clock = clock;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getRequestURI();
        return path.startsWith("/api/health") || path.startsWith("/api/meta") || path.startsWith("/actuator") || path.startsWith("/api/auth");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || authentication instanceof AnonymousAuthenticationToken) {
            filterChain.doFilter(request, response);
            return;
        }

        String key = authentication.getName();
        Deque<Instant> window = requestWindows.computeIfAbsent(key, ignored -> new ArrayDeque<>());
        Instant now = clock.instant();

        synchronized (window) {
            while (!window.isEmpty() && window.peekFirst().isBefore(now.minusSeconds(60))) {
                window.pollFirst();
            }
            if (window.size() >= MAX_REQUESTS_PER_MINUTE) {
                response.setStatus(429);
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                objectMapper.writeValue(response.getOutputStream(), new ApiErrorResponse(429, "Rate limit exceeded", List.of("MAX_60_REQUESTS_PER_MINUTE")));
                return;
            }
            window.addLast(now);
        }

        filterChain.doFilter(request, response);
    }
}
