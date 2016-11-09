package com.zerses.camelsandbox;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.main.Main;
import org.springframework.stereotype.Component;

public class AASimpleTest {

    //A simple Camel test with timer (so, nothing external)
    //And no extra (non-core) component requirements
    public static void main(String[] args) throws Exception {

        Main main = new Main();

        main.addRouteBuilder(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("timer://myTimerX?period=5000")
                .id("myTimerXRoute")
                .setBody()
                .simple("Route was fired at ${header.firedTime}")
                .log("${body}");
                
                
      
            }
        });
        main.run();
    }

}
