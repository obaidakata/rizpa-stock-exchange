package app;

import app.constant.Constants;
import rizpa.RizpaFacade;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;

public class ServletUtils {

    private static final String RIZPA_FACADE_ATTRIBUTE_NAME = "rizpaFacade";

    private static final Object appManagerLock = new Object();

    public static RizpaFacade getRizpaFacade(ServletContext servletContext) {
        if (servletContext.getAttribute(RIZPA_FACADE_ATTRIBUTE_NAME) == null) {
            synchronized (appManagerLock) {
                if (servletContext.getAttribute(RIZPA_FACADE_ATTRIBUTE_NAME) == null) {
                    servletContext.setAttribute(RIZPA_FACADE_ATTRIBUTE_NAME, new RizpaFacade());
                }
            }
        }

        return (RizpaFacade) servletContext.getAttribute(RIZPA_FACADE_ATTRIBUTE_NAME);
    }

    public static String getUsernameFromSession(HttpSession session) {
        Object usernameAsObject = session.getAttribute(Constants.USERNAME);
        return usernameAsObject instanceof String ? (String) usernameAsObject: null;
    }
}
