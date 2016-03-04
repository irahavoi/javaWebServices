package com.irahavoi;

import javax.xml.soap.*;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Iterator;

public class DemoSoap {
    private static final String localName = "TimeRequest";
    private static final String namespace = "http://irahavoi/com";
    private static final String namespacePrefix = "ws";

    private ByteArrayOutputStream out;
    private ByteArrayInputStream in;

    public static void main(String[] args){
        new DemoSoap().request();
    }

    private void request(){
        try{
            //Build a soap message to send to an output stream
            SOAPMessage msg = createSoapMessage();

            //Inject the appropriate information in the message:
            SOAPEnvelope env = msg.getSOAPPart().getEnvelope();
            SOAPHeader header = env.getHeader();

            //Add an element to the soap header:
            Name lookupName = createQName(msg);
            header.addHeaderElement(lookupName).addTextNode("time_request");

            //Simulate sending a soap message to a remote system by writing it to a ByteArrayOutputStream:
            out = new ByteArrayOutputStream();
            msg.writeTo(out);


            trace("The sent SOAP message", msg);

            SOAPMessage response = processRequest();

            extractContentsAndPrint(response);

        } catch(SOAPException e){
            System.err.println(e);
        }
        catch (IOException e){
            System.err.println(e);
        }
    }

    private SOAPMessage createSoapMessage(){
        SOAPMessage msg = null;

        try{
            MessageFactory mf = MessageFactory.newInstance();
            msg = mf.createMessage();
        } catch(SOAPException e){
            System.err.println(e);
        }

        return msg;
    }

    private SOAPMessage createSoapMessage(InputStream in){
        SOAPMessage msg = null;

        try{
            MessageFactory mf = MessageFactory.newInstance();
            msg = mf.createMessage(null, in);
        } catch(SOAPException e){
            System.err.println(e);
        }
        catch (IOException e){
            System.err.println(e);
        }

        return msg;
    }

    private Name createQName(SOAPMessage message){
        Name name = null;

        try{
            SOAPEnvelope env = message.getSOAPPart().getEnvelope();
            name = env.createName(localName, namespacePrefix, namespace);
        } catch(SOAPException e){
            System.err.println(e);
        }

        return name;
    }

    private void trace(String s, SOAPMessage m){
        System.err.println("\n");
        System.err.println(s);

        try{
            m.writeTo(System.out);
        } catch(SOAPException e){
            System.err.println(e);
        }
        catch (IOException e){
            System.err.println(e);
        }
    }

    private SOAPMessage processRequest(){
        processIncomingSoap();
        coordinateStreams();
        return createSoapMessage(in);

    }

    private void processIncomingSoap(){
        try{
            //Copy output stream to input stream to simulate coordinated streams over a network connections.
            coordinateStreams();

            //Create the received SOAP message from the input stream
            SOAPMessage msg = createSoapMessage(in);

            //Inspect the message for the keyword 'time_request'
            //and process the request, if the keyword occurs
            Name lookupName = createQName(msg);

            SOAPHeader header = msg.getSOAPHeader();
            Iterator it = header.getChildElements(lookupName);

            Node next = (Node) it.next();

            String value = (next == null) ? "Error!" : next.getValue();

            if(value.toLowerCase().contains("time_request")){
                //Extract the body and the current time as element:
                String now = new Date().toString();
                SOAPBody body = msg.getSOAPBody();
                body.addBodyElement(lookupName).addTextNode(now);

                msg.saveChanges();

                //Write to the output stream:
                msg.writeTo(out);
                trace("The received/processed SOAP message: ", msg);
            }

        } catch(SOAPException e){
            System.err.println(e);
        }
        catch (IOException e){
            System.err.println(e);
        }

    }

    private void coordinateStreams() {
        in = new ByteArrayInputStream(out.toByteArray());
        out.reset();
    }

    private void extractContentsAndPrint(SOAPMessage msg){
        try{
            SOAPBody body = msg.getSOAPBody();

            Name lookupName = createQName(msg);
            Iterator it  = body.getChildElements(lookupName);

            Node next = (Node) it.next();

            String value = (next == null) ? "Error!" : next.getValue();
            System.out.println("\nReturned from server: " + value);
        } catch (SOAPException e){
            System.out.println("e");
        }
    }
}
