package appManeger;

import rizpa.RizpaFacade;

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

    public RizpaFacade getRizpaFacade() {
        if(rizpaFacade == null)
        {
            rizpaFacade = new RizpaFacade();
        }

        return rizpaFacade;
    }
}
