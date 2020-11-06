package com.proyecto.webflux.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proyecto.webflux.security.AuthRequest;
import com.proyecto.webflux.security.AuthResponse;
import com.proyecto.webflux.security.ErrorLogin;
import com.proyecto.webflux.security.JWTUtil;
import com.proyecto.webflux.service.IUsuarioService;

import reactor.core.publisher.Mono;

@RestController
public class LoginController {

	private JWTUtil jwtUtil;
	private IUsuarioService usuarioService;
	
	@Autowired
	public LoginController(JWTUtil jwtUtil, IUsuarioService usuarioService) {
		this.jwtUtil = jwtUtil;
		this.usuarioService = usuarioService;
	}
	
	@PostMapping("/login")
	public Mono<ResponseEntity<?>> login(@RequestBody AuthRequest authRequest){
		return usuarioService.buscarPorUsuario(authRequest.getUsername())
				.map(userDetails -> {
					if(BCrypt.checkpw(authRequest.getPassword(), userDetails.getPassword())) {
						String token = jwtUtil.generateToken(userDetails);
						Date expiracion = jwtUtil.getExpirationDateFromToken(token);
						
						return ResponseEntity.ok(new AuthResponse(token, expiracion));
					}else {
						return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new ErrorLogin("Credenciales incorrectas", new Date()));
					}
				}).defaultIfEmpty(ResponseEntity.status(HttpStatus.UNAUTHORIZED).build());
	}
	
}
