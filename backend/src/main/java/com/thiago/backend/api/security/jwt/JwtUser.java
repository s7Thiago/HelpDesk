package com.thiago.backend.api.security.jwt;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;

/*
 * O SpringSecurity depende de alguma coisa que implemente a interface UserDetails, pois é através
 * dessa implementação que ele será capaz de controlar quem está autenticado na aplicação
 * 
 * */
public class JwtUser implements UserDetails{
	private static final long serialVersionUID = -9136227786974184866L;
	
	private final String id;
	private final String userName;
	private final String password;
	private final Collection<? extends GrantedAuthority> authorities;
	
	public JwtUser(String id, String userName, String password, Collection<? extends GrantedAuthority> authorities) {
		super();
		this.id = id;
		this.userName = userName;
		this.password = password;
		this.authorities = authorities;
	}

	@JsonIgnore
	public String getId() {
		return id;
	}
	
	@Override
	public String getUsername() {
		return userName;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}

	@JsonIgnore
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	
	@JsonIgnore
	@Override
	public String getPassword() {
		return password ;
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	@Override
	public boolean isEnabled() {
		return true;
	}
	
	
	

}
