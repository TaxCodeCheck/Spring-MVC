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
                System.out.println("Authentication received!");
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

}


