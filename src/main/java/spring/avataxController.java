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
import java.math.BigDecimal;

@Controller
@ResponseBody()
public class avataxController {

    public String username = "tylerthewonderfull@gmail.com";
    public String password = "3E2B46DA6E";
    @GetMapping("/auth/{username}/{password}")
    @RequestMapping("/auth/{username}/{password}")
    public String auth(HttpServletRequest request, Model model, String user, String pass){
        AvaTaxClient client = new AvaTaxClient("Test", "1.0", "localhost", AvaTaxEnvironment.Production).withSecurity(username, password);
        try{
            PingResultModel ping = client.ping();
            if(ping.getAuthenticated()){
                System.out.println("Authentication recieved!");
            }else{
                System.out.println("Authentication rejected");
            }
        }catch(Exception e){
            System.out.println("inauthenticated");
            System.out.println(e);
        }
        return "logg in worked";
        }
    }


