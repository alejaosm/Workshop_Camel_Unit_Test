package co.com.bancolombia.routes;

import co.com.bancolombia.domain.usecases.routes.GenericApiCRoute;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Paths;

class GenericApiCRouteTest extends CamelTestSupport {

    String jsonRequest;
    String jsonResponse;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {

        jsonRequest = Files.readString(Paths.get("src/test/resources/card/request.json"));
        jsonResponse = Files.readString(Paths.get("src/test/resources/card/response.json"));

        return new GenericApiCRoute();
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @BeforeEach
    void setupTestRoute() throws Exception {
        RouteDefinition route = context.getRouteDefinition("generic-consume-apic-route-id");
        AdviceWith.adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure()  {
                replaceFromWith("direct:test-generic-consume-apic");
                weaveById("consume-apic-id").replace().to("mock:mock-consume-apic");
                weaveById("error-bean-id").remove();
                weaveById("error-bean-exception-id").remove();
            }
        });
    }

    @Test
    void testConsumeApiCSuccess()  {

        context.start();

        MockEndpoint mockConsumeApiC = getMockEndpoint("mock:mock-consume-apic");

        mockConsumeApiC.expectedMessageCount(1);
        mockConsumeApiC.whenAnyExchangeReceived(exchange -> exchange.getMessage().setBody(jsonResponse));

        template.send("direct:test-generic-consume-apic", exchange -> exchange.getMessage().setBody(jsonRequest));

        context.stop();
    }
}