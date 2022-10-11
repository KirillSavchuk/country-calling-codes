package lv.savchuk.country.calling.codes.web.dto;

import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value
@Builder
@RequiredArgsConstructor
public class PhoneNumberValidateResponse {

	String countryName;
	String countryFlagUrl;
	String countryCallingCode;

}