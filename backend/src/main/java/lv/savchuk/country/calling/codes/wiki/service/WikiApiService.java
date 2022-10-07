package lv.savchuk.country.calling.codes.wiki.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.savchuk.country.calling.codes.wiki.client.WikiApiClient;
import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class WikiApiService {

	private final WikiApiClient apiClient;

	public WikiPage getPageSection(String title) {
		return apiClient.getPage(title, null, "parse", "json", "sections");
	}

	public WikiPage getPageContent(String title, String sectionIndex) {
		return apiClient.getPage(title, sectionIndex, "parse", "json", "text");
	}

}