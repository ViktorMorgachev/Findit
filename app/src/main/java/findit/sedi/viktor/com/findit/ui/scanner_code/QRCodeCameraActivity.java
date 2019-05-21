package findit.sedi.viktor.com.findit.ui.scanner_code;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.IOException;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.common.ManagersFactory;
import findit.sedi.viktor.com.findit.data_providers.cloud.myserver.ServerManager;
import findit.sedi.viktor.com.findit.interactors.KeyCommonSettings;

public class QRCodeCameraActivity extends AppCompatActivity {
    // Views
    private CameraSource mCameraSource;
    private BarcodeDetector mBarcodeDetector;
    private SurfaceView surfaceView;
    TextView txtBarcodeValue;
    private static final int REQUEST_CAMERA_PERMISSION = 201;
    Button btnAction;
    String code = "";
    String intentData = "";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);


        initUI();


        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);


    }

    private void initUI() {

        setContentView(R.layout.qr_camera_layout);

        txtBarcodeValue = findViewById(R.id.txtBarcodeValue);
        surfaceView = findViewById(R.id.surfaceView);
        btnAction = findViewById(R.id.btnAction);
        btnAction.setEnabled(false);
        surfaceView.setZOrderMediaOverlay(true);

        btnAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(QRCodeCameraActivity.this, "Вы получили  " +
                        ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(code).getBonus() + " бонусов", Toast.LENGTH_LONG).show();
                ServerManager.getInstance().sendCode(code, "fond");

                // Если маркер одноразовый, то обнуляем значение
                if (!ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(code).isReusable())
                {
                    ServerManager.getInstance().resetQrPlaceBonus(code);
                }

                ManagersFactory.getInstance().getAccountManager().getUser().setBonus(ManagersFactory.getInstance().getQrPointManager().getQrPlaceByID(code).getBonus());
                ServerManager.getInstance().updateUserOnServer("bonus");
                QRCodeCameraActivity.this.onBackPressed();
            }
        });

    }

    private void initialiseDetectorsAndSources() {


        // This process is dedicated to LeakCanary for heap analysis.
        // You should not init your app in this process.


        mBarcodeDetector = new BarcodeDetector.Builder(this)
                .setBarcodeFormats(Barcode.ALL_FORMATS)
                .build();


        try {
            if (!mBarcodeDetector.isOperational()) {
                Toast.makeText(getApplicationContext(), "Не найдён детектор", Toast.LENGTH_LONG).show();
                return;
            }
        } catch (Exception e) {
            Toast.makeText(this, "Обновите Google Play Services!", Toast.LENGTH_LONG).show();
        }

        mCameraSource = new CameraSource.Builder(this, mBarcodeDetector)
                .setRequestedPreviewSize(1920, 1080)
                .setAutoFocusEnabled(true)
                .setRequestedFps(24)
                .build();

        surfaceView.getHolder().addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                try {
                    if (ActivityCompat.checkSelfPermission(QRCodeCameraActivity.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                        mCameraSource.start(holder);
                    } else {
                        ActivityCompat.requestPermissions(QRCodeCameraActivity.this, new
                                String[]{Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                //   Toast.makeText(getApplicationContext(), "Surface was destroed", Toast.LENGTH_SHORT).show();

                if (mBarcodeDetector != null) {
                    mBarcodeDetector.release();
                    mBarcodeDetector = null;
                }

            }
        });


        mBarcodeDetector.setProcessor(new Detector.Processor<Barcode>() {
            @Override
            public void release() {
                // Toast.makeText(getApplicationContext(), "To prevent memory leaks barcode scanner has been stopped", Toast.LENGTH_SHORT).show();

                mBarcodeDetector = null;
                mCameraSource = null;

            }

            // Code working from other thread
            @Override
            public void receiveDetections(Detector.Detections<Barcode> detections) {
                final SparseArray<Barcode> barcodes = detections.getDetectedItems();
                if (barcodes.size() != 0) {

                    txtBarcodeValue.post(new Runnable() {

                        @Override
                        public void run() {

                            txtBarcodeValue.setText(intentData);

                            if (barcodes.valueAt(0).url != null) {
                                txtBarcodeValue.removeCallbacks(null);
                                intentData = barcodes.valueAt(0).url.url;
                                txtBarcodeValue.setText(intentData);
                                btnAction.setEnabled(true);
                            } else {
                                btnAction.setEnabled(true);
                                intentData = barcodes.valueAt(0).displayValue;
                                txtBarcodeValue.setText(intentData);

                            }

                            code = barcodes.valueAt(0).displayValue;

                        }
                    });


                }

            }
        });

    }


    @Override
    protected void onResume() {
        super.onResume();
        initialiseDetectorsAndSources();
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(KeyCommonSettings.KeysField.LOG_TAG, "Activity was paused()");

    }

    @Override
    protected void onStop() {
        super.onStop();
        if (mCameraSource != null) {
            mCameraSource.release();
            mCameraSource = null;
        }
        Log.d(KeyCommonSettings.KeysField.LOG_TAG, "Activity was stoped()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCameraSource != null) {
            mCameraSource.release();
            mCameraSource = null;
        }
        Log.d(KeyCommonSettings.KeysField.LOG_TAG, "Activity was destroyed()");
    }
}




