package org.numisoft.gwt.gwtproject.shared;

import com.google.gwt.user.client.rpc.IsSerializable;

public class Customer implements IsSerializable {

	private String title;
	private String firstName;
	private String lastName;
	private String customerType;
	private String modifiedWhen;

	public Customer() {
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
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

	public String getCustomerType() {
		return customerType;
	}

	public void setCustomerType(String customerType) {
		this.customerType = customerType;
	}

	public String getModifiedWhen() {
		return modifiedWhen;
	}

	public void setModifiedWhen(String string) {
		this.modifiedWhen = string;
	}

}
