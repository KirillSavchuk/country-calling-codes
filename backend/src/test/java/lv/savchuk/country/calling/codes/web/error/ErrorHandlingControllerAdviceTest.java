package lv.savchuk.country.calling.codes.web.error;

import lv.savchuk.country.calling.codes.exception.CountryCallingCodeNotFoundException;
import lv.savchuk.country.calling.codes.exception.CountryCallingCodesNotInitializedException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ErrorHandlingControllerAdviceTest {

	private ErrorHandlingControllerAdvice advice;

	@BeforeEach
	void setUp() {
		this.advice = new ErrorHandlingControllerAdvice();
	}

	@Test
	void onCountryCallingCodeNotFoundException() {
		final CountryCallingCodeNotFoundException ex = new CountryCallingCodeNotFoundException("phoneNumber");

		final ErrorResponse errorResponse = advice.onCountryCallingCodeNotFoundException(ex);

		assertThat(errorResponse.getErrorMessage()).isEqualTo("Failed to find Country Calling Code for provided 'phoneNumber' phone number.");
		assertThat(errorResponse.getViolations()).isEmpty();
	}

	@Test
	void onCountryCallingCodesNotInitializedException() {
		final CountryCallingCodesNotInitializedException ex = new CountryCallingCodesNotInitializedException();

		final ErrorResponse errorResponse = advice.onCountryCallingCodesNotInitializedException(ex);

		assertThat(errorResponse.getErrorMessage()).isEqualTo("Country Calling Codes database is not initialized. Please retry later or hire a new developer!");
		assertThat(errorResponse.getViolations()).isEmpty();
	}

	@Test
	@SuppressWarnings("unchecked")
	void onConstraintValidationException() {
		final String violatedProperty = "phoneNumber";
		final String violationMessage = "Very bad phone number!";

		final ConstraintViolation<String> violation = mock(ConstraintViolation.class);
		final Path path = mock(Path.class);
		when(violation.getPropertyPath()).thenReturn(path);
		when(path.toString()).thenReturn(violatedProperty);
		when(violation.getMessage()).thenReturn(violationMessage);
		final ConstraintViolationException ex = new ConstraintViolationException(Set.of(violation));

		final ErrorResponse errorResponse = advice.onConstraintValidationException(ex);

		assertThat(errorResponse.getErrorMessage()).isNull();
		assertThat(errorResponse.getViolations()).hasSize(1);
		assertThat(errorResponse.getViolations().get(0).getFieldName()).isEqualTo(violatedProperty);
		assertThat(errorResponse.getViolations().get(0).getMessage()).isEqualTo(violationMessage);
	}

}