package lv.savchuk.country.calling.codes.wiki.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import lv.savchuk.country.calling.codes.wiki.dto.mapper.WikiPageAttributeMapper;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.util.List;


public class WikiPageDeserializer extends StdDeserializer<WikiPage> {

	private List<WikiPageAttributeMapper<?>> attributeMappers;

	public WikiPageDeserializer(Class<?> vc) {
		super(vc);
	}

	@Autowired
	public WikiPageDeserializer(List<WikiPageAttributeMapper<?>> attributeMappers) {
		this((Class<?>) null);
		this.attributeMappers = attributeMappers;
	}

	@Override
	public WikiPage deserialize(JsonParser jp, DeserializationContext ctx) throws IOException {
		final JsonNode json = jp.getCodec().readTree(jp);
		final JsonNode jsonWikiPageData = json.get("parse");
		final WikiPage wikiPage = new WikiPage();
		attributeMappers.forEach(mapper -> mapper.map(wikiPage, jsonWikiPageData));
		return wikiPage;
	}

}