package lv.savchuk.country.calling.codes.wiki.dto.mapper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;
import org.springframework.stereotype.Service;

import java.util.function.Consumer;

@Service
public class WikiPageIdMapper extends WikiPageAttributeMapper<Integer> {

	public WikiPageIdMapper(ObjectMapper objectMapper) {
		super(objectMapper);
	}

	@Override
	public Consumer<Integer> attributeSetter(WikiPage wikiPage) {
		return wikiPage::setId;
	}

	@Override
	public Integer getValue(JsonNode wikiPageData) {
		return wikiPageData.get("pageid").intValue();
	}

}