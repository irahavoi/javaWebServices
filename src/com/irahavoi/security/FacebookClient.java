package com.irahavoi.security;

import javax.net.ssl.HttpsURLConnection;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.cert.Certificate;

public class FacebookClient {
    private static final String facebookUrl = "https://facebook.com:443";

    public static void main(String[] args){
        new FacebookClient().connect();
    }

    private void connect(){
        try{
            URL url = new URL(facebookUrl);

            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.connect();
            dumpFeatures(connection);
        } catch (MalformedURLException e){
            print(e);
        } catch (IOException e){
            print(e);
        }
    }

    private void dumpFeatures(HttpsURLConnection connection){
        try{
            print("Status code: " + connection.getResponseCode());
            print("Cipher suite: " + connection.getCipherSuite());
            Certificate[] certs = connection.getServerCertificates();

            for(Certificate cert :certs){
                print("\tCert type: " + cert.getType());
                print("\tHash code: " + cert.hashCode());
                print("\tAlgorithm: " + cert.getPublicKey().getAlgorithm());
                print("\tFormat: " + cert.getPublicKey().getFormat());
                print("");
            }
        } catch (Exception e){
            print(e);
        }
    }

    private void print(Object s){
        System.out.println(s);
    }
}
