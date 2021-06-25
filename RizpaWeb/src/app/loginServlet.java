package app;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class loginServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String username = SessionUtils.getUsername(request);
        if(username != null) {
            System.out.println("Not null");
            UserRole userRole = SessionUtils.getUserRole(request);
            String redirectTo;

            if (userRole == UserRole.Admin) {
                redirectTo = "../adminPage.html";
            } else {
                redirectTo = "../user-stocks/user-stocks.html";
            }

            response.sendRedirect(redirectTo);
        }
        else {
            username = request.getParameter("username");
            if(username.isEmpty()) {
                System.out.println("Error must put username");
                response.sendRedirect("error.html");
            }
            else {
                username = username.trim();
            }
        }

    }
}
