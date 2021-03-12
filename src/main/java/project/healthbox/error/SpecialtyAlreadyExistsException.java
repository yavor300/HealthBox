package project.healthbox.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT, reason = "Specialty already exists!")
public class SpecialtyAlreadyExistsException extends RuntimeException {
    public SpecialtyAlreadyExistsException(String message) {
        super(message);
    }
}
