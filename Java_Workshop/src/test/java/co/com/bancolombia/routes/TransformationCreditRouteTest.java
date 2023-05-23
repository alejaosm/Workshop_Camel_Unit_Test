package co.com.bancolombia.routes;

import co.com.bancolombia.domain.usecases.beans.CreditCardBean;
import co.com.bancolombia.domain.usecases.routes.TransformationCreditRoute;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransformationCreditRouteTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() {
        TransformationCreditRoute transformationCreditRoute = new TransformationCreditRoute();
        return transformationCreditRoute;
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @BeforeEach
    void setupTestRoute() throws Exception {

        RouteDefinition route = context.getRouteDefinition("transformation-credit-route-id");
        AdviceWith.adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                replaceFromWith("direct:test-transformation-credit");
                weaveById("credit-card-bean-id").replace().to("mock:mock-credit-card-bean-id");
            }
        });
    }

    @Test
    void testConsumeApiCSuccess() throws Exception {

        context.start();
        CreditCardBean creditCardBean = new CreditCardBean();
        MockEndpoint mockCreditCardBean = getMockEndpoint("mock:mock-credit-card-bean-id");

        mockCreditCardBean.expectedHeaderReceived("message-id", "123455");
        mockCreditCardBean.expectedBodiesReceived(creditCardBean);

        template.send("direct:test-transformation-credit", exchange -> {
            exchange.getIn().setHeader("message-id", "123455");
            exchange.getIn().setBody(creditCardBean);
        });

        mockCreditCardBean.assertIsSatisfied();

        context.stop();
    }
}