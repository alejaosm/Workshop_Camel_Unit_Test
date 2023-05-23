package co.com.bancolombia.routes;

import co.com.bancolombia.domain.usecases.processors.PreapprovalReqProcess;
import co.com.bancolombia.domain.usecases.routes.PreapprovalRoute;
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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.util.ReflectionTestUtils;

import java.nio.file.Files;
import java.nio.file.Paths;

class PreapprovalRouteTest extends CamelTestSupport {

    @Autowired
    private PreapprovalReqProcess preapprovalReqProcess;

    RestProperties restProperties;

    String jsonRequest;

    String jsonResponse200;
    String jsonResponse400;
    String jsonResponseCardHolder;

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {

        jsonRequest = Files.readString(Paths.get("src/test/resources/preapproval/request.json"));
        jsonResponse200 = Files.readString(Paths.get("src/test/resources/preapproval/response_200.json"));

        jsonResponse400 = Files.readString(Paths.get("src/test/resources/preapproval/response_400.json"));
        jsonResponseCardHolder = Files.readString(Paths.get("src/test/resources/card/response.json"));

        PreapprovalRoute preapprovalRoute = new PreapprovalRoute();

        preapprovalReqProcess = new PreapprovalReqProcess();
        restProperties = new RestProperties();

        restProperties.setRetrieveApproval("http://localhost:8089/v1/API-ext");

        ReflectionTestUtils.setField(preapprovalRoute, "preapprovalReqProcess", preapprovalReqProcess);
        ReflectionTestUtils.setField(preapprovalRoute, "restProperties", restProperties);

        return preapprovalRoute;
    }

    @Override
    public boolean isUseAdviceWith() {
        return true;
    }

    @BeforeEach
    public void setupTestRoute() throws Exception {

        RouteDefinition route = context.getRouteDefinition("preapproval-route-id");
        AdviceWith.adviceWith(route, context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                replaceFromWith("direct:test-preapproval");
                weaveById("process-req-id").replace().to("mock:mock-process-req-id");
                weaveById("preapproval-consume-id").replace().to("mock:mock-preapproval-consume-id");
            }
        });
    }


    @Test
    void test200() throws Exception {

        context.start();

        MockEndpoint mockProcessPreapprovalReq = getMockEndpoint("mock:mock-process-req-id");
        mockProcessPreapprovalReq.whenAnyExchangeReceived(preapprovalReqProcess);
        mockProcessPreapprovalReq.expectedMessageCount(1);

        MockEndpoint mockPreapprovalAPI = getMockEndpoint("mock:mock-preapproval-consume-id");
        mockPreapprovalAPI.expectedBodiesReceived(jsonRequest);

        mockPreapprovalAPI.whenExchangeReceived(1, exchange -> {
            exchange.getMessage().setBody(jsonResponse200);
            exchange.getMessage().setHeader(Exchange.HTTP_RESPONSE_CODE, "200");
        });

        template.send("direct:test-preapproval", exchange -> {
            exchange.getMessage().setBody(jsonResponseCardHolder);
        });

        MockEndpoint mockServiceErrorBean = getMockEndpoint("mock:mock-service-error-bean");
        mockServiceErrorBean.expectedMessageCount(0);

        mockProcessPreapprovalReq.assertIsSatisfied();
        mockPreapprovalAPI.assertIsSatisfied();
        mockServiceErrorBean.assertIsSatisfied();

        context.stop();
    }

    @Test
    void test404() throws Exception {

        context.start();

        MockEndpoint mockProcessPreapprovalReq = getMockEndpoint("mock:mock-process-req-id");
        mockProcessPreapprovalReq.whenAnyExchangeReceived(preapprovalReqProcess);
        mockProcessPreapprovalReq.expectedMessageCount(1);

        MockEndpoint mockPreapprovalAPI = getMockEndpoint("mock:mock-preapproval-consume-id");
        mockPreapprovalAPI.expectedBodiesReceived(jsonRequest);

        mockPreapprovalAPI.whenAnyExchangeReceived(exchange -> {
            exchange.getIn().setBody(jsonResponse400);
            exchange.getIn().setHeader(Exchange.HTTP_RESPONSE_CODE, "404");
        });
        mockPreapprovalAPI.expectedMessageCount(1);

        template.send("direct:test-preapproval", exchange -> {
            exchange.getMessage().setBody(jsonResponseCardHolder);
        });

        mockProcessPreapprovalReq.assertIsSatisfied();
        mockPreapprovalAPI.assertIsSatisfied();

        context.stop();
    }
}