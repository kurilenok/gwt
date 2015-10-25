package org.numisoft.gwt.gwtproject.shared;

import java.io.Serializable;

import org.apache.commons.codec.language.Metaphone;

public class CustomerRequest implements Serializable {

	String firstNameMetaphone;
	String lastNameMetaphone;

	public CustomerRequest(String firstName, String lastName) {

		Metaphone metaphone = new Metaphone();

		firstNameMetaphone = metaphone.encode(firstName);
		lastNameMetaphone = metaphone.encode(lastName);

	}

}
