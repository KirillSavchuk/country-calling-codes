package lv.savchuk.country.calling.codes.web.error;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

	private String errorMessage;
	private List<InputViolation> violations;

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