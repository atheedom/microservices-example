package co.uk.escape.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class RegistrationResponseMessageBundle implements MessageBundle{
	
	@JsonDeserialize(as=RegistrationResponse.class)
	private RegistrationResponse payload;
	
	@JsonDeserialize(as=ArrayList.class)	
	private List<String> permissions;
	
	public RegistrationResponseMessageBundle(){}
		
	public RegistrationResponseMessageBundle(RegistrationResponse payload){
		this.payload = payload;
	}
	
	@JsonDeserialize(as=RegistrationResponse.class)
	public RegistrationResponse getPayload() {
		return payload;
	}

	public void setPayload(RegistrationResponse payload) {
		this.payload = payload;
	}
	
	@JsonDeserialize(as=ArrayList.class)	
	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
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
		RegistrationResponseMessageBundle other = (RegistrationResponseMessageBundle) obj;
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
