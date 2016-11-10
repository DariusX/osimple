package com.zerses.camelsandbox;

import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;
import org.apache.camel.model.rest.RestParamType;
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

        System.out.println("===============" + extActiveMqServicePort + "======================================================  Environment : End ================ \n\n");

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

        //@formatter:off
        rest()
            .get("/policy/{policyId}")
            .description("Find a specific Policy")
            .param().name("policyId").type(RestParamType.path).description("Policy ID").endParam()
            .outType(String.class)
            .route()
            .to("log:From_REST_find?showAll=true")
            .choice()
              .when(header("policyId").isEqualTo("111"))
              .transform(simple("Policy # ${header.policyId}: Workers Comp - Acme Widgets"))
              
            .when(header("policyId").isEqualTo("222"))
              .transform(simple("Policy # ${header.policyId}: Workers Comp - Should not see this message!!!"))
              .inOut("direct:fileRoute")
              
            .otherwise()
              .transform(simple("Policy # ${header.policyId}: Workers Comp - Should not see this message!!!"))
              .inOut("direct:msgRoute")
            .end();
        //@formatter:on

        from("direct:msgRoute")
            .transform(simple("Policy # ${header.policyId}: Sending Data to Message Broker"))
            .inOut("activemq:queue:TEST.FOO")
            .log("Returned body: ${body}")
            .transform(simple("Policy # ${header.policyId}: Workers Comp - After return from Message Broker"));

        from("direct:fileRoute")
            .process(new Processor() {

                @Override
                public void process(Exchange exchange) throws Exception {
                    String fileLine = NetFileReader.readFile("/data/testfile.txt");
                    System.out.println(fileLine);
                    exchange.getIn().setBody(fileLine);

                }
            });

        String inDir = "/data";
        // from("file://" + inDir +
        // "?move=../arch/${date:now:yyyyMMddhhmmss}.${file:name}")
        from("file://" + inDir + "?noop=true")
            .id("fileTestRoute")
            .log(LoggingLevel.INFO, "Reading file: ${file:name}");

    }

}
