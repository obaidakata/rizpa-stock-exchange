package app;

import app.constant.Constants;
import rizpa.RizpaFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet(name = "StockRedirectServlet", urlPatterns = {"/StockRedirect"})
public class StockRedirectServlet extends HttpServlet {
    private static final String ERROR_URL = "error.html";
    private static final Object lock = new Object();
    private static final String STOCK_DETAILS_URL = "../stockDetails/stockDetails.html";

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String stockSymbolFromParameter = request.getParameter(Constants.STOCK_SYMBOL);
        if (stockSymbolFromParameter.isEmpty()) {
            System.out.println("Error must put stock symbol");
            response.setStatus(409);
            response.getOutputStream().println(ERROR_URL);
        } else {
            stockSymbolFromParameter = stockSymbolFromParameter.trim();
            synchronized (lock) {
                RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());

                if (rizpaFacade.isStockExists(stockSymbolFromParameter)) {
                    request.getSession(true).setAttribute(Constants.STOCK_SYMBOL, stockSymbolFromParameter);
                    response.setStatus(200);
                    response.getOutputStream().println(STOCK_DETAILS_URL);
                }
                else {
                    String errorMessage = "Stock " + stockSymbolFromParameter + " not exists. Please enter a different symbol";
                    response.setStatus(401);
                    response.getOutputStream().println(errorMessage);
                }
            }
        }

    }
}
