package co.uk.escape.domain;

public class LoginResponse {
	
	private Boolean authorised;

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
	

}
