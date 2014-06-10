package co.uk.escape.domain;

import java.util.ArrayList;

public class UserInfoResponse implements MessageBase{
	
	private ArrayList<RegisteredUser> top100users;
	
	public UserInfoResponse(){}
	
	public UserInfoResponse(ArrayList<RegisteredUser> top100users){
		this.top100users = top100users;
	}

	public ArrayList<RegisteredUser> getTop100users() {
		return top100users;
	}

	public void setTop100users(ArrayList<RegisteredUser> top100users) {
		this.top100users = top100users;
	}
	
	

}
