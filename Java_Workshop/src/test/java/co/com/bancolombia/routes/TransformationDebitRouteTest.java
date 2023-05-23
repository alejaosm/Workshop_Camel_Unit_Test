package co.com.bancolombia.routes;

import co.com.bancolombia.domain.usecases.beans.DebitCardBean;
import co.com.bancolombia.domain.usecases.routes.TransformationDebitRoute;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TransformationDebitRouteTest extends CamelTestSupport {

    @Override
    protected RouteBuilder createRouteBuilder() {
        TransformationDebitRoute transformationDebitRoute = new TransformationDebitRoute();
        return transformationDebitRoute;
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @BeforeEach
    void setupTestRoute() throws Exception {
        RouteDefinition route1 = context.getRouteDefinition("transformation-debit-route-id");
        AdviceWith.adviceWith(route1, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                replaceFromWith("direct:test-transformation-debit");
                weaveById("debit-card-bean-id").replace().to("mock:mock-debit-card-bean-id");
            }
        });
    }

    @Test
    void testConsumeApiCSuccess() throws Exception {
        context.start();
        DebitCardBean debitCardBean = new DebitCardBean();
        MockEndpoint mockDebitCardBean = getMockEndpoint("mock:mock-debit-card-bean-id");

        mockDebitCardBean.expectedHeaderReceived("message-id", "123455");
        mockDebitCardBean.expectedBodiesReceived(debitCardBean);

        template.send("direct:test-transformation-debit", exchange -> {
            exchange.getIn().setHeader("message-id", "123455");
            exchange.getIn().setBody(debitCardBean);
        });

        mockDebitCardBean.assertIsSatisfied();

        context.stop();
    }
}