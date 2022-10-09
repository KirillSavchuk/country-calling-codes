package lv.savchuk.country.calling.codes.wiki.impl;

import lv.savchuk.country.calling.codes.model.CountryCallingCodes;
import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;
import lv.savchuk.country.calling.codes.wiki.service.WikiApiService;
import lv.savchuk.country.calling.codes.wiki.service.WikiCountryCallingCodesParser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WikiCountryCallingCodesProviderTest {

	private WikiCountryCallingCodesProvider provider;

	private WikiApiService apiService;
	private WikiCountryCallingCodesParser contentParser;

	private static final String WIKI_SECTION_ID = "3";

	private static final String COUNTRY_NAME = "Latvia";

	@BeforeEach
	void setUp() {
		this.apiService = mock(WikiApiService.class);
		this.contentParser = mock(WikiCountryCallingCodesParser.class);
		this.provider = new WikiCountryCallingCodesProvider(apiService, contentParser);

		final WikiPage sectionsWikiPage = createSectionsWikiPage();
		when(apiService.getPageSection(WikiCountryCallingCodesProvider.WIKI_PAGE_TITLE)).thenReturn(sectionsWikiPage);

		final WikiPage contentWikiPage = createContentWikiPage();
		when(apiService.getPageContent(WikiCountryCallingCodesProvider.WIKI_PAGE_TITLE, WIKI_SECTION_ID)).thenReturn(contentWikiPage);

		final CountryCallingCodes countryCallingCodes = CountryCallingCodes.builder().name(COUNTRY_NAME).build();
		when(contentParser.parse(contentWikiPage)).thenReturn(List.of(countryCallingCodes));
	}

	@Test
	void collectCountryData_success() {
		final List<CountryCallingCodes> countryCallingCodes = provider.collectCountryData();

		assertThat(countryCallingCodes).hasSize(1);
		assertThat(countryCallingCodes.get(0).getName()).isEqualTo(COUNTRY_NAME);
	}

	private WikiPage createSectionsWikiPage() {
		final WikiPage wikiPage = createDefaultWikiPage();
		wikiPage.setSections(List.of(
				createWikiPageSection("1", "Very Boring Section"),
				createWikiPageSection("2", "Do you realy code review this?"),
				createWikiPageSection(WIKI_SECTION_ID, WikiCountryCallingCodesProvider.WIKI_SECTION_TITLE),
				createWikiPageSection("4", "Writing Unit tests is super boring")
		));
		return wikiPage;
	}

	private WikiPage createContentWikiPage() {
		final WikiPage wikiPage = createDefaultWikiPage();
		wikiPage.setContent(createWikiPageContent());
		return wikiPage;
	}

	private WikiPage.Section createWikiPageSection(String index, String title) {
		final WikiPage.Section section = new WikiPage.Section();
		section.setIndex(index);
		section.setTitle(title);
		return section;
	}

	private WikiPage.Content createWikiPageContent() {
		final WikiPage.Content content = new WikiPage.Content();
		content.setOriginalText("Text");
		return content;
	}

	private WikiPage createDefaultWikiPage() {
		final WikiPage wikiPage = new WikiPage();
		wikiPage.setId(123);
		wikiPage.setTitle(WikiCountryCallingCodesProvider.WIKI_PAGE_TITLE);
		return wikiPage;
	}

}