package co.com.bancolombia.domain.usecases.routes;

import co.com.bancolombia.infrastructure.properties.RestParams;
import co.com.bancolombia.infrastructure.properties.RestProperties;
import co.com.bancolombia.integracion.integration_adapter.httprequest.utils.Params;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class RetrieveRoute extends RouteBuilder {
    @Autowired
    private RestProperties restProperties;

    @Override
    public void configure() throws Exception {

        from("direct:retrieve").routeId("retrieve-route-id")
                .setProperty(RestParams.MESSAGE_ID, header(RestParams.MESSAGE_ID))
                .choice()
                    .when().simple("${body.data.card.cardType} == 'C'")
                        .to("direct:transformation-credit").id("transformation-request-credit-id")
                        .removeHeaders("*")
                        .setProperty(Params.URI, () -> "netty-http:"+restProperties.getUriCreditCard())
                        .setBody(simple("${body}"))
                    .when().simple("${body.data.card.cardType} == 'D'")
                        .to("direct:transformation-debit").id("transformation-request-debit-id")
                        .removeHeaders("*")
                        .setProperty(Params.URI, () -> "netty-http:"+restProperties.getUriDebitCard())
                        .setBody(simple("${body}"))
                .end()
                .to("direct:generic-consume-apic").id("generic-consume-apic-id")
                .to("direct:validation-errors").id("validation-errors-id");

        from("direct:validation-errors").routeId("validation-errors-route-id")
                .choice()
                    .when(header(Exchange.HTTP_RESPONSE_CODE).isEqualTo("200"))
                        .to("direct:preapproval").id("preapproval-id")
                    .when(simple("${headers.CamelHttpResponseCode} in '400,401,403,404,409,500,502,503,504'"))
                        .log("[ERROR] ${headers.CamelHttpResponseCode} ***** [Body] ${body}")
                        .convertBodyTo(String.class)
                .end();
    }
}