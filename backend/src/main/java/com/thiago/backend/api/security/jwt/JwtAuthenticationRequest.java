package com.thiago.backend.api.security.jwt;

import java.io.Serializable;

public class JwtAuthenticationRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    private String email;
    private String password;

    // Construtor vazio
    public JwtAuthenticationRequest() {
    }

    // Construtor com parâmetros
    public JwtAuthenticationRequest(String email, String password) {
        setEmail(email);
        setPassword(password);
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
