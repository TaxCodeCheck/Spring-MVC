package spring;

import net.avalara.avatax.rest.client.AvaTaxClient;
import net.avalara.avatax.rest.client.enums.AvaTaxEnvironment;
import net.avalara.avatax.rest.client.models.PingResultModel;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@SessionAttributes({"username","password"})
public class avataxController {

    @RequestMapping(value="/auth", method=RequestMethod.POST)
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

        return false;
    }

}


