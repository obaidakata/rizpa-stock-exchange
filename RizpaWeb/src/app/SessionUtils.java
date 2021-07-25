package app;

import app.constant.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = null;
        if(session != null) {
            session.setMaxInactiveInterval(-3);
            sessionAttribute =  session.getAttribute(Constants.USERNAME);
        }

        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static String getStockSymbol(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = null;
        if(session != null) {
            session.setMaxInactiveInterval(-3);
            sessionAttribute =  session.getAttribute(Constants.STOCK_SYMBOL);
        }

        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
}
