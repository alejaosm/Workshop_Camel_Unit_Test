package co.com.bancolombia.infrastructure.adapters.driver;

import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReq;
import co.com.bancolombia.domain.usecases.beans.BusinessErrorsResponse;
import co.com.bancolombia.infrastructure.properties.RestParams;
import co.com.bancolombia.infrastructure.properties.RestProperties;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.bean.validator.BeanValidationException;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * A REST Adapter in Apache Camel using HTTP-Netty.
 */

@Component
public class RestAdapter extends RouteBuilder {

    @Autowired
    private BusinessErrorsResponse badResponseBusiness;
    @Autowired
    private RestProperties restProperties;

    public void configure() throws Exception {

        onException(BeanValidationException.class)
                .handled(true)
                .bean(badResponseBusiness, "createBadResponse(*)").id("beanBadResponseBusiness1")
                .marshal().json()
                .end();

        restConfiguration()
                .component(restProperties.getComponent())
                .endpointProperty("ssl", "true")
                .bindingMode(RestBindingMode.off)
                .enableCORS(restProperties.isEnableCORS())
                .clientRequestValidation(restProperties.isClientRequestValidation())
                .host(restProperties.getHost())
                .port(restProperties.getPort())
                .apiProperty("api.title", restProperties.getApiTitle())
                .apiProperty("api.version", restProperties.getApiVersion());

        rest(RestParams.BASE_PATH)
                .post(RestParams.SERVICE_PATH)
                .consumes("application/json")
                .produces("application/json")
                .description(RestParams.SERVICE_DESC)
                .type(RetrieveReq.class)
                .param()
                    .name(RestParams.MESSAGE_ID)
                    .type(RestParamType.header)
                    .required(true)
                .endParam()
                .param()
                    .name(RestParams.ACCEPT)
                    .type(RestParamType.header)
                    .dataType("string")
                    .required(false)
                .endParam()
                .param()
                    .name("requestBody")
                    .type(RestParamType.body)
                    .required(true)
                .endParam()
                .to("direct:route-rest-adapter");

        from("direct:route-rest-adapter")
                .unmarshal(new JacksonDataFormat(RetrieveReq.class))
                .to("bean-validator://x")
                .to("direct:retrieve");


    }
}