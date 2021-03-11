package project.healthbox.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Specialty cannot be found!")
public class SpecialtyNotFoundException extends RuntimeException {
    public SpecialtyNotFoundException(String message) {
        super(message);
    }
}