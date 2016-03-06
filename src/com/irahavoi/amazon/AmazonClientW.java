package com.irahavoi.amazon;

import awcClient.*;

import javax.xml.ws.Holder;
import java.util.List;

public class AmazonClientW {
    public static void main(String [] args){
        if(args.length < 1){
            return;
        }

        //Need to provide a key to use the service:
        final String access_key = args[0];

        //Construct the service object to get the port:
        AWSECommerceService service = new AWSECommerceService();
        AWSECommerceServicePortType port = service.getAWSECommerceServicePort();

        //Construct an empty request object and add details:
        ItemSearchRequest request = new ItemSearchRequest();
        request.setSearchIndex("Books");
        request.setKeywords("quantum gravity");

        ItemSearch search = new ItemSearch();
        search.getRequest().add(request);
        search.setAWSAccessKeyId(access_key);

        Holder<OperationRequest> operationRequestHolder = null;

        Holder<List<Items>> items = new Holder<List<Items>>();

        port.itemSearch(search.getMarketplaceDomain(),
                search.getAWSAccessKeyId(),
                search.getAssociateTag(),
                search.getXMLEscaping(),
                search.getValidate(),
                search.getShared(),
                search.getRequest(),
                operationRequestHolder,
                items);

        //Unpack the response:
        Items result = items.value.get(0);
        List<Item> itemList = result.getItem();
        for(Item item : itemList){
          System.out.println(item.getItemAttributes().getTitle());
        }
    }
}
