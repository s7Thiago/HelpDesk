package com.thiago.backend.api.security.jwt;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import com.thiago.backend.api.entity.User;
import com.thiago.backend.api.enums.ProfileEnum;

/*
 * Classe Factory que converte um usuário (User), para um tipo reconhecível
 * pelo SpringSecurity
 * 
 * */
public class JwtUserFactory {
	private JwtUserFactory() {
		
	}
	
	/*
	 * gera um JwtUser com base nos dados de um User, dessa forma, convertendo o
	 * perfil do usuário para o formato usado pelo SpringSecurity
	 * 
	 * */
	public static JwtUser create(User user) {
		return new JwtUser(user.getId(), user.getEmail(), user.getPassword(), mapToGrantedAuthorities(user.getProfile()));
	}
	
	private static List<GrantedAuthority> mapToGrantedAuthorities(ProfileEnum profileEnum){
		List<GrantedAuthority> authorities = new ArrayList<GrantedAuthority>();
		authorities.add(new SimpleGrantedAuthority(profileEnum.toString()));
		return authorities;
	}
}
