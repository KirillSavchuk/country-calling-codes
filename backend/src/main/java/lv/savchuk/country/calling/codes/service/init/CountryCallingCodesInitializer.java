package lv.savchuk.country.calling.codes.service.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.savchuk.country.calling.codes.api.CountryCallingCodesProvider;
import lv.savchuk.country.calling.codes.api.CountryCallingCodesService;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@Order
@RequiredArgsConstructor
public class CountryCallingCodesInitializer implements AppInitializer {

    private static final String ARGUMENT = "init-country-calling-codes-db";

    private final CountryCallingCodesProvider callingCodesProvider;
    private final CountryCallingCodesService service;

    @Override
    public boolean shouldRun(ApplicationArguments args) {
        return args.containsOption(ARGUMENT);
    }

    @Override
    public void run(ApplicationArguments args) {
        service.add(callingCodesProvider.collectCountryData());
    }

}
