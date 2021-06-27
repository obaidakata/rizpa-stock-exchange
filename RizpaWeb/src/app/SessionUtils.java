package app;

import app.constant.Constants;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        System.out.println(session);
        Object sessionAttribute = session != null ? session.getAttribute("username"):  null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }
}
