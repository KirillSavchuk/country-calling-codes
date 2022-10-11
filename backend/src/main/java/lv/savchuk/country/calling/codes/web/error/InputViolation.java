package lv.savchuk.country.calling.codes.web.error;

import lombok.Value;

@Value
public class InputViolation {

	String fieldName;
	String message;

}