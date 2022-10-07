package lv.savchuk.country.calling.codes.wiki.client;

import lv.savchuk.country.calling.codes.wiki.dto.WikiPage;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(
		name = "wiki-api",
		url = "${client.wiki-api.url}"
)
public interface WikiApiClient {

	@GetMapping
	WikiPage getPage(
			@RequestParam("page") String pageTitle,
			@RequestParam("section") String sectionId,
			@RequestParam("action") String action,
			@RequestParam("format") String format,
			@RequestParam("prop") String prop
	);

}