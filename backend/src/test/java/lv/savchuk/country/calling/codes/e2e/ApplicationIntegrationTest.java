package lv.savchuk.country.calling.codes.e2e;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import lv.savchuk.country.calling.codes.e2e.stub.WikiApiGetContentStub;
import lv.savchuk.country.calling.codes.e2e.stub.WikiApiGetSectionsStub;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.web.dto.PhoneNumberValidateRequest;
import lv.savchuk.country.calling.codes.web.dto.PhoneNumberValidateResponse;
import lv.savchuk.country.calling.codes.web.error.ErrorResponse;
import lv.savchuk.country.calling.codes.web.error.InputViolation;
import org.apache.http.HttpStatus;
import org.assertj.core.groups.Tuple;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.context.WebApplicationContext;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;


@AutoConfigureMockMvc
@SpringBootTest(
		args = {"--clean-db", "--init-country-calling-codes-db"},
		webEnvironment = RANDOM_PORT
)
@ActiveProfiles({"test"})
class ApplicationIntegrationTest {

	@BeforeAll
	static void initWireMockStubs() {
		final int wireMockServerPort = 7071;
		WireMockServer wireMockServer = new WireMockServer(new WireMockConfiguration().port(wireMockServerPort));
		wireMockServer.start();
		WireMock.configureFor("localhost", wireMockServerPort);
		setupMockResponse();
	}

	public static void setupMockResponse() {
		WikiApiGetContentStub.registerStub();
		WikiApiGetSectionsStub.registerStub();
	}

	@BeforeEach
	public void initialiseRestAssuredMockMvcWebApplicationContext(
			@Autowired WebApplicationContext webApplicationContext,
			@LocalServerPort int localPort
	) {
		RestAssuredMockMvc.webAppContextSetup(webApplicationContext);
		RestAssured.port = localPort;
	}

	@Test
	void callGetCountryCallingCodesList_returnsAllCountries() {
		final CountryCallingCodes[] response = RestAssured.given()
				.when()
				.get("api/v1/country-calling-codes/list")
				.then().log().all()
				.and().assertThat().statusCode(HttpStatus.SC_OK)
				.and().extract().as(CountryCallingCodes[].class);

		assertThat(response).hasSize(7);
		assertThat(response).extracting(
				CountryCallingCodes::getName,
				CountryCallingCodes::getFlagUrl,
				CountryCallingCodes::getCodes
		).contains(
				Tuple.tuple(
						"Bahamas",
						"https://upload.wikimedia.org/wikipedia/commons/thumb/9/93/Flag_of_the_Bahamas.svg/23px-Flag_of_the_Bahamas.svg.png",
						List.of(1242)),
				Tuple.tuple(
						"Bonaire",
						"https://upload.wikimedia.org/wikipedia/commons/thumb/1/1e/Flag_of_Bonaire.svg/23px-Flag_of_Bonaire.svg.png",
						List.of(5997)),
				Tuple.tuple(
						"Globalstar (Mobile Satellite Service)",
						"",
						List.of(8818, 8819)),
				Tuple.tuple(
						"Canada",
						"https://upload.wikimedia.org/wikipedia/commons/thumb/d/d9/Flag_of_Canada_%28Pantone%29.svg/23px-Flag_of_Canada_%28Pantone%29.svg.png",
						List.of(1)),
				Tuple.tuple(
						"United States",
						"https://upload.wikimedia.org/wikipedia/en/thumb/a/a4/Flag_of_the_United_States.svg/23px-Flag_of_the_United_States.svg.png",
						List.of(1)),
				Tuple.tuple(
						"Latvia",
						"https://upload.wikimedia.org/wikipedia/commons/thumb/8/84/Flag_of_Latvia.svg/23px-Flag_of_Latvia.svg.png",
						List.of(371)),
				Tuple.tuple(
						"Kazakhstan",
						"https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Flag_of_Kazakhstan.svg/23px-Flag_of_Kazakhstan.svg.png",
						List.of(76, 77))
		);
	}

	@Test
	void callPostCountryCallingCodesValidatePhoneNumber_invalidNumber_returnsError() {
		final PhoneNumberValidateRequest request = new PhoneNumberValidateRequest("+371ABCDEF000");
		final ErrorResponse error = validatePhoneNumber(request, ErrorResponse.class, HttpStatus.SC_BAD_REQUEST);

		final List<InputViolation> violations = error.getViolations();
		assertThat(error.getErrorMessage()).isNull();
		assertThat(violations).hasSize(1);
		assertThat(violations.get(0).getFieldName()).isEqualTo("phoneNumber");
		assertThat(violations.get(0).getMessage()).isEqualTo("Phone Number should match Regex: '\\+?[0-9\\s]+'");
	}

	@Test
	void callPostCountryCallingCodesValidatePhoneNumber_codeNotFound_returnsError() {
		final PhoneNumberValidateRequest request = new PhoneNumberValidateRequest("+99999999999");
		final ErrorResponse error = validatePhoneNumber(request, ErrorResponse.class, HttpStatus.SC_NOT_FOUND);

		assertThat(error.getViolations()).isNull();
		assertThat(error.getErrorMessage()).isEqualTo("Failed to find Country Calling Code for provided '+99999999999' phone number.");
	}

	@ParameterizedTest(name = "[{index}] Phone number '{0}' to be from '{1}' country")
	@MethodSource("callPostCountryCallingCodesValidatePhoneNumber_returnsValidResult_data")
	void callPostCountryCallingCodesValidatePhoneNumber_returnsValidResult(String phoneNumber, String countryName, String callingCode) {
		final PhoneNumberValidateRequest request = new PhoneNumberValidateRequest(phoneNumber);
		final PhoneNumberValidateResponse response = validatePhoneNumber(request, PhoneNumberValidateResponse.class, HttpStatus.SC_OK);

		assertThat(response.getCountryName()).isEqualTo(countryName);
		assertThat(response.getCountryCallingCode()).isEqualTo(callingCode);
	}

	static Stream<Arguments> callPostCountryCallingCodesValidatePhoneNumber_returnsValidResult_data() {
		return Stream.of(
				Arguments.of("+371 123456789", "Latvia", "+371"),
				Arguments.of("+1 2 4 2 4 2 4 2 4 2", "Bahamas", "+1242"),
				Arguments.of("+88190000000", "Globalstar (Mobile Satellite Service)", "+8819"),
				Arguments.of("+371 123456789", "Latvia", "+371")
		);
	}

	private <T> T validatePhoneNumber(PhoneNumberValidateRequest request, Class<T> responseCls, int expectedStatusCode) {
		return RestAssured.given()
				.when()
				.body(request)
				.contentType(ContentType.JSON)
				.post("api/v1/country-calling-codes/validate-phone-number")
				.then().log().all()
				.and().assertThat().statusCode(expectedStatusCode)
				.and().extract().as(responseCls);
	}

}