package co.uk.escape.domain;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

public class UserInfoResponseMessageBundle implements MessageBundle{
	
	@JsonDeserialize(as=UserInfoResponse.class)
	private UserInfoResponse payload;
	
	public UserInfoResponseMessageBundle(){}
		
	public UserInfoResponseMessageBundle(UserInfoResponse payload){
		this.payload = payload;
	}
	
	@JsonDeserialize(as=UserInfoResponse.class)
	public UserInfoResponse getPayload() {
		return payload;
	}

	public void setPayload(UserInfoResponse payload) {
		this.payload = payload;
	}


}
