package app;

import com.google.gson.Gson;
import engine.TransactionRecord;
import engine.descriptor.User;
import rizpa.RizpaFacade;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;

@WebServlet(name = "UserTransactionRecord", urlPatterns = {"/user/transactionsRecord"})
public class UserTransactionRecord extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        String usernameFromSession = SessionUtils.getUsername(request);
        if(usernameFromSession == null) {
            response.setStatus(401);
            response.getOutputStream().println("Error must login before");
        }
        else {
            RizpaFacade rizpaFacade = ServletUtils.getRizpaFacade(getServletContext());
            Collection<TransactionRecord> transactionRecords = rizpaFacade.getUserTransactionsRecord(usernameFromSession);
            response.setStatus(200);
            response.getOutputStream().println(new Gson().toJson(transactionRecords));
        }
    }
}
