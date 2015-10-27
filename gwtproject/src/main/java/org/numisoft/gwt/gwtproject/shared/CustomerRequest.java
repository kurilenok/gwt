package org.numisoft.gwt.gwtproject.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CustomerRequest implements IsSerializable {

	// String firstNameMetaphone;
	// String lastNameMetaphone;

	private String firstName;
	private String lastName;

	public CustomerRequest() {

	}

	public CustomerRequest(String firstName, String lastName) {
		this.firstName = firstName;
		this.lastName = lastName;

		// Metaphone metaphone = new Metaphone();
		//
		// firstNameMetaphone = metaphone.encode(firstName);
		// lastNameMetaphone = metaphone.encode(lastName);

	}

	public CustomerRequest(String firstName) {
		this.firstName = firstName;
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
