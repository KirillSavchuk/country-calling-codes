package lv.savchuk.country.calling.codes.wiki.service;

import lombok.SneakyThrows;
import lv.savchuk.country.calling.codes.TestUtils;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;


class WikiCountryCallingCodesParserTest {

	private WikiCountryCallingCodesParser parser;

	@BeforeEach
	void setUp() {
		this.parser = new WikiCountryCallingCodesParser();
	}

	@Test
	void parse_noHtmlDocument() {
		final WikiPage wikiPage = new WikiPage();
		final WikiPage.Content wikiContent = new WikiPage.Content();
		wikiContent.setOriginalText("NoValidHtml");
		wikiPage.setContent(wikiContent);

		final List<CountryCallingCodes> countryCallingCodesList = parser.parse(wikiPage);

		assertThat(countryCallingCodesList).isEmpty();
	}

	@Test
	void parse_noTableInHtml() {
		final WikiPage wikiPage = createFromFile("html-no-table.html");

		final List<CountryCallingCodes> countryCallingCodesList = parser.parse(wikiPage);

		assertThat(countryCallingCodesList).isEmpty();
	}

	@ParameterizedTest(name = "[{index}] {0}")
	@MethodSource("parse_success_data")
	void parse_success(String pathToFile, CountryCallingCodes expectedData) {
		final WikiPage wikiPage = createFromFile(pathToFile);

		final List<CountryCallingCodes> countryCallingCodesList = parser.parse(wikiPage);

		assertThat(countryCallingCodesList).isNotEmpty();
		assertThat(countryCallingCodesList).hasSize(1);

		final CountryCallingCodes countryCallingCodes = countryCallingCodesList.get(0);
		assertThat(countryCallingCodes.getName()).isEqualTo(expectedData.getName());
		assertThat(countryCallingCodes.getFlagUrl()).isEqualTo(expectedData.getFlagUrl());
		assertThat(countryCallingCodes.getCodes()).containsExactlyInAnyOrderElementsOf(expectedData.getCodes());
	}

	static Stream<Arguments> parse_success_data() {
		return Stream.of(
				Arguments.of("tr-no-flag.html", expected(
						"Globalstar (Mobile Satellite Service)",
						StringUtils.EMPTY,
						List.of(8818, 8819))),
				Arguments.of("tr-with-flag.html", expected(
						"Slovakia",
						"https://upload.wikimedia.org/wikipedia/commons/thumb/e/e6/Flag_of_Slovakia.svg/23px-Flag_of_Slovakia.svg.png",
						List.of(421))),
				Arguments.of("tr-with-notes.html", expected(
						"Kazakhstan",
						"https://upload.wikimedia.org/wikipedia/commons/thumb/d/d3/Flag_of_Kazakhstan.svg/23px-Flag_of_Kazakhstan.svg.png",
						List.of(76, 77))),
				Arguments.of("tr-with-text.html", expected(
						"Vatican City State (Holy See)",
						"https://upload.wikimedia.org/wikipedia/commons/thumb/0/00/Flag_of_the_Vatican_City.svg/16px-Flag_of_the_Vatican_City.svg.png",
						List.of(3906698, 379)))
		);
	}

	private static CountryCallingCodes expected(String countryName, String countryFlagUrl, List<Integer> countryCallingCodes) {
		return CountryCallingCodes.builder()
				.name(countryName)
				.flagUrl(countryFlagUrl)
				.codes(countryCallingCodes)
				.build();
	}

	@SneakyThrows
	private WikiPage createFromFile(String fileName) {
		final String pathToFile = "wiki/html/" + fileName;
		final String text = TestUtils.readFile(pathToFile, getClass());
		final Document html = Jsoup.parse(text);

		final WikiPage wikiPage = new WikiPage();
		final WikiPage.Content wikiContent = new WikiPage.Content();
		wikiContent.setHtml(html);
		wikiContent.setOriginalText(text);
		wikiPage.setContent(wikiContent);
		return wikiPage;
	}

}