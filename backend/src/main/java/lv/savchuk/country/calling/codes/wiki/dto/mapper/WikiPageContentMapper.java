package lv.savchuk.country.calling.codes.wiki.dto.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class WikiPageContentMapper extends WikiPageAttributeMapper<WikiPage.Content> {

	public WikiPageContentMapper(ObjectMapper objectMapper) {
		super(objectMapper);
	}

	@Override
	protected Consumer<WikiPage.Content> attributeSetter(WikiPage wikiPage) {
		return wikiPage::setContent;
	}

	@Override
	protected WikiPage.Content getValue(JsonNode wikiPageData) {
		final String originalText = wikiPageData.get("text").get("*").textValue();
		final var content = new WikiPage.Content();
		content.setOriginalText(originalText);
		content.setHtml(toHTML(originalText));
		return content;
	}

	private Document toHTML(String text) {
		return Jsoup.parse(text);
	}

}