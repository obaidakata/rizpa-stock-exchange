package app;

import com.google.gson.Gson;
import engine.Transaction;
import engine.command.CommandType;
import rizpa.RizpaFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@WebServlet(name = "CommandServlet", urlPatterns = {"/newCommand"})
public class CommandServlet extends HttpServlet {
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        try {
            String usernameFromSession = SessionUtils.getUsername(request);
            String stockSymbolFromSession = SessionUtils.getStockSymbol(request);
            String directionAsString = request.getParameter("direction");
            String commandTypeAsString = request.getParameter("commandType");
            String priceAsString = request.getParameter("price");
            String amountAsString = request.getParameter("amount");
            RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());
            List<Transaction> transactions = rizpaFacade.doCommand(
                    usernameFromSession,
                    stockSymbolFromSession,
                    directionAsString,
                    commandTypeAsString,
                    priceAsString,
                    amountAsString);
            response.setStatus(200);
            response.getOutputStream().println(new Gson().toJson(transactions));
        }
        catch (Exception e) {
            response.setStatus(401);
            response.getOutputStream().println(e.getMessage());
        }

    }
}
