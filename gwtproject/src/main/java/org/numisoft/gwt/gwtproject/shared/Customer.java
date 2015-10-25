package org.numisoft.gwt.gwtproject.shared;

import java.io.Serializable;

public class Customer implements Serializable {

	String firstName;
	String lastName;

	public Customer(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;
	}

}
