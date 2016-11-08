# osimple - Example Project for OpenShift 3 

This module implements a smaple project to test some OpenShift3 features and configuration. 


## Building

You can build this module with Maven:

> mvn clean install

## Packaging

This project is packaged as a WAR file

##Pre-requisites

You need an ActiveMQ broker installed on some host. This project tests connecting to such a broker. This should be a public IP or at least visible from your OpenShift installation
You need some software running on an external host (could be the same as the ActiveMQ broker's host) that reads from an ActiveMQ queue and responds (JMS-style) 

You need access to OpenShift v3 to run this project. On Windows, the easiest way is  to use the All-In-One virtual Machine https://www.openshift.org/vm/

## Running locally
You should be able to run this locally in something like Tomcat if you export an environment variable pointing to your ActiveMQ broker.
E.g.   set EXT_ACTIVEMQ_SERVICE_PORT=tcp://131.141.156.177:61616"

## Running on Open Shift 3

### Create your Project and add the main app

These steps can be performed using your browser, pointing at your openShift web-console, or using the OpenShift CLI (oc command).

Login to OpenShift (oc login)

Create a new Project (oc new-project myproject)

Add a Wildfly app. This template comes with the default OpenShift installation.So, the simplest way would be to use the web-console to add it

* Click on one of the wildfly templates (choose the latest)
* On the pop-up screen provide a name for the app
* And, point to the [osimple Github repository](https://github.com/DariusX/osimple.git), or to your own forked/cloned version
	
###Add ActiveMQ service and endpoint

Add an OpenShift service that wraps your external ActiveMQ broker. The selectors are left blank because we don't want the deployment to be inside the OpenShift cluster
	
* The file amqService.json has the definition. No changes are needed for your environment, since the service is only a "logical" starting point
* Use the CLI and make sure you are logged in (oc login) and that the correct project is active (oc get project)
* Use the CLI to create the endpoint from the file (oc create -f amqService.json)
	
Add an EndPoint for the above ActiveMQ service. This is where you point to the IP of your ActiveMQ broker

* The file amqEndPoints.json needs to be edited. The IP address in the file must be replaced with the IP address of your ActiveMQBroker
 	(The reason this is plural is that -- in general -- a service can load-balance across multiple end-points.)
* Once the change has been made, use the CLI to create the endpoint from the file (oc create -f amqEndpoints.json)
 	
In the web console, you should see another app show up in your project. If you click on it, you should see that it is a service

* It should have been assigned an internal IP, and the port should be 61616 (assuming you have kept the ActiveMQ default)
* You can also use the CLI (oc get services) (oc get endpoints) to list what you have just created


## Testing on Open Shift 3
  
You will need to use the route to your app (Wildfly will use port 8080 by default). The route is shown on the Overview page.
You can also find it with the CLI (oc get routes)

The WAR file is osimple.war
Within this file, the Camel Servlet has been set up to use /rest, within that context (i.e. /osimple/rest)
The specific REST service has been set up to use /policy/{policyId} within that context

Suppose OpenShift tells you your route is: http://osimple-app-osimple.192.168.99.100.xip.io/

Using a browser, go to http://osimple-app-osimple.192.168.99.100.xip.io/osimple/rest/policy/111
Notice that the policyId is 111. This has been hard-coded to simply return a message without doing anything else
The reply should be: "Policy # ${header.policyId}: Workers Comp - Acme Widgets"

Next, try http://osimple-app-osimple.192.168.99.100.xip.io/osimple/rest/policy/222
Any other policyId will attempt to hit your ActiveMQ broker and will wait to a response 
At this point, three common errors occur:
	A NullException typically means the IP to your broker is empty
	A connection error implies some problem with the particular IP for the broker
	A timeout typically means there's no response
	


