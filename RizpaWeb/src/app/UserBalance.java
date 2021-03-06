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
import java.time.LocalDateTime;

@WebServlet(name = "UserBalance", urlPatterns = {"/userBalance"})
public class UserBalance extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        if(usernameFromSession == null) {
            System.out.println("ERROR session empty");
            response.setStatus(401);
            response.getOutputStream().println("Error must login before");
        }
        else {
            RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());
            User user = rizpaFacade.getUserByName(usernameFromSession);
            response.setStatus(200);
            response.getOutputStream().println(new Gson().toJson(user.getBalance()));
        }
    }
}
