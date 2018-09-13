package spring;

import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.TransactionBuilder;
import net.avalara.avatax.rest.client.enums.AvaTaxEnvironment;
import net.avalara.avatax.rest.client.enums.DocumentType;
import net.avalara.avatax.rest.client.enums.TransactionAddressType;
import net.avalara.avatax.rest.client.models.PingResultModel;
import net.avalara.avatax.rest.client.models.TransactionModel;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Arrays;
import java.util.Base64;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


@Controller
@SessionAttributes({"username","password"})
public class avataxController {

    @GetMapping("/auth")
    @ResponseBody
    public Boolean auth(HttpServletRequest request, Model model, @RequestParam String username, @RequestParam String password){
        HttpSession sesh = request.getSession();
        AvaTaxClient client = new AvaTaxClient("Test", "1.0", "localhost", AvaTaxEnvironment.Production).withSecurity(username, password);
        try{
            PingResultModel ping = client.ping();
            if(ping.getAuthenticated()){
                sesh.setAttribute("isLoggedIn",true);
                sesh.setAttribute("username",username);
                sesh.setAttribute("password",password);
                System.out.println("Authentication recieved!");
            }else{
                System.out.println("Authentication rejected");
                return false;
            }
        }catch(Exception e){
            System.out.println("inauthenticated");
            System.out.println(e);
        }

        return true;
    }

    public class TaxCodeSearch {
        @GetMapping("/search/taxcode")
        @ResponseBody
        public String searchTaxCode() {

            String builder = null;
            try {
                String userNamePass = Base64.getEncoder().encodeToString(("Ahmedhosman41@gmail.com:Ahmed3.14").getBytes());

                String requestUrl = "https://rest.avatax.com/api/v2/definitions/taxcodes";


                CloseableHttpClient client = HttpClients.createDefault();
                HttpGet requestBuilder = new HttpGet(requestUrl);
                requestBuilder.addHeader("accept", "application/json");
                requestBuilder.addHeader("authorization", "Basic " + userNamePass);

                HttpResponse response = client.execute(requestBuilder);

                if (response.getStatusLine().getStatusCode() != 200) {
                    throw new RuntimeException("Search Failed : Error Code : " + response.getStatusLine().getStatusCode());
                }

                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader((response.getEntity().getContent())));

                String reader;
                while ((reader = bufferedReader.readLine()) != null) {
                    String[] outputArray = reader.split("[^a-zA-Z0-9.\\s]");
                    System.out.println("Output " + Arrays.deepToString(outputArray));
                    builder = reader;
                }
                System.out.println(parse(builder));
                builder = parse(builder);
                return builder;
            } catch (ClientProtocolException e) {

                e.printStackTrace();

            } catch (IOException e) {

                e.printStackTrace();
            }
            builder = parse(builder);
            System.out.println(builder);
            return builder;
        }
    }

    public static String parse (String str){
        String[] stray = new String[2];
        stray[0] = str;
        stray[1] = "";

        while (stray[0].contains("taxCode\"")) {
            stray = parse(stray);
        }
        return stray[1];
    }

    private static String[] parse(String[] arr){
        String str = arr[0];
        int index = str.indexOf("taxCode\"");
        if(arr[1].length() != 0) arr[1]+= ", ";
        arr[1] += str.substring(index + 10, index + 18);
        arr[0] = str.substring(index+18);
        return arr;
    }
}


