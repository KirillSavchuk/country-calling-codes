package lv.savchuk.country.calling.codes.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

	@Bean
	public OpenAPI springShopOpenAPI() {
		return new OpenAPI().info(getInfo());
	}

	private Info getInfo() {
		return new Info()
				.title("Country Calling Codes Service")
				.description("Country Calling Codes Service")
				.version("v1")
				.contact(getContact());
	}

	private Contact getContact() {
		return new Contact()
				.name("Kirill Savchuk")
				.email("kirillss1998@gmail.com")
				.url("https://savchuk.id.lv");
	}

}