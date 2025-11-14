package com.example.public_service_management.common.security;

import java.io.IOException;
import java.util.Arrays;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.example.public_service_management.citizen.auth.CitizenDetails;
import com.example.public_service_management.citizen.auth.CitizenDetailsService;
import com.example.public_service_management.common.exceptions.UnauthorizedException;
import com.example.public_service_management.common.utils.I18nUtil;
import com.example.public_service_management.common.utils.TokenUtil;

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
  private final CitizenDetailsService citizenDetailsService;
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
      CitizenDetails citizenDetails = citizenDetailsService.loadUserByUsername(sessionId);
      UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(citizenDetails, null,
          citizenDetails.getAuthorities());

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
