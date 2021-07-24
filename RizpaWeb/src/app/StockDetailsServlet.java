package app;

import com.google.gson.Gson;
import engine.descriptor.Stock;
import rizpa.RizpaFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "StockDetailsServlet", urlPatterns = {"/stockDetails"})
public class StockDetailsServlet extends HttpServlet {
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
            Stock stock = rizpaFacade.getStockBySymbol(stockSymbolFromSession);
            response.setStatus(200);
            response.getOutputStream().println(new Gson().toJson(stock));
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String userFromFromSession = SessionUtils.getUsername(request);
        if(userFromFromSession == null) {
            System.out.println("ERROR session empty");
            response.setStatus(401);
            response.getOutputStream().println("Error in stock details");
        }
        else {
            RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());
            String companyName = request.getParameter("companyName");
            String symbol = request.getParameter("symbol");
            String numberOfStocks = request.getParameter("numberOfStocks");
            String companyMarketValue = request.getParameter("companyMarketValue");
            try {
                rizpaFacade.doIPO(userFromFromSession,
                        companyName,
                        symbol,
                        numberOfStocks,
                        companyMarketValue
                        );
                response.setStatus(200);
                response.getOutputStream().println("Stock created successfully");
            } catch (Exception exception) {
                response.setStatus(401);
                response.getOutputStream().println(exception.getMessage());
            }

        }
    }
}
