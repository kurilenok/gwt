package org.numisoft.gwt.gwtproject.client;

import java.io.Serializable;

public class Customer implements Serializable {

	String firstName;
	String lastName;

	public Customer() {

	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

}
