package lv.savchuk.country.calling.codes.wiki.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jsoup.nodes.Document;

import java.util.List;

@Data
@NoArgsConstructor
@JsonDeserialize(using = WikiPageDeserializer.class)
public class WikiPage {

	private int id;
	private String title;
	private Content content;
	private List<Section> sections;

	@Data
	@NoArgsConstructor
	public static class Section {
		private String index;
		@JsonProperty("line")
		private String title;
	}

	@Data
	@NoArgsConstructor
	public static class Content {
		private String originalText;
		private Document html;
	}

}