package app;

import com.google.gson.Gson;
import engine.descriptor.Users;
import rizpa.RizpaFacade;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("application/json;charset=UTF-8");
        RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());
        Users users = rizpaFacade.getUsers();
        response.setStatus(200);
        response.getOutputStream().println(new Gson().toJson(users));
    }
}
