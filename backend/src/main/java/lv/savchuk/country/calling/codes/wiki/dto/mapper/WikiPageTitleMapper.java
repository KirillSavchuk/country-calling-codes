package lv.savchuk.country.calling.codes.wiki.dto.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class WikiPageTitleMapper extends WikiPageAttributeMapper<String> {

	public WikiPageTitleMapper(ObjectMapper objectMapper) {
		super(objectMapper);
	}

	@Override
	public Consumer<String> attributeSetter(WikiPage wikiPage) {
		return wikiPage::setTitle;
	}

	@Override
	public String getValue(JsonNode wikiPageData) {
		return wikiPageData.get("title").textValue();
	}

}