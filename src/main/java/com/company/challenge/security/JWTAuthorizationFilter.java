package com.company.challenge.security;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.company.challenge.config.JwtConfig;
import com.company.challenge.userapi.message.Message;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Jwts;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	private JwtConfig jwtConfig;
	
	public JWTAuthorizationFilter(AuthenticationManager authManager, JwtConfig jwtConfig) {
		super(authManager);
		this.jwtConfig = jwtConfig;
	}

	@Override
	protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
			throws IOException, ServletException {
		String header = req.getHeader(jwtConfig.getHeaderString());
		if (header == null || !header.startsWith(jwtConfig.getTokenPrefix())) {
			chain.doFilter(req, res);
			return;
		}
		try {
			UsernamePasswordAuthenticationToken authentication = getAuthentication(req);
			SecurityContextHolder.getContext().setAuthentication(authentication);
			chain.doFilter(req, res);
		} catch (io.jsonwebtoken.ExpiredJwtException e ) {
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			Message message = new Message(Message.INVALID_SESSION);
			ObjectMapper objectMapper= new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(message);
			res.getOutputStream().write(jsonString.getBytes("UTF-8"));
			res.flushBuffer();
			return;
		} catch (io.jsonwebtoken.SignatureException e ) {
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			Message message = new Message(Message.NOT_AUTHORIZED);
			ObjectMapper objectMapper= new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(message);
			res.getOutputStream().write(jsonString.getBytes("UTF-8"));
			res.flushBuffer();
			return;
		} catch (io.jsonwebtoken.MalformedJwtException e) {
			res.setContentType("application/json");
			res.setCharacterEncoding("UTF-8");
			res.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
			Message message = new Message(Message.NOT_AUTHORIZED);
			ObjectMapper objectMapper= new ObjectMapper();
			String jsonString = objectMapper.writeValueAsString(message);
			res.getOutputStream().write(jsonString.getBytes("UTF-8"));
			res.flushBuffer();
			return;
		}
	}

	private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
		String token = request.getHeader(jwtConfig.getHeaderString());
		if (token != null) {
			// parse the token.
			String user = Jwts.parser().setSigningKey(jwtConfig.getSecret()).parseClaimsJws(token.replace(jwtConfig.getTokenPrefix(), "")).getBody()
					.getSubject();
			if (user != null) {
				return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
			}
			return null;
		}
		return null;
	}
}