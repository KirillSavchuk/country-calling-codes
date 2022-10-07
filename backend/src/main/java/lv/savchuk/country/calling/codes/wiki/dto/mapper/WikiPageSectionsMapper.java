package lv.savchuk.country.calling.codes.wiki.dto.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

@Service
public class WikiPageSectionsMapper extends WikiPageAttributeMapper<List<WikiPage.Section>> {

	public WikiPageSectionsMapper(ObjectMapper objectMapper) {
		super(objectMapper);
	}

	@Override
	public Consumer<List<WikiPage.Section>> attributeSetter(WikiPage wikiPage) {
		return wikiPage::setSections;
	}

	@Override
	public List<WikiPage.Section> getValue(JsonNode wikiPageData) throws JsonProcessingException {
		final List<WikiPage.Section> sections = new ArrayList<>();
		final JsonNode jsonWikiSections = wikiPageData.get("sections");
		if (jsonWikiSections.isArray()) {
			for (JsonNode jsonWikiSection : jsonWikiSections) {
				sections.add(createFrom(jsonWikiSection));
			}
		}
		return sections;
	}

	private WikiPage.Section createFrom(JsonNode jsonWikiSection) throws JsonProcessingException {
		return getObjectMapper().treeToValue(jsonWikiSection, WikiPage.Section.class);
	}

}