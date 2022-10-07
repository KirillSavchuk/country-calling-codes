
package lv.savchuk.country.calling.codes.wiki.service;

import lombok.extern.slf4j.Slf4j;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static java.util.Objects.isNull;
import static java.util.stream.Collectors.toList;

@Slf4j
@Service
public class WikiCountryCallingCodesParser {

	public List<CountryCallingCodes> parse(WikiPage wikiPage) {
		final List<CountryCallingCodes> countryCallingCodesList = new ArrayList<>();
		final Document html = wikiPage.getContent().getHtml();
		if (isNull(html)) {
			return countryCallingCodesList;
		}
		final Elements countryTableRows = html.select("table > tbody > tr:not(:first-child)");
		if (countryTableRows.isEmpty()) {
			return countryCallingCodesList;
		}

		for (Element row : countryTableRows) {
			countryCallingCodesList.add(CountryCallingCodes.builder()
					.name(getCountryName(row))
					.flagUrl(getCountryFlagUrl(row))
					.codes(getCountryCallingCodes(row))
					.build());
		}
		return countryCallingCodesList;
	}

	private String getCountryName(Element countryTableRow) {
		return countryTableRow.select("td:nth-child(1)").text();
	}

	private String getCountryFlagUrl(Element countryTableRow) {
		final String countryFlagUrl = countryTableRow.select("td:nth-child(1) img").attr("src");
		if (StringUtils.isNotBlank(countryFlagUrl)) {
			return "https:" + countryFlagUrl;
		}
		return StringUtils.EMPTY;
	}

	private List<Integer> getCountryCallingCodes(Element countryTableRow) {
		final String rawText = countryTableRow.select("td:nth-child(2)").text();
		final String[] countryCallingCodes = removeRedundantData(rawText).split(",");
		return Stream.of(countryCallingCodes).map(Integer::valueOf).collect(toList());
	}

	private String removeRedundantData(String countryCallingCodesRawText) {
		return countryCallingCodesRawText
				.replaceAll("(\\[.+\\])", "") // '+7[notes 1]' -> '+7'
				.replaceAll("[A-Za-z\\s\\+]", "");  // '+39 06 698, assigned +379' -> '+39 06 698,assigned +379'
	}

}