package co.uk.escape.domain;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.HttpHeaders;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RegistrationRequestMessageBundle implements MessageBundle{
	
	@JsonDeserialize(as=RegistrationRequest.class)
	private RegistrationRequest payload;
	
	@JsonDeserialize(as=ArrayList.class)	
	private List<String> permissions;
	
	@JsonDeserialize(as=HttpHeaders.class)	
	private HttpHeaders headers;	 
	
	private String method;
	
	public RegistrationRequestMessageBundle(){}
		
	public RegistrationRequestMessageBundle(RegistrationRequest payload){
		this.payload = payload;
	}
	
	@JsonDeserialize(as=RegistrationRequest.class)
	public RegistrationRequest getPayload() {
		return payload;
	}

	public void setPayload(RegistrationRequest payload) {
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
	
	public String getMethod() {
		return method;
	}

	public void setMethod(String method) {
		this.method = method;
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
		RegistrationRequestMessageBundle other = (RegistrationRequestMessageBundle) obj;
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
