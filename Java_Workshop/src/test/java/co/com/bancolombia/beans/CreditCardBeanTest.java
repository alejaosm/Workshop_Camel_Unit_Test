package co.com.bancolombia.beans;

import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReq;
import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReqData;
import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReqDataCard;
import co.com.bancolombia.domain.entities.model.dtos.in.retrievecardholderinformationcredit.RetrieveCardHolderCreditReq;
import co.com.bancolombia.domain.usecases.beans.CreditCardBean;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CreditCardBeanTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception{

        CreditCardBean creditCardBean = new CreditCardBean();

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("direct:testCreditCardBean")
                        .log("CreditCardBean inicial {$body}")
                        .bean(creditCardBean, "transformationCreditRequest(*)")
                        .log("CreditCardBean final {$body}")
                        .to("mock:mockResultRequest");
            }
        };
    }
    @Test
    public void testCreditCardBean (){
        template.send("direct:testCreditCardBean", exchange -> {
            exchange.getIn().setBody(createTransformationCreditRequest());
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:mockResultRequest");
        Exchange exchange = mockEndpoint.getExchanges().stream().findFirst().get();

        RetrieveCardHolderCreditReq retrieveCardHolderCreditReq = exchange.getIn().getBody(RetrieveCardHolderCreditReq.class);

        Assertions.assertEquals(retrieveCardHolderCreditReq.getData().getUser().getId(), "SVP");
        Assertions.assertEquals(retrieveCardHolderCreditReq.getData().getCustomerCard().getNumber(), "0000377813384145240");
    }

    public RetrieveReq createTransformationCreditRequest() {
        return RetrieveReq.builder()
                .data(RetrieveReqData.builder()
                        .card(RetrieveReqDataCard.builder()
                                .id("SVP")
                                .number("0000377813384145240")
                                .build())
                        .build())
                .build();
    }
}
