package com.thiago.backend.api.security.controller;

import javax.naming.AuthenticationException;
import javax.servlet.http.HttpServletRequest;

import com.thiago.backend.api.entity.User;
import com.thiago.backend.api.security.config.model.CurrentUser;
import com.thiago.backend.api.security.jwt.JwtAuthenticationRequest;
import com.thiago.backend.api.security.jwt.JwtTokenUtil;
import com.thiago.backend.api.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

// Permitindo acesso de qualquer endereço IP pois a aplicação estará na porta 8080
// e i frontend estará em outra porta (na 4200, por exemplo)
@CrossOrigin(origins = "*")
@RestController
public class AuthenticatorRestController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UserService userService;

    // Cria o token de autenticação a partir de um email e senha válidos passados
    @PostMapping("/api/auth")
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtAuthenticationRequest authenticationRequest)
            throws AuthenticationException {
        final Authentication authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(authenticationRequest.getEmail(),
                        authenticationRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        final UserDetails userDetails = userDetailsService.loadUserByUsername(authenticationRequest.getEmail());
        final String token = jwtTokenUtil.generateToken(userDetails);
        final User user = userService.findByEmail(authenticationRequest.getEmail());
        user.setPassword(null);
        return ResponseEntity.ok(new CurrentUser(token, user));
    }

    // Caso necessário, permite dar um refresh no token
    @PostMapping("/api/refresh")
    public ResponseEntity<?> refreshAndGetAuthenticationToken(HttpServletRequest request) {
        String token = request.getHeader("Authorization");
        String userName = jwtTokenUtil.getUserNameFromToken(token);
        final User user = userService.findByEmail(userName);

        if (jwtTokenUtil.canTokenBeRefreshed(token)) {
            String refreshedToken = jwtTokenUtil.refreshToken(token);
            return ResponseEntity.ok(new CurrentUser(refreshedToken, user));
        } else {
            return ResponseEntity.badRequest().body(null);
        }
    }

}
