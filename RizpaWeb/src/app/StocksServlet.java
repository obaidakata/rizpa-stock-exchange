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
import java.util.List;

@WebServlet(name = "StocksServlet", urlPatterns = {"/stocks"})
public class StocksServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());
        List<Stock> stocks = rizpaFacade.getAllStocks();
        response.setStatus(200);
        response.getOutputStream().println(new Gson().toJson(stocks));
    }
}
