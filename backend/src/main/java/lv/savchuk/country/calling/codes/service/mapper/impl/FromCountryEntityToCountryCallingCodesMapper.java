
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
public class FromCountryEntityToCountryCallingCodesMapper implements Mapper<CountryEntity, CountryCallingCodes> {

	@Override
	public MapperStrategy<CountryEntity, CountryCallingCodes> getMapperStrategy() {
		return new MapperStrategy<>(CountryEntity.class, CountryCallingCodes.class);
	}

	@Override
	public CountryCallingCodes mapFrom(CountryEntity countryEntity) {
		return CountryCallingCodes.builder()
				.name(countryEntity.getName())
				.flagUrl(countryEntity.getFlagUrl())
				.codes(mapCodes(countryEntity.getCallingCodes()))
				.build();
	}

	private List<Integer> mapCodes(List<CountryCallingCodeEntity> callingCodeEntities) {
		return callingCodeEntities.stream().map(CountryCallingCodeEntity::getCode).collect(toList());
	}

}