package co.com.bancolombia.routes;

import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReq;
import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReqData;
import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReqDataCard;
import co.com.bancolombia.domain.usecases.routes.RetrieveRoute;
import co.com.bancolombia.infrastructure.properties.RestProperties;
import co.com.bancolombia.integracion.integration_adapter.httprequest.utils.Params;
import org.apache.camel.Exchange;
import org.apache.camel.builder.AdviceWith;
import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.model.RouteDefinition;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

class RetrieveRouteTest extends CamelTestSupport {

    String jsonRequest;
    String jsonResponse;


    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {

        jsonRequest = Files.readString(Paths.get("src/test/resources/card/request.json"));
        jsonResponse = Files.readString(Paths.get("src/test/resources/card/response.json"));

        RetrieveRoute retrieveRoute = new RetrieveRoute();
        RestProperties restProperties = new RestProperties();

        ReflectionTestUtils.setField(retrieveRoute, "restProperties", restProperties);

        return retrieveRoute;
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @BeforeEach
    public void setupTestRoute() throws Exception {
        RouteDefinition routeRetrieve = context.getRouteDefinition("retrieve-route-id");
        AdviceWith.adviceWith(routeRetrieve, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                replaceFromWith("direct:test-retrieve");
                weaveById("transformation-request-credit-id").replace().to("mock:mock-transformation-request-credit-id");
                weaveById("transformation-request-debit-id").replace().to("mock:mock-transformation-request-debit-id");
                weaveById("generic-consume-apic-id").replace().to("mock:mock-generic-consume-apic-id");
                weaveById("validation-errors-id").replace().to("mock:mock-validation-errors-id");
            }
        });

        RouteDefinition routeValidationErrors = context.getRouteDefinition("validation-errors-route-id");
        AdviceWith.adviceWith(routeValidationErrors, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                replaceFromWith("direct:test-validation-errors");
                weaveById("preapproval-id").replace().to("mock:mock-preapproval-id");
            }
        });
    }

    @Test
    void testCredit() throws Exception {
        context.start();

        MockEndpoint mockTransformationCredit = getMockEndpoint("mock:mock-transformation-request-credit-id");
        MockEndpoint mockTransformationDebit = getMockEndpoint("mock:mock-transformation-request-debit-id");
        MockEndpoint mockGenericConsumeApiC = getMockEndpoint("mock:mock-generic-consume-apic-id");
        MockEndpoint mockValidationErrors = getMockEndpoint("mock:mock-validation-errors-id");

        mockTransformationCredit.expectedHeaderReceived("message-id", "123455");
        mockTransformationCredit.whenAnyExchangeReceived(exchange -> {
            exchange.getIn().setBody(jsonRequest);
            exchange.setProperty(Params.URI, "mock:http://uri-credit");
        });

        mockTransformationCredit.expectedMessageCount(1);
        mockTransformationDebit.expectedMessageCount(0);

        mockGenericConsumeApiC.expectedMessageCount(1);
        mockGenericConsumeApiC.whenAnyExchangeReceived(exchange -> exchange.getMessage().setBody(jsonResponse));

        mockValidationErrors.expectedMessageCount(1);

        template.send("direct:test-retrieve", exchange -> {
            exchange.getMessage().setHeader(Params.MESSAGE_ID, "123455");
            exchange.getMessage().setBody(createRequestCredit());
        });

        mockTransformationCredit.assertIsSatisfied();
        mockTransformationDebit.assertIsSatisfied();
        mockGenericConsumeApiC.assertIsSatisfied();
        mockValidationErrors.assertIsSatisfied();

        context.stop();
    }

    @Test
    void testDebit() throws Exception {
        context.start();

        MockEndpoint mockTransformationCredit = getMockEndpoint("mock:mock-transformation-request-credit-id");
        MockEndpoint mockTransformationDebit = getMockEndpoint("mock:mock-transformation-request-debit-id");
        MockEndpoint mockGenericConsumeApiC = getMockEndpoint("mock:mock-generic-consume-apic-id");
        MockEndpoint mockValidationErrors = getMockEndpoint("mock:mock-validation-errors-id");

        mockTransformationDebit.expectedHeaderReceived(Params.MESSAGE_ID, "123455");
        mockTransformationDebit.whenAnyExchangeReceived(exchange -> {
            exchange.getIn().setBody(jsonRequest);
            exchange.setProperty(Params.URI, "mock:http://uri-debit");
        });

        mockTransformationCredit.expectedMessageCount(0);
        mockTransformationDebit.expectedMessageCount(1);
        mockGenericConsumeApiC.expectedMessageCount(1);

        mockGenericConsumeApiC.expectedBodiesReceived(jsonRequest);
        mockGenericConsumeApiC.whenAnyExchangeReceived(exchange -> exchange.getMessage().setBody(jsonResponse));

        mockValidationErrors.expectedMessageCount(1);

        template.send("direct:test-retrieve", exchange -> {
            exchange.getMessage().setHeader("message-id", "123455");
            exchange.getMessage().setBody(createRequestDebit());
        });

        mockTransformationCredit.assertIsSatisfied();
        mockTransformationDebit.assertIsSatisfied();
        mockGenericConsumeApiC.assertIsSatisfied();
        mockValidationErrors.assertIsSatisfied();

        context.stop();
    }

    @Test
    void testValidationErrors() throws Exception {

        context.start();

        MockEndpoint mockPreapproval = getMockEndpoint("mock:preapproval-id");
        mockPreapproval.expectedMessageCount(0);

        template.send("direct:test-validation-errors", exchange -> {
            exchange.getMessage().setHeader("message-id", "123455");
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, "400");
            exchange.getMessage().setBody(jsonResponse);
        });

        mockPreapproval.assertIsSatisfied();

        context.stop();
    }


    public RetrieveReq createRequestCredit() {

        return RetrieveReq.builder()

                .data(RetrieveReqData.builder()
                        .card(RetrieveReqDataCard.builder()
                                .id("SVP")
                                .number("0000377813384145240")
                                .cardType("C")
                                .build())
                        .build())
                .build();
    }

    public RetrieveReq createRequestDebit() {

        return RetrieveReq.builder()
                .data(RetrieveReqData.builder()
                        .card(RetrieveReqDataCard.builder()
                                .id("SVP")
                                .number("0004513070220694215")
                                .cardType("D")
                                .build())
                        .build())
                .build();
    }
}