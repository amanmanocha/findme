package com.epam.hackfest.findme;

import java.util.ArrayList;
import java.util.List;

public class User {

    private final String name;
    private final List<String> phones=new ArrayList<String>();

    public User(String name) {
        this.name = name;
    }

	public String getName() {
		return name;
	}
	
	public void addPhone(String phone){
		phones.add(phone);
	}

	public List<String> getPhones() {
		return phones;
	}
}
