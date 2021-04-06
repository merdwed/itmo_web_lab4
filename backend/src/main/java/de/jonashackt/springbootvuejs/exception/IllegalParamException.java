package de.jonashackt.springbootvuejs.exception;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
@ResponseStatus(code = HttpStatus.BAD_REQUEST)
public class IllegalParamException extends IllegalArgumentException{
    public IllegalParamException() {
        super();
    }

    public IllegalParamException(String message) {
        super(message);
    }

    public IllegalParamException(String message, Throwable cause) {
        super(message, cause);
    }
}