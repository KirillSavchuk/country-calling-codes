package lv.savchuk.country.calling.codes.service.mapper.impl;

import lv.savchuk.country.calling.codes.db.entities.CountryCallingCodeEntity;
import lv.savchuk.country.calling.codes.db.entities.CountryEntity;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.service.mapper.Mapper;
import lv.savchuk.country.calling.codes.service.mapper.MapperStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FromCountryCallingCodeEntityToCountryCallingCodesMapper implements Mapper<CountryCallingCodeEntity, CountryCallingCodes> {

	@Override
	public MapperStrategy<CountryCallingCodeEntity, CountryCallingCodes> getMapperStrategy() {
		return new MapperStrategy<>(CountryCallingCodeEntity.class, CountryCallingCodes.class);
	}

	@Override
	public CountryCallingCodes mapFrom(CountryCallingCodeEntity callingCodeEntity) {
		final CountryEntity countryEntity = callingCodeEntity.getCountry();
		return CountryCallingCodes.builder()
				.name(countryEntity.getName())
				.flagUrl(countryEntity.getFlagUrl())
				.codes(List.of(callingCodeEntity.getCode()))
				.build();
	}

}