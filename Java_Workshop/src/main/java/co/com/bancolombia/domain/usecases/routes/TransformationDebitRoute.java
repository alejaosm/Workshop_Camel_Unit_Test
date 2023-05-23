package co.com.bancolombia.domain.usecases.routes;

import co.com.bancolombia.domain.usecases.beans.DebitCardBean;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class TransformationDebitRoute extends RouteBuilder {
    @Autowired
    private DebitCardBean debitCardBean;

    @Override
    public void configure() throws Exception {
        from("direct:transformation-debit").routeId("transformation-debit-route-id")
                .bean(debitCardBean, "transformationDebitRequest(${body})").id("debit-card-bean-id")
                .marshal().json()
                .end();
    }
}
