package co.com.bancolombia.domain.usecases.routes;

import co.com.bancolombia.domain.usecases.beans.BusinessErrorsResponse;
import co.com.bancolombia.infrastructure.properties.RestParams;
import co.com.bancolombia.infrastructure.utils.Util;
import co.com.bancolombia.integracion.integration_adapter.httprequest.utils.Params;
import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.http.conn.ConnectTimeoutException;
import org.apache.http.conn.HttpHostConnectException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.net.ssl.SSLHandshakeException;
import java.net.SocketTimeoutException;

@Component
public class GenericApiCRoute extends RouteBuilder {

    @Value("${service.internal.ia-api-connect.config.client-id}")
    private String clientId;

    @Value("${service.internal.ia-api-connect.config.client-secret}")
    private String clientSecret;

    @Autowired
    private BusinessErrorsResponse badResponseBusiness;

    @Override
    public void configure() throws Exception {

        onException(SocketTimeoutException.class, ConnectTimeoutException.class, HttpHostConnectException.class)
                .log("[ERROR] Error en API CONNECT")
                .handled(true)
                .bean(badResponseBusiness, "createBadResponse(*, 'ERROR_GENERIC')").id("error-bean-exception-id")
                .marshal().json()
                .end();

        from("direct:generic-consume-apic").routeId("generic-consume-apic-route-id")
                .setHeader(Params.CLIENT_ID, () -> clientId)
                .setHeader(Params.CLIENT_SECRET, () -> clientSecret)
                .setHeader(Params.MESSAGE_ID, exchangeProperty(Params.MESSAGE_ID))
                .setProperty(Params.METHOD, () -> Params.POST)
                .setProperty(Params.PATH_PARAMS, () -> Util.uriParameters())
                .setProperty(Params.ACCEPT_HEADER, () -> RestParams.JSON_TYPE_VALUE)
                .setProperty(Params.CONTENT_TYPE_HEADER, () -> RestParams.JSON_TYPE_VALUE)
                .doTry()
                    .to("direct:consumeIAApiC").id("consume-apic-id")
                    .log(LoggingLevel.INFO, "After API Payment Methods --- ${body}")
                .doCatch(SocketTimeoutException.class, SSLHandshakeException.class,
                        ConnectTimeoutException.class, HttpHostConnectException.class)
                    .bean(badResponseBusiness, "createBadResponse(*, 'ERROR_GENERIC')").id("error-bean-id")
                    .marshal().json()
                .end();
    }
}