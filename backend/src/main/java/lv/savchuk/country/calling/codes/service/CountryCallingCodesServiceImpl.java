package lv.savchuk.country.calling.codes.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.savchuk.country.calling.codes.api.CountryCallingCodesService;
import lv.savchuk.country.calling.codes.db.entities.CountryCallingCodeEntity;
import lv.savchuk.country.calling.codes.db.entities.CountryEntity;
import lv.savchuk.country.calling.codes.db.repostiory.CountryCallingCodesRepository;
import lv.savchuk.country.calling.codes.db.repostiory.CountryRepository;
import lv.savchuk.country.calling.codes.exception.CountryCallingCodeNotFoundException;
import lv.savchuk.country.calling.codes.exception.CountryCallingCodesNotInitializedException;
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

	private static final int MIN_INITIALIZED_COUNTRY_COUNT = 5;

	private final MapperFactory mapperFactory;
	private final CountryRepository countryRepository;
	private final CountryCallingCodesRepository countryCallingCodesRepository;

	@Override
	public List<CountryCallingCodes> getAll() throws CountryCallingCodesNotInitializedException {
		final List<CountryEntity> countryEntities = countryRepository.findAll();
		if (countryEntities.isEmpty()) {
			throw new CountryCallingCodesNotInitializedException();
		}
		final var mapper = mapperFactory.getMapperFor(CountryEntity.class, CountryCallingCodes.class);
		return countryEntities.stream()
				.map(mapper::mapFrom)
				.sorted(Comparator.comparing(CountryCallingCodes::getName))
				.collect(toList());
	}

	@Override
	public CountryCallingCodes findBy(String phoneNumber) throws CountryCallingCodeNotFoundException, CountryCallingCodesNotInitializedException {
		final String adjustedPhoneNumber = adjustPhoneNumber(phoneNumber);
		final List<CountryCallingCodeEntity> countryCallingCodesList = countryCallingCodesRepository.findByPhoneNumber(adjustedPhoneNumber);
		final Optional<CountryCallingCodeEntity> optCallingCode = findExactMatch(countryCallingCodesList);
		if (optCallingCode.isEmpty()) {
			if (isDatabaseInitialized()) {
				throw new CountryCallingCodeNotFoundException(phoneNumber);
			} else {
				throw new CountryCallingCodesNotInitializedException();
			}
		}
		final var mapper = mapperFactory.getMapperFor(CountryCallingCodeEntity.class, CountryCallingCodes.class);
		return mapper.mapFrom(optCallingCode.get());
	}

	private boolean isDatabaseInitialized() {
		return countryCallingCodesRepository.count() >= MIN_INITIALIZED_COUNTRY_COUNT;
	}

	private String adjustPhoneNumber(String phoneNumber) {
		return phoneNumber.trim().replaceAll("[\\s\\+]", "");
	}

	/**
	 * Method tries to find exact country calling code match by retrieved code length in scenarios,
	 * where provided <code>'phoneNumber'</code> was <code>'+1 242 xxxxxxx'</code>, which corresponds to both
	 * Canada (<code>'+1'</code> code) and Bahamas (<code>'+1242'</code> code), where Bahamas has exact match.
	 *
	 * @param options list of found {@link CountryCallingCodeEntity}
	 * @return the exact match of {@link CountryCallingCodeEntity} for provided phone number
	 */
	private Optional<CountryCallingCodeEntity> findExactMatch(List<CountryCallingCodeEntity> options) {
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