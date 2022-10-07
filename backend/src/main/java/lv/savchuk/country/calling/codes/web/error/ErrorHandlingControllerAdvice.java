package lv.savchuk.country.calling.codes.web.error;

import lv.savchuk.country.calling.codes.exception.CountryCallingCodeNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

@ControllerAdvice
public class ErrorHandlingControllerAdvice {

	@ResponseBody
	@ResponseStatus(HttpStatus.NOT_FOUND)
	@ExceptionHandler(CountryCallingCodeNotFoundException.class)
	public ErrorResponse onConstraintValidationException(CountryCallingCodeNotFoundException ex) {
		return new ErrorResponse(ex.getMessage());
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(ConstraintViolationException.class)
	public ErrorResponse onConstraintValidationException(ConstraintViolationException ex) {
		final List<InputViolation> violations = ex.getConstraintViolations().stream()
				.map(error -> new InputViolation(error.getPropertyPath().toString(), error.getMessage()))
				.collect(Collectors.toList());
		return new ErrorResponse(violations);
	}

	@ResponseBody
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ErrorResponse onMethodArgumentNotValidException(MethodArgumentNotValidException ex) {
		final List<InputViolation> violations = ex.getBindingResult().getFieldErrors().stream()
				.map(error -> new InputViolation(error.getField(), error.getDefaultMessage()))
				.collect(Collectors.toList());
		return new ErrorResponse(violations);
	}

}