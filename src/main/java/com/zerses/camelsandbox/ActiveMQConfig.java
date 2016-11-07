package com.zerses.camelsandbox;

import java.util.Map;

import javax.annotation.PostConstruct;

import org.apache.activemq.camel.component.ActiveMQComponent;
import org.apache.camel.CamelContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class ActiveMQConfig {
    
    @Autowired
    CamelContext camelContext;
    
    @PostConstruct
    public void xxx()
    {
        Map<String, String> env = System.getenv();
        String extActiveMqServicePort = env.get("EXT_ACTIVEMQ_SERVICE_PORT");
        System.out.println("======  FOUND FOUND ========="+extActiveMqServicePort+"=========\n\n");
        camelContext.addComponent("activemq", ActiveMQComponent.activeMQComponent(extActiveMqServicePort));
    }

}
