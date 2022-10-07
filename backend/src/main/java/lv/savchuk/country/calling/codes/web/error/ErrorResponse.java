package lv.savchuk.country.calling.codes.web.error;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
public class ErrorResponse {

	@Setter
	private String errorMessage;
	private final List<InputViolation> violations;

	public ErrorResponse(List<InputViolation> violations) {
		this.violations = violations;
	}

	public ErrorResponse(String errorMessage) {
		this(new ArrayList<>());
		this.errorMessage = errorMessage;
	}

	public void add(InputViolation violation) {
		violations.add(violation);
	}

}