package lv.savchuk.country.calling.codes.service;

import lv.savchuk.country.calling.codes.api.CountryCallingCodesService;
import lv.savchuk.country.calling.codes.db.entities.CountryCallingCodeEntity;
import lv.savchuk.country.calling.codes.db.entities.CountryEntity;
import lv.savchuk.country.calling.codes.db.repostiory.CountryCallingCodesRepository;
import lv.savchuk.country.calling.codes.db.repostiory.CountryRepository;
import lv.savchuk.country.calling.codes.exception.CountryCallingCodeNotFoundException;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.service.mapper.MapperFactory;
import lv.savchuk.country.calling.codes.service.mapper.impl.FromCountryCallingCodeEntityToCountryCallingCodesMapper;
import lv.savchuk.country.calling.codes.service.mapper.impl.FromCountryCallingCodesToCountryEntityMapper;
import lv.savchuk.country.calling.codes.service.mapper.impl.FromCountryEntityToCountryCallingCodesMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryCallingCodesServiceImplTest {

	private CountryCallingCodesService service;

	private MapperFactory mapperFactory;
	private CountryRepository countryRepository;
	private CountryCallingCodesRepository countryCallingCodesRepository;

	@BeforeEach
	void setUp() {
		this.mapperFactory = initMapperFactory();
		this.countryRepository = mock(CountryRepository.class);
		this.countryCallingCodesRepository = mock(CountryCallingCodesRepository.class);
		this.service = new CountryCallingCodesServiceImpl(mapperFactory, countryRepository, countryCallingCodesRepository);
	}

	private MapperFactory initMapperFactory() {
		return new MapperFactory(List.of(
				new FromCountryCallingCodeEntityToCountryCallingCodesMapper(),
				new FromCountryCallingCodesToCountryEntityMapper(),
				new FromCountryEntityToCountryCallingCodesMapper()
		));
	}

	@Test
	void getAll() {
	}

	@Test
	void findByPhoneNumber_notFound() {
		final String invalidPhoneNumber = "+9999999999";
		when(countryCallingCodesRepository.findByPhoneNumber(any())).thenReturn(Collections.emptyList());

		final CountryCallingCodeNotFoundException ex = assertThrows(CountryCallingCodeNotFoundException.class,
				() -> service.findBy(invalidPhoneNumber));

		assertThat(ex.getMessage()).isEqualTo(format("Failed to find Country Calling Code for provided '%s' phone number.", invalidPhoneNumber));
	}

	@Test
	void findByPhoneNumber_oneValueFound() throws CountryCallingCodeNotFoundException {
		final String countryName = "Latvia";
		final String phoneNumber = "+371 123456789";
		final String adjustedPhoneNumber = "371123456789";
		final CountryCallingCodeEntity latviaCallingCode = createCountryCallingCodeEntity(countryName, 371);
		when(countryCallingCodesRepository.findByPhoneNumber(adjustedPhoneNumber)).thenReturn(List.of(latviaCallingCode));

		final CountryCallingCodes result = service.findBy(phoneNumber);

		assertThat(result.getCodes()).containsExactly(371);
		assertThat(result.getName()).isEqualTo(countryName);
		assertThat(result.getFlagUrl()).isNull();
	}

	@Test
	void findByPhoneNumber_multipleValuesFound() throws CountryCallingCodeNotFoundException {
		final int countryCallingCode = 1242;
		final String countryName = "Bahamas";
		final String phoneNumber = "+1 242 12345 6789";
		final String adjustedPhoneNumber = "1242123456789";
		final CountryCallingCodeEntity bahamasCallingCode = createCountryCallingCodeEntity(countryName, countryCallingCode);
		final CountryCallingCodeEntity canadaCallingCode = createCountryCallingCodeEntity("Canada", 1);
		final CountryCallingCodeEntity usCallingCode = createCountryCallingCodeEntity("United States,", 1);
		when(countryCallingCodesRepository.findByPhoneNumber(adjustedPhoneNumber))
				.thenReturn(List.of(usCallingCode, bahamasCallingCode, canadaCallingCode));

		final CountryCallingCodes result = service.findBy(phoneNumber);

		assertThat(result.getCodes()).containsExactly(countryCallingCode);
		assertThat(result.getName()).isEqualTo(countryName);
		assertThat(result.getFlagUrl()).isNull();
	}

	@Test
	void add() {
	}

	private CountryCallingCodeEntity createCountryCallingCodeEntity(String countryName, int callingCode) {
		final CountryEntity countryEntity = new CountryEntity();
		countryEntity.setName(countryName);
		final CountryCallingCodeEntity entity = new CountryCallingCodeEntity();
		entity.setCode(callingCode);
		entity.setCountry(countryEntity);
		return entity;
	}

}