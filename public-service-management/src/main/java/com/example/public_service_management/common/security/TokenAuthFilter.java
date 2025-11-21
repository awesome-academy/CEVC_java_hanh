package com.example.public_service_management.common.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.public_service_management.common.exceptions.UnauthorizedException;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.common.utils.TokenUtil;
import com.example.public_service_management.user.CustomUserDetails;
import com.example.public_service_management.user_session.UserSession;
import com.example.public_service_management.user_session.UserSessionRepository;

import io.jsonwebtoken.JwtException;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class TokenAuthFilter extends OncePerRequestFilter {
  private final JwtTokenProvider jwtTokenProvider;
  private final UserSessionRepository userSessionRepository;
  private final TokenUtil tokenUtil;
  private final I18nUtil i18nUtil;

  @Value("${security.public-paths}")
  private String publicPathsConfig;

  private String[] publicPaths;

  @PostConstruct
  public void init() {
    publicPaths = publicPathsConfig.split(",");
  }

  @Override
  protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
    return Arrays.stream(publicPaths).anyMatch(request.getRequestURI()::startsWith);
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    try {
      String accessToken = tokenUtil.getAccessToken(request);
      String sessionId = jwtTokenProvider.getSessionId(accessToken);
      UserSession userSession = userSessionRepository.findBySessionId(sessionId)
          .orElseThrow(() -> new UnauthorizedException(i18nUtil.get("error.invalid_access_token")));
      CustomUserDetails userDetails = new CustomUserDetails(userSession.getUser());
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(userDetails, null,
          userDetails.getAuthorities());

      SecurityContextHolder.getContext().setAuthentication(auth);

      filterChain.doFilter(request, response);
    } catch (JwtException e) {
      writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, i18nUtil.get("error.invalid_access_token"));
    } catch (UnauthorizedException e) {
      writeErrorResponse(response, HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
    } catch (Exception e) {
      e.printStackTrace();
      writeErrorResponse(response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR, i18nUtil.get("error.unexpected"));
    }
  }

  private void writeErrorResponse(HttpServletResponse response, int status, String message) throws IOException {
    response.setStatus(status);
    response.setContentType("application/json;charset=UTF-8");
    response.getWriter().write("{\"error\":\"" + message + "\"}");
  }
}
