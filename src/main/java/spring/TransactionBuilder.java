package spring;

import JSON.JSONParser;
import com.google.gson.Gson;

import net.avalara.avatax.rest.client.enums.DocumentType;
import net.avalara.avatax.rest.client.models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@Controller
@SessionAttributes({"taxcode","zip","username","password"})
public class TransactionBuilder {
    @RequestMapping(value="/transaction", method=RequestMethod.GET)
    @ResponseBody
    public String transactionBuilder(HttpServletRequest post, @RequestParam String taxcode,
                                     @RequestParam String zip, @RequestParam String username, @RequestParam String password) {
        String rate = null;

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date date = new Date();
        CreateTransactionModel CTModel = new CreateTransactionModel();
        LineItemModel line = new LineItemModel();
        line.setTaxCode(taxcode);
        line.setDescription("doesn't matter");
        BigDecimal decimal = BigDecimal.valueOf(100);
        line.setAmount(decimal);
        line.setQuantity(decimal);
        ArrayList<LineItemModel> lines = new ArrayList<>();
        lines.add(line);

        AddressesModel addModel = new AddressesModel();
        AddressLocationInfo addInfo = new AddressLocationInfo();
        addInfo.setPostalCode(zip);

        addModel.setSingleLocation(addInfo);

        CTModel.setDate(date);
        CTModel.setCompanyCode("DEFAULT");
        CTModel.setCustomerCode("custom");
        CTModel.setType(DocumentType.SalesOrder);
        CTModel.setCurrencyCode("USD");
        CTModel.setCommit(false);
        CTModel.setAddresses(addModel);
        CTModel.setLines(lines);
        Gson gson = new Gson();
        try {
            String userPass = Base64.getEncoder().encodeToString((username + ":" + password).getBytes());
            String requestUrl = "https://rest.avatax.com/api/v2/transactions/create";
            StringEntity requestEntity = new StringEntity(gson.toJson(CTModel), ContentType.APPLICATION_JSON);

            CloseableHttpClient client = HttpClients.createDefault();
            HttpPost httpPost = new HttpPost(requestUrl);
            httpPost.addHeader("accept", "application/json");
            httpPost.addHeader("authorization", "Basic " + userPass);
            httpPost.setEntity(requestEntity);

            HttpResponse response = client.execute(httpPost);

            if (response.getStatusLine().getStatusCode() != 200 && (response.getStatusLine().getStatusCode() != 201)) {
                throw new RuntimeException("Error : " + response.getStatusLine().getStatusCode());
            }
            BufferedReader reader = new BufferedReader(new InputStreamReader((response.getEntity()
                    .getContent())));

            String scan;
            while ((scan = reader.readLine()) != null) {
                rate = scan;
            }
            //parses out the tax rate and sums the tax's then
            // converts to string after formatting it to 3 decimals places.
            DecimalFormat d3 = new DecimalFormat("#.000");
            double rateDouble = JSONParser.parseTaxRate(rate);
            rateDouble *= 100;
            rate = d3.format(rateDouble);
            rate = rate + "%";
            System.out.println(rate);
            return rate;
        } catch (ClientProtocolException e) {

            e.printStackTrace();

        } catch (IOException e) {

            e.printStackTrace();
        }
        System.out.println(rate);
        return rate;
    }
}
