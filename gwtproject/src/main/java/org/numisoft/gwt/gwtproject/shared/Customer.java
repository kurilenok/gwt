package org.numisoft.gwt.gwtproject.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Customer implements IsSerializable {

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
