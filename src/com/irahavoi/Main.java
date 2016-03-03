package com.irahavoi;

import javax.xml.ws.Endpoint;

public class Main {

    public static final String ADDRESS = "http://127.0.0.1:9876/ws";
    public static void main(String[] args) {
        Endpoint.publish(ADDRESS, new TimeServerImpl());
        System.out.println("Web Service is up and running..");
        System.out.println("You can access it at: " + ADDRESS + "?wsdl");
    }
}
