package lv.savchuk.country.calling.codes.service.init;

import lv.savchuk.country.calling.codes.api.CountryCallingCodesProvider;
import lv.savchuk.country.calling.codes.api.CountryCallingCodesService;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.DefaultApplicationArguments;

import java.util.List;

import static lv.savchuk.country.calling.codes.service.init.CountryCallingCodesInitializer.ARGUMENT;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CountryCallingCodesInitializerTest {

	private CountryCallingCodesInitializer initializer;
	private CountryCallingCodesProvider callingCodesProvider;
	private CountryCallingCodesService service;

	@BeforeEach
	void setUp() {
		this.callingCodesProvider = mock(CountryCallingCodesProvider.class);
		this.service = mock(CountryCallingCodesService.class);
		this.initializer = new CountryCallingCodesInitializer(callingCodesProvider, service);
	}

	@Test
	void shouldRun_returnTrue() {
		final DefaultApplicationArguments args = new DefaultApplicationArguments("--" + ARGUMENT);
		assertTrue(initializer.shouldRun(args));
	}

	@Test
	void shouldRun_returnFalse() {
		final DefaultApplicationArguments args = new DefaultApplicationArguments("--dummy-argument");
		assertFalse(initializer.shouldRun(args));
	}

	@Test
	void run() {
		final List<CountryCallingCodes> codes = List.of(CountryCallingCodes.builder().build(), CountryCallingCodes.builder().build());
		when(callingCodesProvider.collectCountryData()).thenReturn(codes);

		initializer.run(null);

		verify(callingCodesProvider).collectCountryData();
		verify(service).add(codes);
		verifyNoMoreInteractions(callingCodesProvider, service);
	}

}