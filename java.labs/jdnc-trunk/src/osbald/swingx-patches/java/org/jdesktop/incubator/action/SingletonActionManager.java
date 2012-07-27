package org.jdesktop.incubator.action;

/*
 * Dislike the use of singletons, but they can be convenient lookups for smaller apps
 */

//TODO needs a better name?

public class SingletonActionManager extends ActionManager {
    /**
     * Shared instance of the singleton ActionManager.
     */
    private static ActionManager INSTANCE = new SingletonActionManager();

    /**
     * Return the instance of the ActionManger. If this has not been explicitly
     * set then it will be created.
     *
     * @return the ActionManager instance.
     * @see #setInstance
     */
    public static ActionManager getInstance() {
        return INSTANCE;
    }

    /**
     * Sets the ActionManager instance.
     */
    //TODO oh really? (can of worms)
    public static void setInstance(ActionManager manager) {
        INSTANCE = manager;
    }
}
