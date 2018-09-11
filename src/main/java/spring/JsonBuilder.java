package spring;

import java.util.*;
import java.io.IOException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonBuilder {

    public String type;
    public String companyCode;
    public String date;
    public String customerCode;
    public String purchaseOrderNo;
    public Boolean commit;
    public String currencyCode;
    public ArrayList<Map<String, Object>> lines;
    public HashMap<String, HashMap> addresses;

    JsonBuilder (String type, String companyCode, String number, int quantity, int amount, String taxCode,
                 String itemCode, String date, String customerCode, String purchaseOrderNo, Boolean commit,
                 String currencyCode, String description, String address, String city, String region,
                 String country, String postalCode){

        this.type = type;
        this.companyCode = companyCode;
        this.date = date;
        this.customerCode = customerCode;
        this.purchaseOrderNo = purchaseOrderNo;
        this.commit = commit;
        this.currencyCode = currencyCode;

        HashMap<String, Object> mapLines = new HashMap<>();
        mapLines.put("number", number);
        mapLines.put("quantity", quantity);
        mapLines.put("amount", amount);
        mapLines.put("taxCode", taxCode);
        mapLines.put("itemCode", itemCode);
        mapLines.put("description", description);

        this.lines= new ArrayList<>();
        this.lines.add(mapLines);

        HashMap<String, String> mapLocal = new HashMap<>();
        mapLocal.put("line1", address);
        mapLocal.put("city", city);
        mapLocal.put("region", region);
        mapLocal.put("country", country);
        mapLocal.put("postalCode", postalCode);

        HashMap<String, HashMap> singleLocation = new HashMap<>();
        singleLocation.put("singleLocation",mapLocal);

        this.addresses = new HashMap<>();
        this.addresses = singleLocation;
    }
}

