
package lv.savchuk.country.calling.codes.wiki.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.savchuk.country.calling.codes.api.CountryCallingCodesProvider;
import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;
import lv.savchuk.country.calling.codes.wiki.service.WikiApiService;
import lv.savchuk.country.calling.codes.wiki.service.WikiCountryCallingCodesParser;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class WikiCountryCallingCodesProvider implements CountryCallingCodesProvider {

	static final String WIKI_PAGE_TITLE = "List_of_country_calling_codes";
	static final String WIKI_SECTION_TITLE = "Alphabetical listing by country or region";

	private final WikiApiService apiService;
	private final WikiCountryCallingCodesParser contentParser;

	public List<CountryCallingCodes> collectCountryData() {
		final WikiPage wikiPageWithSections = apiService.getPageSection(WIKI_PAGE_TITLE);
		final Optional<String> sectionId = wikiPageWithSections.getSections().stream()
				.filter(section -> WIKI_SECTION_TITLE.equals(section.getTitle()))
				.map(WikiPage.Section::getIndex)
				.findFirst();
		if (sectionId.isEmpty()) {
			log.error("ERROR");
		}
		final WikiPage wikiCountryCallingCodesPage = apiService.getPageContent(WIKI_PAGE_TITLE, sectionId.get());
		return contentParser.parse(wikiCountryCallingCodesPage);
	}

}