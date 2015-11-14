package com.epam.hackfest.findme.domain;

import java.util.ArrayList;
import java.util.List;

public class SearchResult {
	
	private String userName;
	
	private boolean isPrivate;
	
	private List<String> phoneNumbers;

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
	
	public boolean isPrivate() {
		return isPrivate;
	}

	public void setPrivate(boolean isPrivate) {
		this.isPrivate = isPrivate;
	}
	
	public boolean hasPhoneNumbers(){
		return phoneNumbers != null && phoneNumbers.size() > 0;
	}

	public List<String> getPhoneNumbers() {
		return phoneNumbers;
	}

	public void setPhoneNumbers(List<String> phoneNumbers) {
		this.phoneNumbers = phoneNumbers;
	}

	public void addPhoneNumber(String phoneNumber) {
		if( phoneNumbers == null ){
			phoneNumbers = new ArrayList<String>();
		}
		if( !phoneNumbers.contains(phoneNumber) ){
			phoneNumbers.add(phoneNumber);
		}
	}
	
}
