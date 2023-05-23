package co.com.bancolombia.beans;

import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReq;
import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReqData;
import co.com.bancolombia.domain.entities.model.dtos.in.retrieve.RetrieveReqDataCard;
import co.com.bancolombia.domain.entities.model.dtos.in.retrievecardholderinformationdebit.RetrieveCardHolderDebitReq;
import co.com.bancolombia.domain.usecases.beans.DebitCardBean;
import org.apache.camel.Exchange;
import org.apache.camel.RoutesBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.junit5.CamelTestSupport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class DebitCardBeanTest extends CamelTestSupport {

    @Override
    protected RoutesBuilder createRouteBuilder() throws Exception{

        DebitCardBean debitCardBean = new DebitCardBean();

        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {

                from("direct:testDebitCardBean")
                        .log("DebitCardBean inicial {$body}")
                        .bean(debitCardBean, "transformationDebitRequest(*)")
                        .log("DebitCardBean final {$body}")
                        .to("mock:mockResultRequest");
            }
        };
    }

    @Test
    public void testDebitCardBean (){
        template.send("direct:testDebitCardBean", exchange -> {
            exchange.getIn().setBody(createTransformationDebitRequest());
        });

        MockEndpoint mockEndpoint = getMockEndpoint("mock:mockResultRequest");
        Exchange exchange = mockEndpoint.getExchanges().stream().findFirst().get();

        RetrieveCardHolderDebitReq retrieveCardHolderDebitReq = exchange.getIn().getBody(RetrieveCardHolderDebitReq.class);

        Assertions.assertEquals(retrieveCardHolderDebitReq.getData().getUser().getId(), "SVP");
        Assertions.assertEquals(retrieveCardHolderDebitReq.getData().getCustomerCard().getNumber(), "0000377813384145240");
    }

    public RetrieveReq createTransformationDebitRequest() {
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
