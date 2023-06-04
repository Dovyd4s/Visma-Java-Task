package lt.vaitoska.vismatask.models;

import org.springframework.http.HttpStatus;

public class ResourseAndMessage<T> {
    private T resource;
    private String message;
    private HttpStatus httpStatus;

    public ResourseAndMessage() {
    }

    public ResourseAndMessage(T resource, String message, HttpStatus httpStatus) {
        this.resource = resource;
        this.message = message;
        this.httpStatus = httpStatus;
    }

    public T getResource() {
        return resource;
    }

    public void setResource(T resource) {
        this.resource = resource;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public void setHttpStatus(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }
}
