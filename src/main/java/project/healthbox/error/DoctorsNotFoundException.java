package project.healthbox.error;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.NOT_FOUND, reason = "Doctors not found!")
public class DoctorsNotFoundException extends RuntimeException {

    private int statusCode;

    public DoctorsNotFoundException() {
        this.statusCode = 404;
    }

    public DoctorsNotFoundException(String message) {
        super(message);
        this.statusCode = 404;
    }

    public int getStatusCode() {
        return statusCode;
    }
}
