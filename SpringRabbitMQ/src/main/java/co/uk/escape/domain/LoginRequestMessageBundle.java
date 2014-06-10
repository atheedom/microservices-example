package co.uk.escape.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class LoginRequestMessageBundle implements MessageBundle{
	
	@JsonDeserialize(as=LoginRequest.class)
	private LoginRequest payload;
	
	@JsonDeserialize(as=ArrayList.class)	
	private List<String> permissions;
	
	@JsonDeserialize(as=HttpHeaders.class)	
	private HttpHeaders headers;	

	
	
	public LoginRequestMessageBundle(){}
		
	public LoginRequestMessageBundle(LoginRequest payload, HttpHeaders headers){
		this.payload = payload;
		this.headers = headers;
	}
	
	@JsonDeserialize(as=LoginRequest.class)
	public LoginRequest getPayload() {
		return payload;
	}

	public void setPayload(LoginRequest payload) {
		this.payload = payload;
	}
	
	@JsonDeserialize(as=ArrayList.class)	
	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	@JsonDeserialize(as=HttpHeaders.class)	
	public HttpHeaders getHeaders() {
		return headers;
	}

	public void setHeaders(HttpHeaders headers) {
		this.headers = headers;
	}
	
	
	@Override
	public String toString() {
		return "MessageBundle [payload=" + payload + ", permissions=" + permissions + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((payload == null) ? 0 : payload.hashCode());
		result = prime * result
				+ ((permissions == null) ? 0 : permissions.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		LoginRequestMessageBundle other = (LoginRequestMessageBundle) obj;
		if (payload == null) {
			if (other.payload != null)
				return false;
		} else if (!payload.equals(other.payload))
			return false;
		if (permissions == null) {
			if (other.permissions != null)
				return false;
		} else if (!permissions.equals(other.permissions))
			return false;
		return true;
	}


}
