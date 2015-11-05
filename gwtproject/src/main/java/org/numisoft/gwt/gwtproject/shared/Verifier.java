package org.numisoft.gwt.gwtproject.shared;

/**
 * This class is used to verify user input.
 * */
public class Verifier {

	/**
	 * This method is used to verify that user input are English chars.
	 * */
	public static boolean isEnglish(String input) {
		if (input.matches("[a-zA-Z]+") || input == "") {
			return true;
		}
		return false;
	}

	/**
	 * This method is used to verify that user input is not null and is not too
	 * long.
	 * */
	public static boolean isNotTooShortOrTooLong(String input) {
		if (input.length() > 0 && input.length() < 30) {
			return true;
		}
		return false;
	}

}
