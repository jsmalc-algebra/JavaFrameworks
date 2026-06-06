package hr.algebra.jsmalc.online_retial_project.filter;

import hr.algebra.jsmalc.online_retial_project.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.util.Collections;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JwtAuthFilter Tests")
public class JwtAuthFilterTest {

    @Mock
    private JwtService jwtService;

    @Mock
    private UserDetailsService userDetailsService;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain filterChain;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private JwtAuthFilter jwtAuthFilter;

    private UserDetails testUserDetails;
    private static final String VALID_TOKEN = "valid.jwt.token";
    private static final String TEST_USERNAME = "test_user";
    private static final String BEARER_TOKEN = "Bearer " + VALID_TOKEN;
    private static final String PROTECTED_URI = "/warehouse/rest/products";

    @BeforeEach
    void setUp() {
        testUserDetails = User.builder()
                .username(TEST_USERNAME)
                .password("password")
                .authorities(Collections.emptyList())
                .build();

        SecurityContextHolder.setContext(securityContext);
    }


    @Nested
    @DisplayName("Non-protected URI")
    class NonProtectedUriTests {

        @Test
        @DisplayName("Should pass through filter without JWT checks for non-protected URI")
        void doFilterInternal_NonProtectedUri_PassesThrough() throws Exception {
            when(request.getRequestURI()).thenReturn("/warehouse/login");

            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain, times(1)).doFilter(request, response);
            verifyNoInteractions(jwtService, userDetailsService);
        }

        @Test
        @DisplayName("Should pass through filter for root URI")
        void doFilterInternal_RootUri_PassesThrough() throws Exception {
            when(request.getRequestURI()).thenReturn("/");

            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            verify(filterChain, times(1)).doFilter(request, response);
            verifyNoInteractions(jwtService, userDetailsService);
        }
    }

    @Nested
    @DisplayName("Valid JWT token")
    class ValidTokenTests {

        @Test
        @DisplayName("Should authenticate user when valid Bearer token is provided")
        void doFilterInternal_ValidToken_AuthenticatesUser() throws Exception {
            when(request.getRequestURI()).thenReturn(PROTECTED_URI);
            when(request.getHeader("Authorization")).thenReturn(BEARER_TOKEN);
            when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(TEST_USERNAME);
            when(securityContext.getAuthentication()).thenReturn(null);
            when(userDetailsService.loadUserByUsername(TEST_USERNAME)).thenReturn(testUserDetails);
            when(jwtService.validateToken(VALID_TOKEN, testUserDetails)).thenReturn(true);

            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            verify(jwtService).extractUsername(VALID_TOKEN);
            verify(userDetailsService).loadUserByUsername(TEST_USERNAME);
            verify(jwtService).validateToken(VALID_TOKEN, testUserDetails);
            verify(securityContext).setAuthentication(any());
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should not re-authenticate when SecurityContext already has authentication")
        void doFilterInternal_AlreadyAuthenticated_SkipsAuthentication() throws Exception {
            Authentication existingAuth = mock(Authentication.class);

            when(request.getRequestURI()).thenReturn(PROTECTED_URI);
            when(request.getHeader("Authorization")).thenReturn(BEARER_TOKEN);
            when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(TEST_USERNAME);
            when(securityContext.getAuthentication()).thenReturn(existingAuth);

            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            verify(jwtService).extractUsername(VALID_TOKEN);
            verifyNoInteractions(userDetailsService);
            verify(securityContext, never()).setAuthentication(any());
            verify(filterChain).doFilter(request, response);
        }
    }


    @Nested
    @DisplayName("Missing or malformed Authorization header")
    class MissingOrMalformedHeaderTests {

        @Test
        @DisplayName("Should return 401 and still call filterChain when Authorization header is missing")
        void doFilterInternal_NoAuthHeader_Returns401AndContinues() throws Exception {
            when(request.getRequestURI()).thenReturn(PROTECTED_URI);
            when(request.getHeader("Authorization")).thenReturn(null);

            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(filterChain, times(2)).doFilter(request, response);
            verifyNoInteractions(jwtService, userDetailsService);
        }

        @Test
        @DisplayName("Should return 401 when Authorization header does not start with 'Bearer '")
        void doFilterInternal_NonBearerHeader_Returns401() throws Exception {
            when(request.getRequestURI()).thenReturn(PROTECTED_URI);
            when(request.getHeader("Authorization")).thenReturn("Basic dXNlcjpwYXNz");

            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(filterChain, times(2)).doFilter(request, response);
            verifyNoInteractions(jwtService, userDetailsService);
        }

        @Test
        @DisplayName("Should return 401 when Authorization header is an empty string")
        void doFilterInternal_EmptyAuthHeader_Returns401() throws Exception {
            when(request.getRequestURI()).thenReturn(PROTECTED_URI);
            when(request.getHeader("Authorization")).thenReturn("");

            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            verify(response).setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            verify(filterChain, times(2)).doFilter(request, response);
            verifyNoInteractions(jwtService, userDetailsService);
        }
    }


    @Nested
    @DisplayName("Invalid JWT token")
    class InvalidTokenTests {

        @Test
        @DisplayName("Should not set authentication when token fails validation")
        void doFilterInternal_InvalidToken_DoesNotAuthenticate() throws Exception {
            when(request.getRequestURI()).thenReturn(PROTECTED_URI);
            when(request.getHeader("Authorization")).thenReturn(BEARER_TOKEN);
            when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(TEST_USERNAME);
            when(securityContext.getAuthentication()).thenReturn(null);
            when(userDetailsService.loadUserByUsername(TEST_USERNAME)).thenReturn(testUserDetails);
            when(jwtService.validateToken(VALID_TOKEN, testUserDetails)).thenReturn(false);

            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            verify(jwtService).validateToken(VALID_TOKEN, testUserDetails);
            verify(securityContext, never()).setAuthentication(any());
            verify(filterChain).doFilter(request, response);
        }

        @Test
        @DisplayName("Should not attempt user lookup when username extracted from token is null")
        void doFilterInternal_NullUsernameFromToken_SkipsAuthentication() throws Exception {
            when(request.getRequestURI()).thenReturn(PROTECTED_URI);
            when(request.getHeader("Authorization")).thenReturn(BEARER_TOKEN);
            when(jwtService.extractUsername(VALID_TOKEN)).thenReturn(null);

            jwtAuthFilter.doFilterInternal(request, response, filterChain);

            verifyNoInteractions(userDetailsService);
            verify(securityContext, never()).setAuthentication(any());
            verify(filterChain).doFilter(request, response);
        }
    }
}