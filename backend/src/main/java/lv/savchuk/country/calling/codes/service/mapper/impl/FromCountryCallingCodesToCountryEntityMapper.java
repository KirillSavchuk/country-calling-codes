package lv.savchuk.country.calling.codes.service.mapper.impl;

import lv.savchuk.country.calling.codes.db.entities.CountryCallingCodeEntity;
import lv.savchuk.country.calling.codes.db.entities.CountryEntity;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.service.mapper.Mapper;
import lv.savchuk.country.calling.codes.service.mapper.MapperStrategy;
import org.springframework.stereotype.Service;

import java.util.List;

import static java.util.stream.Collectors.toList;

@Service
public class FromCountryCallingCodesToCountryEntityMapper implements Mapper<CountryCallingCodes, CountryEntity> {

	@Override
	public MapperStrategy<CountryCallingCodes, CountryEntity> getMapperStrategy() {
		return new MapperStrategy<>(CountryCallingCodes.class, CountryEntity.class);
	}

	@Override
	public CountryEntity mapFrom(CountryCallingCodes countryCodes) {
		final List<CountryCallingCodeEntity> callingCodes = countryCodes.getCodes().stream()
				.map(CountryCallingCodeEntity::new)
				.collect(toList());
		final CountryEntity countryEntity = new CountryEntity();
		countryEntity.setName(countryCodes.getName());
		countryEntity.setFlagUrl(countryCodes.getFlagUrl());
		countryEntity.addCodes(callingCodes);
		return countryEntity;
	}

}