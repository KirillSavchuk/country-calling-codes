package lv.savchuk.country.calling.codes.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import lv.savchuk.country.calling.codes.api.CountryCallingCodesService;
import lv.savchuk.country.calling.codes.exception.CountryCallingCodeNotFoundException;
import lv.savchuk.country.calling.codes.exception.CountryCallingCodesNotInitializedException;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.service.mapper.Mapper;
import lv.savchuk.country.calling.codes.service.mapper.MapperFactory;
import lv.savchuk.country.calling.codes.web.dto.PhoneNumberValidateRequest;
import lv.savchuk.country.calling.codes.web.dto.PhoneNumberValidateResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@CrossOrigin("http://localhost:8080")
@RequestMapping("api/v1/country-calling-codes")
public class CountryCallingCodesController {

    private final Mapper<CountryCallingCodes, PhoneNumberValidateResponse> phoneNumberValidateResponseMapper;
    private final CountryCallingCodesService countryCallingCodesService;

    public CountryCallingCodesController(CountryCallingCodesService countryCallingCodesService, MapperFactory mapperFactory) {
        this.countryCallingCodesService = countryCallingCodesService;
        this.phoneNumberValidateResponseMapper = mapperFactory.getMapperFor(CountryCallingCodes.class, PhoneNumberValidateResponse.class);
    }

    @GetMapping("/list")
    @Operation(summary = "Get All Country Calling Codes")
    public ResponseEntity<List<CountryCallingCodes>> getAll()
            throws CountryCallingCodesNotInitializedException {
        return ResponseEntity.ok(countryCallingCodesService.getAll());
    }

    @PostMapping("/validate-phone-number")
    @Operation(summary = "Validate Phone Number")
    public ResponseEntity<PhoneNumberValidateResponse> checkPhoneNumber(@RequestBody @Valid PhoneNumberValidateRequest request)
            throws CountryCallingCodeNotFoundException, CountryCallingCodesNotInitializedException {
        final CountryCallingCodes countryCallingCodes = countryCallingCodesService.findBy(request.getPhoneNumber());
        return ResponseEntity.ok(phoneNumberValidateResponseMapper.mapFrom(countryCallingCodes));
    }

}

