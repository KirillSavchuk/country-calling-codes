package lv.savchuk.country.calling.codes.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PhoneNumberValidateRequest {

	@NotEmpty(message = "Phone Number cannot be empty")
	@Pattern(regexp = "\\+?[0-9\\s]+", message = "Phone Number should match Regex: '\\+?[0-9\\s]+'")
	@Schema(required = true, description = "Phone Number", example = "+371 28845782")
	private String phoneNumber;

}