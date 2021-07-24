package app;

import com.google.gson.Gson;
import engine.DealData;
import engine.descriptor.Stock;
import rizpa.RizpaFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@WebServlet(name = "TransactionsServlet", urlPatterns = {"/stockTransactions"})
public class TransactionsServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String stockSymbolFromSession = SessionUtils.getStockSymbol(request);
        if(stockSymbolFromSession == null) {
            System.out.println("ERROR session empty");
            response.setStatus(401);
            response.getOutputStream().println("Error in stock details");
        }
        else {
            RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());
            Collection<DealData> transactions = rizpaFacade.getTransactions(stockSymbolFromSession);
            response.setStatus(200);
            response.getOutputStream().println(new Gson().toJson(transactions));
        }
    }
}