package com.company.challenge.security;

import org.springframework.http.HttpMethod;

public class AntMatcher {

	private HttpMethod method;
	private String url;

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = HttpMethod.valueOf(method);
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

}
