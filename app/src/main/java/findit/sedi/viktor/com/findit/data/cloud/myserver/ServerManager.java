package findit.sedi.viktor.com.findit.data.cloud.myserver;

public class ServerManager {
    private static final ServerManager ourInstance = new ServerManager();

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
    }


}
