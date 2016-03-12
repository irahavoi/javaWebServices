package com.irahavoi.rest;


import javax.annotation.Resource;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.*;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.http.HTTPBinding;
import javax.xml.ws.http.HTTPException;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServiceProvider

//Message mode indicates that the service
//wants access to the whole message (http headers and body)
//if you need only body - use PAYLOAD - the default type.
@ServiceMode(value = Service.Mode.MESSAGE)

//Http binding as opposed to SOAP binding
@BindingType(value = HTTPBinding.HTTP_BINDING)
public class RestfulTeams implements Provider<Source>{
    @Resource
    private WebServiceContext context;

    private Map<String, Team> teamMap;
    private List<Team> teams;
    private byte[] teamBytes;

    private static final String fileName = "teams.ser";

    public RestfulTeams(){
        readTeamsFromFile();
        deserialize();
    }

    //Handles incoming requests and generates responses
    @Override
    public Source invoke(Source request) {
        if(context == null){
            throw new RuntimeException("Failed to inject the context!");
        }

        //Grab the message context and extraxt the request verb
        MessageContext msgContext = context.getMessageContext();
        String httpVerb = (String) msgContext.get(MessageContext.HTTP_REQUEST_METHOD);

        httpVerb = httpVerb.trim().toUpperCase();

        //Support only GET requests for now:
        if(httpVerb.equals("GET")){
            return doGet(msgContext);
        } else{
            throw new HTTPException(405); //method not allowed
        }
    }

    private Source doGet(MessageContext msgContext){
        //Parse the query string
        String queryString = (String) msgContext.get(MessageContext.QUERY_STRING);

        //Get all teams:
        if(queryString == null){
            return new StreamSource(new ByteArrayInputStream(teamBytes));
        } else{
            String name = getValueFromQS("name", queryString);

            Team team = teamMap.get(name);

            if(team == null){
                throw new HTTPException(404); //not found
            }

            ByteArrayInputStream stream = encodeToStream(team);
            return  new StreamSource(stream);
        }
    }

    private ByteArrayInputStream encodeToStream(Object object){
        //Serialize object to xml and return;
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        XMLEncoder enc = new XMLEncoder(stream);

        enc.writeObject(object);
        enc.close();

        return new ByteArrayInputStream(stream.toByteArray());
    }

    private String getValueFromQS(String key, String qs){
        String[] parts = qs.split("=");

        if(!parts[0].equalsIgnoreCase(key)){
            throw new HTTPException(400); //bad request
        }

        return parts[1].trim();
    }

    private void readTeamsFromFile(){
        try{
            String cwd = System.getProperty("user.dir");
            String sep = System.getProperty("file.separator");
            String path = getFilePath();

            int len = (int) new File(path).length();
            teamBytes = new byte[len];

            new FileInputStream(path).read(teamBytes);
        } catch(IOException e){
            System.err.println(e);
        }
    }

    private void deserialize(){
        //Deserialize the bytes into the list of teams;
        XMLDecoder dec = new XMLDecoder(new ByteArrayInputStream(teamBytes));
        teams = (List<Team>) dec.readObject();

        //Create a map for quick lookup of teams.

        teamMap = Collections.synchronizedMap(new HashMap<String, Team>());
        for(Team team : teams){
            teamMap.put(team.getName(), team);
        }
    }

    private String getFilePath(){
        String cwd = System.getProperty("user.dir");
        String sep = System.getProperty("file.separator");

        return cwd + sep + "teams" + sep + fileName;
    }


}
