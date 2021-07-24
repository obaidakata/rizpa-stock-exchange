package app;

import app.constant.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.USERNAME):  null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getStockSymbol(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute(Constants.STOCK_SYMBOL):  null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
}
