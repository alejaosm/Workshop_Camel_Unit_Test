package co.com.bancolombia.domain.usecases.processors;

import co.com.bancolombia.domain.entities.model.dtos.enums.TypeEnum;
import co.com.bancolombia.domain.entities.model.dtos.in.retrieveconsolidatedopportunitiesbycustomer.RetrieveConsolidatedCustomerReqData;
import co.com.bancolombia.domain.entities.model.dtos.in.retrieveconsolidatedopportunitiesbycustomer.RetrieveConsolidatedCustomerReqDataCustomer;
import co.com.bancolombia.domain.entities.model.dtos.in.retrieveconsolidatedopportunitiesbycustomer.RetrieveCustomerReq;
import co.com.bancolombia.infrastructure.properties.RestProperties;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class PreapprovalReqProcess implements Processor {


    @Override
    public void process(Exchange exchange) throws IOException {

        RetrieveCustomerReq customerReq = RetrieveCustomerReq
                .builder()
                .data(RetrieveConsolidatedCustomerReqData.builder()
                        .customer(RetrieveConsolidatedCustomerReqDataCustomer.builder()
                                .type(exchange.getProperty("type", TypeEnum.class))
                                .number(exchange.getProperty("number", String.class))
                                .build())
                        .build())
                .build();

        exchange.getMessage().setBody(customerReq);

    }
}