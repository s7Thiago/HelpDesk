package com.thiago.backend.api.security.jwt;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

public class JwtTokenUtil implements Serializable{
	private static final long serialVersionUID = 1L;
	
	static final String CLAIM_KEY_USERNAME = "sub";
	static final String CLAIM_KEY_CREATED = "created";
	static final String CLAIM_KEY_EXPIRED = "exp";
	
	@Value("${jwt.secret}")
	private String secret;
	
	@Value("${jwt.expiration}")
	private Long expiration;
	
	/*
	 * Obtém o email do usuário que está dentro do token
	 * 
	 * */
	public String getUserNameFromToken(String token) {
		String userName;
		
		try {
			final Claims claims = getClaimsFromToken(token);
			userName = claims.getSubject();
			
		} catch (Exception e) {
			userName = null;
		}
		return userName;
	}
	
	/*
	 * Retorna a data de expiração de um token jwt
	 * 
	 * */
	 public Date getExpirationDateFromToken(String token) {
		 Date expiration;
		 
		 try {
			 final Claims claims = getClaimsFromToken(token);
			 expiration = claims.getExpiration();
		 } catch (Exception e) {
			expiration = null;
		}
		 return expiration;
	 }
	 
	 /*
	  * Faz o parse do token jwt para extrair as informações contidas no corpo dele
	  * 
	  * */
	 public Claims getClaimsFromToken(String token) {
		 Claims claims;
		 
		 try {
			 claims = Jwts.parser()
					 .setSigningKey(secret)
					 .parseClaimsJws(token)
					 .getBody();
		 } catch (Exception e) {
			claims = null;
		}
		 return claims;
	 }
	 
	 /*
	  * Verifica se o token está expirado
	  * 
	  * */
	  private Boolean isTokenExpired(String token) {
		  final Date expiration = getExpirationDateFromToken(token);
		  return expiration.before(expiration);
	  }
	  
	  /*
	   * Responsável por gerar o token
	   * 
	   * */
	  public String generateToken(UserDetails userDetails) {
		  Map<String, Object> claims = new HashMap<>();
		  
		  claims.put(CLAIM_KEY_USERNAME, userDetails.getUsername());
		  
		  final Date createdDate = new Date();
		  claims.put(CLAIM_KEY_CREATED, createdDate);
		  
		  return doGenerateToken(claims);
	  }
	  
	  /*
	   * Auxilia o generateToken na criação do token jwt
	   * */
	  public String doGenerateToken(Map<String, Object> claims){
		  final Date createdDate = (Date) claims.get(CLAIM_KEY_CREATED);
		  final Date expirationDate = new Date(createdDate.getTime() + expiration * 1000);
		  return Jwts.builder()
				  .setClaims(claims)
				  .setExpiration(expirationDate)
				  .signWith(SignatureAlgorithm.HS512, secret)
				  .compact();
	  }
	  
	  /*
	   * Verifica se o token pode ser atualizado
	   * 
	   * */
	  public boolean canTokenBeRefreshed(String token) {
		  return (!isTokenExpired(token));
	  }
	  
	  /*
	   * Atualiza o token
	   * 
	   * */
	   public String refreshToken(String token) {
		   String refreshedToken;
		   
		   try {
			   final Claims claims = getClaimsFromToken(token);
			   claims.put(CLAIM_KEY_CREATED, new Date());
			   refreshedToken = doGenerateToken(claims);
		   } catch (Exception e) {
			refreshedToken = null;
		}
		   return refreshedToken;
	   }
	   
	   /*
	    * Verifica se o token está válido
	    * 
	    * */
	   public Boolean validateToken(String token, UserDetails userDetails) {
		   JwtUser user = (JwtUser) userDetails;
		   
		   final String username = getUserNameFromToken(token);
		   return (username.equals(user.getUsername()) && !isTokenExpired(token));
	   }

}





















