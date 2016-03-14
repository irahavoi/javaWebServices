package com.irahavoi.rest;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class TeamsClient {
    private static final String endpoint = "http://localhost:8888/teams";

    public static void main(String args[]){
        new TeamsClient().sendRequests();
    }

    private void sendRequests(){
        try{
            //GET
            HttpURLConnection conn = getConnection(endpoint, "GET");
            conn.connect();
            printAndParse(conn, false);
        } catch (IOException e){
            System.out.println(e);
        } catch (NullPointerException e){
            System.out.println(e);
        }
    }

    private HttpURLConnection getConnection(String urlStr, String verb){
        HttpURLConnection conn = null;

        try{
            URL url = new URL(urlStr);

            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod(verb);
        } catch (MalformedURLException e){
            System.out.println(e);
        } catch(IOException e){
            System.out.println(e);
        }

        return  conn;
    }

    private void printAndParse(HttpURLConnection conn, boolean parse){
        try{
            String xml = "";
            BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            String next = null;

            while((next = reader.readLine()) != null){
                xml += next;
            }

            System.out.println("The raw XML: " + xml);

            if(parse){
                //TODO:
                throw new RuntimeException("Parsing has not been implemented yet");
            }
        } catch (Exception e){
            System.out.println(e);
        }
    }
}
