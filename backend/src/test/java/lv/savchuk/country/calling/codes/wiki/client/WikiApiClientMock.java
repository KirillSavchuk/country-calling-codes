package lv.savchuk.country.calling.codes.wiki.client;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import lombok.SneakyThrows;
import lv.savchuk.country.calling.codes.TestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class WikiApiClientMock {

	public static void setupMockResponse(WireMockServer mockService) {
		mockService.stubFor(request().willReturn(response()));
	}

	private static MappingBuilder request() {
		return get(urlPathEqualTo("/")); //.withQueryParam("a", equalTo("gdg"));
	}

	@SneakyThrows
	private static ResponseDefinitionBuilder response() {
		final String jsonResponse = TestUtils.readFile("wiki/json/e2e/wiki-content-response.json", WikiApiClientMock.class.getClass());
		return aResponse()
				.withStatus(HttpStatus.OK.value())
				.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.withBody(jsonResponse);
	}

}