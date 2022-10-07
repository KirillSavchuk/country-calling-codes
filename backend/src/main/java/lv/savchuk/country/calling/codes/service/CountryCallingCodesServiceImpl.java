package lv.savchuk.country.calling.codes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.savchuk.country.calling.codes.api.CountryCallingCodesService;
import lv.savchuk.country.calling.codes.db.entities.CountryCallingCodeEntity;
import lv.savchuk.country.calling.codes.db.entities.CountryEntity;
import lv.savchuk.country.calling.codes.db.repostiory.CountryCallingCodesRepository;
import lv.savchuk.country.calling.codes.db.repostiory.CountryRepository;
import lv.savchuk.country.calling.codes.exception.CountryCallingCodeNotFoundException;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.service.mapper.MapperFactory;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static java.util.stream.Collectors.toList;

@Slf4j
@Service
@RequiredArgsConstructor
public class CountryCallingCodesServiceImpl implements CountryCallingCodesService {

	private final MapperFactory mapperFactory;
	private final CountryRepository countryRepository;
	private final CountryCallingCodesRepository countryCallingCodesRepository;

	@Override
	public List<CountryCallingCodes> getAll() {
		final List<CountryEntity> countryEntities = countryRepository.findAll();
		if (countryEntities.isEmpty()) {
			//TODO: exception
		}
		final var mapper = mapperFactory.getMapperFor(CountryEntity.class, CountryCallingCodes.class);
		return countryEntities.stream().map(mapper::mapFrom).collect(toList());
	}

	@Override
	public CountryCallingCodes findBy(String phoneNumber) throws CountryCallingCodeNotFoundException {
		final String adjustedPhoneNumber = adjustPhoneNumber(phoneNumber);
		final List<CountryCallingCodeEntity> countryCallingCodesList = countryCallingCodesRepository.findByPhoneNumber(adjustedPhoneNumber);
		final CountryCallingCodeEntity resultEntity = findProbableMatch(countryCallingCodesList)
				.orElseThrow(() -> new CountryCallingCodeNotFoundException(phoneNumber));
		final var mapper = mapperFactory.getMapperFor(CountryCallingCodeEntity.class, CountryCallingCodes.class);
		return mapper.mapFrom(resultEntity);
	}

	private String adjustPhoneNumber(String phoneNumber) {
		return phoneNumber.trim().replaceAll("[\\s\\+]", "");
	}

	/**
	 * @param options
	 * @return
	 */
	private Optional<CountryCallingCodeEntity> findProbableMatch(List<CountryCallingCodeEntity> options) {
		return options.stream().max(Comparator.comparingInt(entity -> String.valueOf(entity.getCode()).length()));
	}

	@Override
	@Transactional
	public void add(List<CountryCallingCodes> countryCallingCodesList) {
		var mapper = mapperFactory.getMapperFor(CountryCallingCodes.class, CountryEntity.class);
		final List<CountryEntity> countryEntities = countryCallingCodesList.stream().map(mapper::mapFrom).collect(toList());
		countryRepository.saveAll(countryEntities);
		log.info("Added '{}' new countries.", countryEntities.size());
	}

}