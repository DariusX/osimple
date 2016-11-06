package com.zerses.camelsandbox;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer://myTimer?period=12000")
            .setBody()
            .simple("Route was fired at ${header.firedTime}")
            .log("${body}");

        from("direct:test007")
            .setBody()
            .simple("Route 007 was fired at ${header.firedTime}")
            .log("${body}");
    }

}
