package app;

import rizpa.RizpaFacade;

import javax.servlet.ServletContext;

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
}
