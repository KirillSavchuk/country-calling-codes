package lv.savchuk.country.calling.codes.exception;


import static java.lang.String.format;

public class CountryCallingCodeNotFoundException extends Exception {

	public CountryCallingCodeNotFoundException(String phoneNumber) {
		super(format("Failed to find Country Calling Code for provided '%s' phone number.", phoneNumber));
	}

}