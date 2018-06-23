package com.company.challenge.userapi.message;

public class Message {

	public static final String EMAIL_ALREADY_TAKEN = "E-mail já existente";
	public static final String INVALID_USERNAME_PASSWORD = "Usuário e/ou senha inválidos";
	public static final String NOT_AUTHORIZED = "Não autorizado";
	public static final String RESOURCE_NOT_FOUND = "Recurso não encontrado";
	public static final String INVALID_SESSION = "Sessão Inválida";
	public static final String INVALID_PRODUCT = "O produto precisa de um nome";
	public static final String PRODUCT_ALREADY_EXISTS = "Produto já existe";
	
	private String message;
	
	public Message(String message) {
		this.message = message;
	}

	public String getMessage() {
		return message;
	}

	public String toString() {
		return String.format("[message=%s]", this.message);
	}
	
	
}
