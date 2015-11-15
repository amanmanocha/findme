package com.epam.hackfest.findme.domain;

public class Request {
	
	private String fromUserName;
	
	private String fromPhoneNumber;

	public String getFromUserName() {
		return fromUserName;
	}

	public void setFromUserName(String fromUserName) {
		this.fromUserName = fromUserName;
	}

	public String getFromPhoneNumber() {
		return fromPhoneNumber;
	}

	public void setFromPhoneNumber(String fromPhoneNumber) {
		this.fromPhoneNumber = fromPhoneNumber;
	}
	
	public String toString(){
		return fromUserName + " has requested to see your new number";
	}

}
