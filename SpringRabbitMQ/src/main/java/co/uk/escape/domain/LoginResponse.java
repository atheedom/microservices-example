package co.uk.escape.domain;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.hateoas.ResourceSupport;

public class LoginResponse extends ResourceSupport implements MessageBase {
	
	private Boolean authorised;
	private String APIKey;
	private String secretKey;
	private Map<String, String> response;
	
	public Boolean getAuthorised() {
		return authorised;
	}

	public void setAuthorised(Boolean authorised) {
		this.authorised = authorised;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((authorised == null) ? 0 : authorised.hashCode());
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
		LoginResponse other = (LoginResponse) obj;
		if (authorised == null) {
			if (other.authorised != null)
				return false;
		} else if (!authorised.equals(other.authorised))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "LoginResponse [authorised=" + authorised + "]";
	}

	public String getAPIKey() {
		return APIKey;
	}

	public void setAPIKey(String aPIKey) {
		APIKey = aPIKey;
	}

	public String getSecretKey() {
		return secretKey;
	}

	public void setSecretKey(String secretKey) {
		this.secretKey = secretKey;
	}

	public Map<String, String> getResponse() {
		return response;
	}

	public void setResponse(Map<String, String> response) {
		this.response = response;
	}
	

}
