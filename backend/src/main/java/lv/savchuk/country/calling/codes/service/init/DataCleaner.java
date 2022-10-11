package lv.savchuk.country.calling.codes.service.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.savchuk.country.calling.codes.db.repostiory.CountryCallingCodesRepository;
import lv.savchuk.country.calling.codes.db.repostiory.CountryRepository;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Order(value = 0)
@RequiredArgsConstructor
public class DataCleaner implements AppInitializer {

	static final String ARGUMENT = "clean-db";

	private final CountryCallingCodesRepository countryCallingCodesRepository;
	private final CountryRepository countryRepository;

	@Override
	public boolean shouldRun(ApplicationArguments args) {
		return args.containsOption(ARGUMENT);
	}

	@Override
	public void run(ApplicationArguments args) {
		countryCallingCodesRepository.deleteAll();
		countryRepository.deleteAll();
	}

}