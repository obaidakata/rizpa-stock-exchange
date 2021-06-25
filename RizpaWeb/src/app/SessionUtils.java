package app;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class SessionUtils {

    public static String getUsername(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("username"):  null;
        return sessionAttribute != null ? sessionAttribute.toString() : null;
    }

    public static UserRole getUserRole(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Object sessionAttribute = session != null ? session.getAttribute("userRole"):  null;
        String userRoleAsString = sessionAttribute != null ? sessionAttribute.toString() : "Trader";
        return Enum.valueOf(UserRole.class, userRoleAsString);
    }
}
