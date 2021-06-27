package app;

import com.google.gson.Gson;
import engine.descriptor.User;
import rizpa.RizpaFacade;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {
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
            System.out.println(user);
            response.setStatus(200);
            response.getOutputStream().println(new Gson().toJson(user));
        }
    }
}
