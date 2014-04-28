package co.uk.escape.domain;

import java.io.Serializable;

public class UserInfoRequest implements Serializable{
	
	private static final long serialVersionUID = 7915992167321675969L;
	private String firstname;
	
	public UserInfoRequest(){}
	
	public UserInfoRequest(String firstname){
		this.setFirstName(firstname);
	}

	public String getFirstName() {
		return firstname;
	}

	public void setFirstName(String emailAddress) {
		this.firstname = emailAddress;
	}

	@Override
	public String toString() {
		return "UserInfoRequest [firstname=" + firstname + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((firstname == null) ? 0 : firstname.hashCode());
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
		UserInfoRequest other = (UserInfoRequest) obj;
		if (firstname == null) {
			if (other.firstname != null)
				return false;
		} else if (!firstname.equals(other.firstname))
			return false;
		return true;
	}



}
