package com.zerses.camelsandbox;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class PolicyInquiryRB extends RouteBuilder {

    @Override
    public void configure() throws Exception {


    restConfiguration()
        .component("servlet")
        .contextPath("/")
        .host("0.0.0.0")
  //      .port(9000)
        .bindingMode(RestBindingMode.json)
        // Set up Swagger config
        .apiContextPath("/api-doc")
        .apiProperty("api.title", "Test API").apiProperty("api.version", "1.2.3")
        // and enable CORS so that Swagger UI can access this and present
        // its cool interface
        .apiProperty("cors", "true");
    ;
    

    rest()
        .get("/policy/{msg}")
        .route()
        .to("log:From_REST_find?showAll=true")
        ;
    }

}
