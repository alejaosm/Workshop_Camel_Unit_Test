package co.com.bancolombia.domain.usecases.mappers;

import co.com.bancolombia.integracion.status.HttpStatus;

public enum CodeExceptions {
    ERROR_BAD_REQUEST(4_000_010, "Bad Request", HttpStatus.BAD_REQUEST.name()),
    ERROR_GENERIC(500, "Internal Server Error", HttpStatus.INTERNAL_SERVER_ERROR.name()),

    NOT_EMPTY(4_000_003, "Verifique su request, falta campo obligatorio", HttpStatus.BAD_REQUEST.name()),
    NOT_NULL(4_000_003, "Verifique su request, falta campo obligatorio", HttpStatus.BAD_REQUEST.name());

    private final int code;
    private final String reason;
    private final String httpCode;

    private CodeExceptions(int statusCode, String reasonPhrase,String httpCode) {
        this.code = statusCode;
        this.reason = reasonPhrase;
        this.httpCode = httpCode;
    }

    public int getStatusCode() {
        return this.code;
    }

    public String getReasonPhrase() {
        return this.reason;
    }

    public String getHttpCode(){
        return this.httpCode;
    }
}
