
package lv.savchuk.country.calling.codes.wiki.dto.mapper;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;

import java.util.function.Consumer;

@Slf4j
@RequiredArgsConstructor
public abstract class WikiPageAttributeMapper<V> {

	@Getter
	private final ObjectMapper objectMapper;

	public void map(WikiPage wikiPage, JsonNode jsonWikiPageData) {
		try {
			attributeSetter(wikiPage).accept(getValue(jsonWikiPageData));
		} catch (JsonProcessingException ex) {
			log.error("Failed to set WikiPage attribute.", ex);
		} catch (NullPointerException ex) {
			// Ignore as field was not found in JsonNode
		}
	}

	protected abstract Consumer<V> attributeSetter(WikiPage wikiPage);

	protected abstract V getValue(JsonNode wikiPageData) throws JsonProcessingException;

}