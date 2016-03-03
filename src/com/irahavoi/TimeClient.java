package com.irahavoi;


import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;

public class TimeClient {
    public static void main(String[] args) throws Exception{
        URL url = new URL(Main.ADDRESS + "?wsdl");

        //Qualified name of the service
        //1st argument is the service URI
        //2nd argument is the service name mublished in the WSDL
        QName qname = new QName("http://irahavoi.com/", "TimeServerImplService");

        //Factory for the service.
        Service service = Service.create(url, qname);

        //Extract the endpoint interface, the service "port".
        TimeServer eif = service.getPort(TimeServer.class);

        System.out.println(eif.getTimeAsString());
        System.out.println(eif.getTimeAsElapsed());

    }
}
