package lv.savchuk.country.calling.codes.api;


import lv.savchuk.country.calling.codes.model.CountryCallingCodes;

import java.util.List;

public interface CountryCallingCodesProvider {

	List<CountryCallingCodes> collectCountryData();

}