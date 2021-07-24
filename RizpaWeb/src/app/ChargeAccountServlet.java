package app;

import com.google.gson.Gson;
import engine.descriptor.User;
import rizpa.RizpaFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "ChargeAccountServlet", urlPatterns = {"/user/chargeAccount"})
public class ChargeAccountServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        String chargeValueAsString = request.getParameter("chargeValue");
        int chargeValue = 0;

        try {
            chargeValue = Integer.parseInt(chargeValueAsString);
        } catch (NumberFormatException e) {
            response.setStatus(401);
            response.getOutputStream().println("ChargeValue must be number");
            return;
        }

        if(usernameFromSession == null) {
            System.out.println("ERROR session empty");
            response.setStatus(401);
            response.getOutputStream().println("Error must login before");
        }
        else {
            RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());
            int userBalanceAfterChange;
            synchronized (getServletContext()) {
                userBalanceAfterChange = rizpaFacade.chargeUserAccount(usernameFromSession, chargeValue);
            }

            response.setStatus(200);
            response.getOutputStream().println(new Gson().toJson(userBalanceAfterChange));
        }
    }
}
