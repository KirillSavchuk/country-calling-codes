package lv.savchuk.country.calling.codes.service.init;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.boot.ApplicationArguments;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


class AppInitializeServiceTest {

	private AppInitializeService service;
	private AppInitializer firstAppInitializer;
	private AppInitializer secondAppInitializer;

	private ApplicationArguments args;

	@BeforeEach
	void setUp() {
		this.args = mock(ApplicationArguments.class);
		this.firstAppInitializer = mock(AppInitializer.class);
		this.secondAppInitializer = mock(AppInitializer.class);
		this.service = new AppInitializeService(List.of(firstAppInitializer, secondAppInitializer));
	}

	@Test
	void run_shouldRunOnlyEnabled() {
		when(firstAppInitializer.shouldRun(args)).thenReturn(true);
		when(secondAppInitializer.shouldRun(args)).thenReturn(false);

		service.run(args);
		verify(firstAppInitializer).run(args);
		verify(secondAppInitializer, never()).run(args);
	}

	@Test
	void run_shouldDoNothing_ifNotEnabled() {
		when(firstAppInitializer.shouldRun(args)).thenReturn(false);
		when(secondAppInitializer.shouldRun(args)).thenReturn(false);

		service.run(args);
		verify(firstAppInitializer).shouldRun(args);
		verify(secondAppInitializer).shouldRun(args);
		verify(firstAppInitializer, never()).run(args);
		verify(secondAppInitializer, never()).run(args);
	}

}

