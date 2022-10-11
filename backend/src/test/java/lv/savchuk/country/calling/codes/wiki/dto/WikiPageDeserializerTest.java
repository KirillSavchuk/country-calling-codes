package lv.savchuk.country.calling.codes.wiki.dto;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lv.savchuk.country.calling.codes.TestUtils;
import lv.savchuk.country.calling.codes.wiki.dto.mapper.WikiPageContentMapper;
import lv.savchuk.country.calling.codes.wiki.dto.mapper.WikiPageIdMapper;
import lv.savchuk.country.calling.codes.wiki.dto.mapper.WikiPageSectionsMapper;
import lv.savchuk.country.calling.codes.wiki.dto.mapper.WikiPageTitleMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import java.io.IOException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WikiPageDeserializerTest {

	private WikiPageDeserializer deserializer;

	private JsonParser jsonParser;
	private ObjectCodec objectCodec;
	private ObjectMapper objectMapper;

	@BeforeEach
	void setUp() {
		this.objectMapper = new ObjectMapper();
		this.deserializer = new WikiPageDeserializer(List.of(
				new WikiPageContentMapper(objectMapper),
				new WikiPageIdMapper(objectMapper),
				new WikiPageSectionsMapper(objectMapper),
				new WikiPageTitleMapper(objectMapper)
		));

		this.jsonParser = mock(JsonParser.class);
		this.objectCodec = mock(ObjectCodec.class);
		when(jsonParser.getCodec()).thenReturn(objectCodec);
	}

	@Test
	void deserialize() throws IOException {
		final String jsonResponse = TestUtils.readFile("wiki/json/deserializer/wiki-content-response.json");
		final JsonNode jsonNode = objectMapper.readTree(jsonResponse);
		when(objectCodec.readTree(jsonParser)).thenReturn(jsonNode);

		final WikiPage wikiPage = deserializer.deserialize(jsonParser, null);

		assertThat(wikiPage.getId()).isEqualTo(12345);
		assertThat(wikiPage.getTitle()).isEqualTo("Wiki Title");
		assertThat(wikiPage.getSections()).isNull();
		assertThat(wikiPage.getContent()).isNotNull();
		assertThat(wikiPage.getContent().getOriginalText()).isEqualTo("<h1>HTML</h1>");
		assertThat(wikiPage.getContent().getHtml()).isNotNull();
		assertThat(wikiPage.getContent().getHtml().body().select("h1").text()).isEqualTo("HTML");
	}

}