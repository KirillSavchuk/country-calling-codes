package lv.savchuk.country.calling.codes.exception;


public class CountryCallingCodesNotInitializedException extends Exception {

	public CountryCallingCodesNotInitializedException() {
		super("Country Calling Codes database is not initialized. Please retry later or hire a new developer!");
	}

}