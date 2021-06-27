package app.login;
import app.ServletUtils;
import app.SessionUtils;
import app.constant.Constants;
import rizpa.RizpaFacade;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class loginServlet extends HttpServlet {
    private static final Object lock = new Object();
    private static final String USER_STOCKS_URL = "users-stocks/users-stocks.html";
    private static final String ERROR_URL = "error.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String usernameFromParameter = request.getParameter(Constants.USERNAME);
        String userRole = request.getParameter(Constants.USERROLE);
        if (usernameFromParameter.isEmpty()) {
            System.out.println("Error must put username");
            response.setStatus(409);
            response.getOutputStream().println(ERROR_URL);
        } else {
            usernameFromParameter = usernameFromParameter.trim();
            synchronized (lock) {
                RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());

                if (rizpaFacade.isUserExists(usernameFromParameter)) {
                    String errorMessage = "Username" + usernameFromParameter + " already exists. Please enter a different username";
                    response.setStatus(401);
                    response.getOutputStream().println(errorMessage);
                }
                else {
                    rizpaFacade.addUser(usernameFromParameter, userRole);
                    request.getSession(true).setAttribute(Constants.USERNAME, usernameFromParameter);
                    System.out.println("On login, request URL is: " + request.getRequestURI());
                    response.setStatus(200);
                    response.getOutputStream().println(USER_STOCKS_URL);
                }
            }
        }
    }

    private void dealWithFreshUser(HttpServletRequest request, HttpServletResponse response) throws IOException {

    }
}