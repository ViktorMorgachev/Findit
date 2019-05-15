package findit.sedi.viktor.com.findit.ui.preloader;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.HashMap;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.common.QrPointManager;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
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

        loadDataFromServer();

    }

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

                    // Пока Фейковые данные загружаем
                    HashMap<Long, Place> placeHashMap = new HashMap<>();
                    Place place;

                   // ServerManager.getInstance().getQrPlaces();
                    // Очередь нам не важна, просто получаем значения и инициализируем пока в наши менеджеры
                    ServerManager.getInstance().getTeams();
                    ServerManager.getInstance().getTournaments();

                   /* place = new Place(new LatLng(42.87592405329111, 74.61399380117655),
                            "История ЦУМа насчитывает вот уже более сорока лет. Это футуристической формы магазин разместил в себе магазинчики с разнообразным товаром от сотовых телефонов, одежды, обуви и косметики до крупной бытовой техники и сувениров.\n" +
                                    "ЦУМ окружен прекрасным архитектурным ансамблем, состоящим из прекрасно ухоженных аллей над которыми свисают старые плакучие ивы с одной стороны, а с другой стороны&nbsp; словно охраняют воды фонтанов брызги которых переливаются в радуге и охлаждают отдыхающих в знойный день.\n" +
                                    "Там же можно найти и кафешки, где можно насладиться чашечкой ароматного кофе или бодрящего чая. Вокруг ЦУМа находится очень много и других достопримечательностей столицы",
                            "Универмаг, располагающийся на бульваре Дзержинского и именуемый местными жителями «Люкс» в 1956 году",
                            getResources().getString(R.string.url_1), 267354, 0, 120, 500);*/


                    // placeHashMap.put(place.getID(), place);


                /*    place = new Place(new LatLng(42.87936940858719, 74.61610838770866), "Это Вечный огонь, бла, бла, бла...", "Вечный огонь",
                            getResources().getString(R.string.url_4), 826442, 0, 56, 1000);*/

                    //    placeHashMap.put(place.getID(), place);

                    mQrPointManager.saveQrPoints(placeHashMap);

                    mDataLoader.post(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PreviewActivity.this, "Данные загруженны, " + placeHashMap.size() + "\n" + "Открытие карты", Toast.LENGTH_LONG).show();
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
