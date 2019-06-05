package findit.sedi.viktor.com.findit.ui.preloader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.common.QrPointManager;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.data_providers.data.QrPoint;
import findit.sedi.viktor.com.findit.presenter.ProgressLoder;
import findit.sedi.viktor.com.findit.ui.main.MainActivity;
import pl.droidsonroids.gif.GifImageView;

public class PreviewActivity extends Activity {


    private Handler mDataLoader = new Handler();
    private Thread mThread;
    private QrPointManager mQrPointManager;

    private GifImageView mGifImageView;
    private ProgressLoder mProgressLoder;

    // View

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ManagersFactory.getInstance().setContext(this);

        mQrPointManager = ManagersFactory.getInstance().getQrPointManager();

        setContentView(R.layout.preview_layout);

        mGifImageView = findViewById(R.id.gif_image);
        mGifImageView.setImageResource(R.drawable.loader_spinner_1s_200px);
        mGifImageView.animate();

        loadDataFromServer();

    }

    // Нужно будет по максимуму всё получить с сервера
    // Самую основную информацию кроме самого пользователя
    private void loadDataFromServer() {

        // Защита, чтобы много потоков не пошло в сеть загружать данные
        if (mThread == null || !mThread.isAlive()) {
            mThread = new Thread(new Runnable() {


                @Override
                public void run() {


                    if (mThread.isInterrupted()) {
                        ProgressLoder.getInstance().breakProgress(true);
                        return;
                    }


                    Thread getTeams = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServerManager.getInstance().getTeams();
                        }
                    });

                    Thread getTournaments = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServerManager.getInstance().getTournaments();
                        }
                    });

                    Thread getPlayers = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServerManager.getInstance().getPlayers();
                        }
                    });

                    Thread getQrPoints = new Thread(new Runnable() {
                        @Override
                        public void run() {
                            ServerManager.getInstance().getQrPlaces();
                        }
                    });

                    // ServerManager.getInstance().getQrPlaces();
                    // Очередь нам не важна, просто получаем значения и инициализируем пока в наши менеджеры
                    getTeams.start();
                    getTournaments.start();
                    getQrPoints.start();
                    getPlayers.start();


                    try {
                        getPlayers.join();
                        getQrPoints.join();
                        getTeams.join();
                        getTournaments.join();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    mDataLoader.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PreviewActivity.this, "Данные загруженны, " + ManagersFactory.getInstance().getQrPointManager().getQrPlaces().size() + "\n" + "Открытие карты", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(PreviewActivity.this, MainActivity.class));

                        }
                    });


                }
            });
            mThread.start();
        }

    }


    @Override
    protected void onResume() {
        super.onResume();
    }


}
