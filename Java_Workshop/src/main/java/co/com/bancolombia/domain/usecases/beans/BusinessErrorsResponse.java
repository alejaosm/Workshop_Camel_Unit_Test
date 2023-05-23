package co.com.bancolombia.domain.usecases.beans;

import co.com.bancolombia.domain.usecases.mappers.CodeExceptions;
import co.com.bancolombia.infrastructure.properties.RestParams;
import co.com.bancolombia.integracion.camel.processors.http.transform.model.Error;
import co.com.bancolombia.integracion.camel.processors.http.transform.model.ErrorMessage;
import co.com.bancolombia.integracion.camel.processors.http.transform.model.Meta;
import co.com.bancolombia.integracion.status.HttpStatus;
import org.apache.camel.Exchange;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.UUID;
import java.util.ArrayList;

@Component
public class BusinessErrorsResponse {

    public void createBadResponse(Exchange exchange, String typeException) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Collection<Object> values =
            Arrays.asList(exchange.getMessage().getHeaders().keySet().toArray());
        String messageId = values.contains(RestParams.MESSAGE_ID)
            ? exchange.getMessage().getHeader(RestParams.MESSAGE_ID).toString()
            : UUID.randomUUID().toString();
        CodeExceptions codeExceptions = CodeExceptions.valueOf(typeException);
        HttpStatus httpCode = HttpStatus.valueOf(String.valueOf(codeExceptions.getHttpCode()));
        ErrorMessage errorMessage = getErrorMessage(messageId, timestamp, httpCode, codeExceptions);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, httpCode.getStatusCode());
        exchange.getIn().setBody(errorMessage);
    }

    public void createBadResponse(Exchange exchange) {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        Collection<Object> values =
            Arrays.asList(exchange.getMessage().getHeaders().keySet().toArray());
        String exception = null != exchange.getProperty(Exchange.EXCEPTION_CAUGHT)
            ? exchange.getProperty(Exchange.EXCEPTION_CAUGHT).toString()
            : "";
        String descriptionError = "";
        if (null != exception && !"".equals(exception)) {
            final BeanValidationException ex =
                exchange.getProperty(Exchange.EXCEPTION_CAUGHT, BeanValidationException.class);
            descriptionError = ex.getConstraintViolations().iterator().next().getMessageTemplate();
        }
        String messageId = values.contains(RestParams.MESSAGE_ID)
            ? exchange.getMessage().getHeader(RestParams.MESSAGE_ID).toString()
            : UUID.randomUUID().toString();
        CodeExceptions codeExceptions = !"".equals(descriptionError)?
            CodeExceptions.valueOf(descriptionError)
            :CodeExceptions.valueOf(CodeExceptions.ERROR_BAD_REQUEST.toString());
        HttpStatus httpCode = HttpStatus.valueOf(codeExceptions.getHttpCode());
        ErrorMessage errorMessage = getErrorMessage(messageId, timestamp, httpCode, codeExceptions);
        exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, httpCode.getStatusCode());
        exchange.getIn().setBody(errorMessage);
    }

    private ErrorMessage getErrorMessage(String messageId, Timestamp timestamp,
                                         HttpStatus httpCode, CodeExceptions codeExceptions) {
        final SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
        Meta meta = new Meta();
        meta.setMessageId(messageId);
        meta.setApplicationId(RestParams.SERVICE_NAME);
        meta.setRequestDateTime(sdf2.format(timestamp));
        ErrorMessage errorMessage = new ErrorMessage();
        List<Error> errorList = new ArrayList<>();
            Error error = new Error();
            error.setCode(String.valueOf(httpCode.getStatusCode()));
            error.setDetail(httpCode.getReasonPhrase());
            errorList.add(error);
            errorMessage.setErrors(errorList);
            errorMessage.setMeta(meta);
            errorMessage.setStatus(String.valueOf(codeExceptions.getStatusCode()));
            errorMessage.setTitle(codeExceptions.getReasonPhrase());
            return errorMessage;
    }
}