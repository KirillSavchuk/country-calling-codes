package lv.savchuk.country.calling.codes.model;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class CountryCallingCodes {

	@Schema(description = "Country Name", example = "Latvia")
	private final String name;

	@Schema(description = "Country Flag URL")
	private final String flagUrl;

	@Schema(description = "Country Calling Codes", example = "371, 1888")
	private final List<Integer> codes;

}