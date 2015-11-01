package org.numisoft.gwt.gwtproject.shared;

public class Verifier {

	public static boolean isEnglish(String input) {

		if (input.matches("[a-zA-Z]+") || input == "") {
			return true;
		}
		return false;
	}
}
