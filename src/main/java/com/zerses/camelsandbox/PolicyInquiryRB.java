package com.zerses.camelsandbox;

import java.util.Map;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.springframework.stereotype.Component;

@Component
public class PolicyInquiryRB extends RouteBuilder {

    @Override
    public void configure() throws Exception {

        System.out.println("\n\n=====================================================================  Environment : Start ================ ");
                          
        Map<String, String> env = System.getenv();
        String extActiveMqServicePort = env.get("EXT_ACTIVEMQ_SERVICE_PORT");
        
        for (String envName : env.keySet()) {
            System.out.format("%s=%s%n",
                              envName,
                              env.get(envName));
        }
        
        System.out.println("==============="+extActiveMqServicePort+"======================================================  Environment : End ================ \n\n");
        
        
        restConfiguration()
            .component("servlet")
            .contextPath("/")
            .host("0.0.0.0")
            // .port(9000)
            .bindingMode(RestBindingMode.json)
            // Set up Swagger config
            .apiContextPath("/api-doc")
            .apiProperty("api.title", "Test API").apiProperty("api.version", "1.2.3")
            // and enable CORS so that Swagger UI can access this and present
            // its cool interface
            .apiProperty("cors", "true");
        ;

        rest()
            .get("/policy/{policyId}")
            .route()
            .to("log:From_REST_find?showAll=true")
            .choice()
            .when(header("policyId").isEqualTo("111"))
            .transform(simple("Policy # ${header.policyId}: Workers Comp - Acme Widgets"))
            .otherwise()
            .transform(simple("Policy # ${header.policyId}: Workers Comp - Should not see this message!!!"))
            .inOut("direct:abc")
            .end();

        from("direct:abc")
            .transform(simple("Policy # ${header.policyId}: Sending Data to Message Broker"))
            .inOut("activemq:queue:TEST.FOO")
            .transform(simple("Policy # ${header.policyId}: Workers Comp - After return from Message Broker"));

    }

}
