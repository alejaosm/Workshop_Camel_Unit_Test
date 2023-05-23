package co.com.bancolombia.infrastructure.configuration;

import co.com.bancolombia.integracion.integration_adapter.httprequest.routes.ApiConnectConsumeRoute;
import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class IAConfigApiC extends RouteBuilder {

    @Autowired
    private CamelContext camelContext;

    @Override
    public void configure() throws Exception {

        camelContext.addRoutes(new ApiConnectConsumeRoute());
    }
}