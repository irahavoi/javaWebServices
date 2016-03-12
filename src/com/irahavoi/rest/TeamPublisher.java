package com.irahavoi.rest;

import javax.xml.ws.Endpoint;

public class TeamPublisher {
    public static void main(String[] args){
        int port = 8888;

        String url = "http://localhost:" + port + "/teams";

        System.out.println("Publishing teams restfully on port " + port);

        Endpoint.publish(url, new RestfulTeams());
    }
}
