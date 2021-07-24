package app;

import com.google.gson.Gson;
import engine.command.Command;
import rizpa.RizpaFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@WebServlet(name = "BuyCommandsServlet", urlPatterns = {"/buyCommands"})
public class BuyCommandsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String stockSymbolFromSession = SessionUtils.getStockSymbol(request);
        if(stockSymbolFromSession == null) {
            System.out.println("ERROR session empty");
            response.setStatus(401);
            response.getOutputStream().println("Error in BuyCommandsServlet");
        }
        else {
            RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());
            Collection<Command> buyCommands = rizpaFacade.getBuyCommands(stockSymbolFromSession);
            response.setStatus(200);
            response.getOutputStream().println(new Gson().toJson(buyCommands));
        }
    }
}
