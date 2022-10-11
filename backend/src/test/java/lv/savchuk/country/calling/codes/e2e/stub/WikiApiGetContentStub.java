package lv.savchuk.country.calling.codes.e2e.stub;


import com.github.tomakehurst.wiremock.client.MappingBuilder;
import com.github.tomakehurst.wiremock.client.ResponseDefinitionBuilder;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.stubbing.StubMapping;
import lombok.SneakyThrows;
import lv.savchuk.country.calling.codes.TestUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.equalTo;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.urlPathEqualTo;

public class WikiApiGetContentStub {

	public static StubMapping registerStub() {
		return WireMock.stubFor(request().willReturn(response()));
	}

	private static MappingBuilder request() {
		return get(urlPathEqualTo("/"))
				.withQueryParam("page", equalTo("List_of_country_calling_codes"))
				.withQueryParam("section", equalTo("11"))
				.withQueryParam("action", equalTo("parse"))
				.withQueryParam("format", equalTo("json"))
				.withQueryParam("prop", equalTo("text"));
	}

	@SneakyThrows
	private static ResponseDefinitionBuilder response() {
		final String jsonResponse = TestUtils.readFile("wiki/json/e2e/wiki-content-response.json");
		return aResponse()
				.withStatus(HttpStatus.OK.value())
				.withHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
				.withBody(jsonResponse);
	}

}