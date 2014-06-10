package co.uk.escape.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class UserInfoRequestMessageBundle implements MessageBundle{
	
	@JsonDeserialize(as=UserInfoRequest.class)
	private UserInfoRequest payload;
	
	@JsonDeserialize(as=HttpHeaders.class)	
	private HttpHeaders headers;

	private String method;	

	public UserInfoRequestMessageBundle(){}
		
	public UserInfoRequestMessageBundle(UserInfoRequest payload, HttpHeaders headers, String method){
		this.payload = payload;
		this.headers = headers;
		this.setMethod(method);
	}
	
	
	@JsonDeserialize(as=UserInfoRequest.class)
	public UserInfoRequest getPayload() {
		return payload;
	}

	public void setPayload(UserInfoRequest payload) {
		this.payload = payload;
	}

	@JsonDeserialize(as=HttpHeaders.class)	
	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}

	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
	}

	
}
