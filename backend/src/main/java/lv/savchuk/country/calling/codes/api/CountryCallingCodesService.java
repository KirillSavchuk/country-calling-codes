package lv.savchuk.country.calling.codes.api;

import lv.savchuk.country.calling.codes.exception.CountryCallingCodeNotFoundException;
import lv.savchuk.country.calling.codes.exception.CountryCallingCodesNotInitializedException;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;

import java.util.List;


public interface CountryCallingCodesService {

	List<CountryCallingCodes> getAll() throws CountryCallingCodesNotInitializedException;

	CountryCallingCodes findBy(String phoneNumber) throws CountryCallingCodeNotFoundException, CountryCallingCodesNotInitializedException;

	void add(List<CountryCallingCodes> countryCallingCodesList);

}