package lv.savchuk.country.calling.codes.service;

import lv.savchuk.country.calling.codes.api.CountryCallingCodesService;
import lv.savchuk.country.calling.codes.db.entities.CountryCallingCodeEntity;
import lv.savchuk.country.calling.codes.db.entities.CountryEntity;
import lv.savchuk.country.calling.codes.db.repostiory.CountryCallingCodesRepository;
import lv.savchuk.country.calling.codes.db.repostiory.CountryRepository;
import lv.savchuk.country.calling.codes.exception.CountryCallingCodeNotFoundException;
import lv.savchuk.country.calling.codes.exception.CountryCallingCodesNotInitializedException;
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

	private CountryRepository countryRepository;
	private CountryCallingCodesRepository countryCallingCodesRepository;

	@BeforeEach
	void setUp() {
		final MapperFactory mapperFactory = initMapperFactory();
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
	void getAll_dbNotInitialized() {
		when(countryRepository.findAll()).thenReturn(Collections.emptyList());
		final CountryCallingCodesNotInitializedException ex = assertThrows(CountryCallingCodesNotInitializedException.class,
				() -> service.getAll());

		assertThat(ex.getMessage()).isEqualTo("Country Calling Codes database is not initialized. Please retry later or hire a new developer!");
	}

	@Test
	void getAll_success() throws CountryCallingCodesNotInitializedException {
		final CountryEntity countryEntity = new CountryEntity();
		countryEntity.setName("CountryName");
		countryEntity.setFlagUrl("FlagUrl");
		countryEntity.addCodes(List.of(new CountryCallingCodeEntity(111), new CountryCallingCodeEntity(222)));

		when(countryRepository.findAll()).thenReturn(List.of(countryEntity));
		final List<CountryCallingCodes> countryCallingCodes = service.getAll();

		assertThat(countryCallingCodes).hasSize(1);
		assertThat(countryCallingCodes.get(0).getName()).isEqualTo("CountryName");
		assertThat(countryCallingCodes.get(0).getFlagUrl()).isEqualTo("FlagUrl");
		assertThat(countryCallingCodes.get(0).getCodes()).containsExactly(111, 222);
	}

	@Test
	void findByPhoneNumber_dbNotInitialized() {
		when(countryCallingCodesRepository.findByPhoneNumber(any())).thenReturn(Collections.emptyList());
		when(countryCallingCodesRepository.count()).thenReturn(0L);

		final CountryCallingCodesNotInitializedException ex = assertThrows(CountryCallingCodesNotInitializedException.class,
				() -> service.findBy("+37199999"));

		assertThat(ex.getMessage()).isEqualTo("Country Calling Codes database is not initialized. Please retry later or hire a new developer!");
	}

	@Test
	void findByPhoneNumber_notFound() {
		final String invalidPhoneNumber = "+9999999999";
		when(countryCallingCodesRepository.findByPhoneNumber(any())).thenReturn(Collections.emptyList());
		when(countryCallingCodesRepository.count()).thenReturn(100L);

		final CountryCallingCodeNotFoundException ex = assertThrows(CountryCallingCodeNotFoundException.class,
				() -> service.findBy(invalidPhoneNumber));

		assertThat(ex.getMessage()).isEqualTo(format("Failed to find Country Calling Code for provided '%s' phone number.", invalidPhoneNumber));
	}

	@Test
	void findByPhoneNumber_oneValueFound() throws CountryCallingCodeNotFoundException, CountryCallingCodesNotInitializedException {
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
	void findByPhoneNumber_multipleValuesFound() throws CountryCallingCodeNotFoundException, CountryCallingCodesNotInitializedException {
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

	private CountryCallingCodeEntity createCountryCallingCodeEntity(String countryName, int callingCode) {
		final CountryEntity countryEntity = new CountryEntity();
		countryEntity.setName(countryName);
		final CountryCallingCodeEntity entity = new CountryCallingCodeEntity();
		entity.setCode(callingCode);
		entity.setCountry(countryEntity);
		return entity;
	}

}