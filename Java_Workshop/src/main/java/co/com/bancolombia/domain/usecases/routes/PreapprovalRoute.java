package co.com.bancolombia.domain.usecases.routes;

import co.com.bancolombia.domain.entities.model.dtos.out.retrievecardholderinformationcredit.RetrieveCardHolderCreditRes;
import co.com.bancolombia.domain.usecases.processors.PreapprovalReqProcess;
import co.com.bancolombia.infrastructure.properties.RestProperties;
import co.com.bancolombia.infrastructure.utils.Util;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.JacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PreapprovalRoute extends RouteBuilder {

    @Autowired
    private RestProperties restProperties;

    @Autowired
    private PreapprovalReqProcess preapprovalReqProcess;

    @Override
    public void configure() throws Exception {

        from("direct:preapproval").routeId("preapproval-route-id")
                .convertBodyTo(String.class)
                .unmarshal(new JacksonDataFormat(RetrieveCardHolderCreditRes.class))
                .setProperty("type", simple("${body.data.customer.identification.documentType}"))
                .setProperty("number", simple("${body.data.customer.identification.documentNumber}"))
                .process(preapprovalReqProcess).id("process-req-id")
                .marshal().json()
                .removeHeaders("Camel*")
                .log("Before consumption Experience API ----- ${body}")
                .toD("netty-http:".concat(restProperties.getRetrieveApproval()
                        .concat(Util.uriParameters().toString()))).id("preapproval-consume-id")
                .choice()
                    .when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo("200"))
                        .convertBodyTo(String.class)
                    .when(simple("${headers.CamelHttpResponseCode} in '400,401,403,404,409,500,502,504'"))
                        .log("[ERROR] API Opportunities By Customer -- ${headers.CamelHttpResponseCode}")
                        .convertBodyTo(String.class)
                .end();
    }
}