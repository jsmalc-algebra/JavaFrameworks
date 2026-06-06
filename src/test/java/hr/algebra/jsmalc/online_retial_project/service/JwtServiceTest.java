package hr.algebra.jsmalc.online_retial_project.service;

import hr.algebra.jsmalc.online_retial_project.service.JwtService;
import hr.algebra.jsmalc.online_retial_project.service.RefreshTokenService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest(JwtService.class)
public class JwtServiceTest {

    @MockitoBean
    private RefreshTokenService refreshTokenService;

    @MockitoBean
    private UserDetailsService userDetailsService;

    private JwtService jwtService;

    @BeforeEach
    public void setUp() {jwtService = new JwtService(refreshTokenService);}

    @Test
    public void test_jwt_token_create_token() {
        String jwtToken = jwtService.generateToken("username");


        verify(refreshTokenService, times(0)).createRefreshToken(any());
        assertFalse(jwtToken.isEmpty());
    }
}
