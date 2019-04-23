package findit.sedi.viktor.com.findit.data.cloud.myserver;

import android.widget.Toast;

public class ServerManager {
    private static final ServerManager ourInstance = new ServerManager();

    public static ServerManager getInstance() {
        return ourInstance;
    }

    private ServerManager() {
    }


    public void sendCode(String code) {

    }
}
