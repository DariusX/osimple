package com.zerses.camelsandbox;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.stereotype.Component;

@Component
public class SimpleRoute extends RouteBuilder {

    @Override
    public void configure() throws Exception {
        from("timer://myTimer?period=12000")
            .id("myTimerRoute")
            .setBody()
            .simple("SimpleRoute was fired at ${header.firedTime}")
            .log("${body}");

    }

}
