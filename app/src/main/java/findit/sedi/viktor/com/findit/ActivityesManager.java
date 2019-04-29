package findit.sedi.viktor.com.findit;

// Этот менеджер будет управлять активностями
public class ActivityesManager {
    private static final ActivityesManager ourInstance = new ActivityesManager();

    public static ActivityesManager getInstance() {
        return ourInstance;
    }

    private ActivityesManager() {
    }
}
