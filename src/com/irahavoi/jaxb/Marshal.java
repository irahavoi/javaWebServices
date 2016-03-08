package com.irahavoi.jaxb;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Marshal {
    private static final String fileName = "bd_mar";

    public static void main(String[] args){
        new Marshal().runExample();
    }

    public void runExample(){
        try {
            JAXBContext ctx = JAXBContext.newInstance(Skier.class);
            Marshaller m = ctx.createMarshaller();
            m.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            Skier skier = createSkier();

            m.marshal(skier, System.out);

            FileOutputStream out = new FileOutputStream(fileName);
            m.marshal(skier, out);
            out.close();

            System.out.println();
            System.out.println("Unmarshalling...");
            Unmarshaller u = ctx.createUnmarshaller();
            Skier personClone = (Skier) u.unmarshal(new File(fileName));
            System.out.println();
            m.marshal(personClone, System.out);


        } catch (JAXBException e){
            System.out.println(e);
        } catch (IOException e){
            System.out.println(e);
        }
    }

    private Skier createSkier(){
        Person person = new Person("John Doe", 29, "Male");
        List<String> list = new ArrayList<String>();

        list.add("12 olympic medals");
        list.add("9 world champinships");
        list.add("Greatest skier");

        return  new Skier(person, "Norway", list);
    }
}
