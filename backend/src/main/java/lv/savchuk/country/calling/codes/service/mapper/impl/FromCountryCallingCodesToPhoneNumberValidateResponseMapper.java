package lv.savchuk.country.calling.codes.service.mapper.impl;

import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.service.mapper.Mapper;
import lv.savchuk.country.calling.codes.service.mapper.MapperStrategy;
import lv.savchuk.country.calling.codes.web.dto.PhoneNumberValidateResponse;
import org.springframework.stereotype.Service;

@Service
public class FromCountryCallingCodesToPhoneNumberValidateResponseMapper implements Mapper<CountryCallingCodes, PhoneNumberValidateResponse> {

	@Override
	public MapperStrategy<CountryCallingCodes, PhoneNumberValidateResponse> getMapperStrategy() {
		return new MapperStrategy<>(CountryCallingCodes.class, PhoneNumberValidateResponse.class);
	}

	@Override
	public PhoneNumberValidateResponse mapFrom(CountryCallingCodes countryCodes) {
		return PhoneNumberValidateResponse.builder()
				.countryName(countryCodes.getName())
				.countryFlagUrl(countryCodes.getFlagUrl())
				.countryCallingCode(getFormattedCallingCode(countryCodes))
				.build();
	}

	private String getFormattedCallingCode(CountryCallingCodes countryCodes) {
		return String.format("+%s", countryCodes.getCodes().get(0));
	}

}