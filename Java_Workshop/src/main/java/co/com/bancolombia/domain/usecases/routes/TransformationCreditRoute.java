package co.com.bancolombia.domain.usecases.routes;

import co.com.bancolombia.domain.usecases.beans.CreditCardBean;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransformationCreditRoute extends RouteBuilder {
    @Autowired
    private CreditCardBean creditCardBean;

    @Override
    public void configure() throws Exception {
        from("direct:transformation-credit").routeId("transformation-credit-route-id")
                .bean(creditCardBean, "transformationCreditRequest(${body})").id("credit-card-bean-id")
                .marshal().json()
                .end();
    }
}