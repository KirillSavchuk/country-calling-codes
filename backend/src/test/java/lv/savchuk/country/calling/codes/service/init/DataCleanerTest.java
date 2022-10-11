package lv.savchuk.country.calling.codes.service.init;

import lv.savchuk.country.calling.codes.db.repostiory.CountryCallingCodesRepository;
import lv.savchuk.country.calling.codes.db.repostiory.CountryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;

import static lv.savchuk.country.calling.codes.service.init.DataCleaner.ARGUMENT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@ExtendWith(MockitoExtension.class)
class DataCleanerTest {

	private DataCleaner dataCleaner;
	private CountryCallingCodesRepository countryCallingCodesRepository;
	private CountryRepository countryRepository;

	@BeforeEach
	void setUp() {
		this.countryCallingCodesRepository = mock(CountryCallingCodesRepository.class);
		this.countryRepository = mock(CountryRepository.class);
		this.dataCleaner = new DataCleaner(countryCallingCodesRepository, countryRepository);
	}

	@Test
	void shouldRun_returnTrue() {
		final DefaultApplicationArguments args = new DefaultApplicationArguments("--" + ARGUMENT);
		assertTrue(dataCleaner.shouldRun(args));
	}

	@Test
	void shouldRun_returnFalse() {
		final DefaultApplicationArguments args = new DefaultApplicationArguments("--dummy-argument");
		assertFalse(dataCleaner.shouldRun(args));
	}

	@Test
	void run() {
		dataCleaner.run(null);
		verify(countryCallingCodesRepository).deleteAll();
		verify(countryRepository).deleteAll();
		verifyNoMoreInteractions(countryCallingCodesRepository, countryRepository);
	}

}