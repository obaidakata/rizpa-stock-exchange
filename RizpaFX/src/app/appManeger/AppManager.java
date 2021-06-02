package app.appManeger;

import rizpa.RizpaFacade;

import java.util.function.Consumer;

public class AppManager {
    private static AppManager instance;

    private RizpaFacade rizpaFacade;

    public static AppManager getInstance() {
        if(instance == null)
        {
            instance = new AppManager();
        }
        return instance;
    }

    private Consumer<String> logger;

    public void setLoggerMethod(Consumer<String> onLog) {
        logger = onLog;
    }


    public void log(String toLog) {
        if(logger != null)
        {
            logger.accept(toLog);
        }

    }


    public RizpaFacade getRizpaFacade() {
        if(rizpaFacade == null)
        {
            rizpaFacade = new RizpaFacade();
        }

        return rizpaFacade;
    }
}
