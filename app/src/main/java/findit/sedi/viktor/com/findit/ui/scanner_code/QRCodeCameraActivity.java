package findit.sedi.viktor.com.findit.ui.scanner_code;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.BuildConfig;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.barcode.Barcode;
import com.google.android.gms.vision.barcode.BarcodeDetector;

import java.io.File;
import java.io.FileNotFoundException;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.interactors.KeyCommonSettings;

import static findit.sedi.viktor.com.findit.interactors.KeyCommonSettings.KeysField.LOG_TAG;

public class QRCodeCameraActivity extends AppCompatActivity {

    // Views
    private Button btnOpenCamera;

    private BarcodeDetector detector;
    private Uri imageUri;
    private static final int REQUEST_CAMERA_PERMISSION = 200;
    private static final int CAMERA_REQUEST = 101;
    private static final String SAVED_INSTANCE_URI = "uri";
    private static final String SAVED_INSTANCE_RESULT = "result";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA}, REQUEST_CAMERA_PERMISSION);

    }

    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    takeBarcodePicture();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission Denied!", Toast.LENGTH_SHORT).show();
                }
        }
    }

    private void takeBarcodePicture() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        File photo = new File(Environment.getExternalStorageDirectory(), "pic.jpg");

        Uri uri;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            uri = FileProvider.getUriForFile(getApplicationContext(),
                    getPackageName() + ".provider", photo);
        } else {
            uri = Uri.fromFile(photo);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, CAMERA_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            launchMediaScanIntent();
            try {
                Bitmap bitmap = decodeBitmapUri(this, imageUri);
                if (detector.isOperational() && bitmap != null) {
                    Frame frame = new Frame.Builder().setBitmap(bitmap).build();
                    SparseArray<Barcode> barcodes = detector.detect(frame);
                    for (int index = 0; index < barcodes.size(); index++) {
                        Barcode code = barcodes.valueAt(index);
                        Toast.makeText(this, code.displayValue, Toast.LENGTH_LONG).show();

                        int type = barcodes.valueAt(index).valueFormat;
                        switch (type) {
                            case Barcode.CONTACT_INFO:
                                Log.i(LOG_TAG, code.contactInfo.title);
                                break;
                            case Barcode.EMAIL:
                                Log.i(LOG_TAG, code.displayValue);
                                break;
                            case Barcode.ISBN:
                                Log.i(LOG_TAG, code.rawValue);
                                break;
                            case Barcode.PHONE:
                                Log.i(LOG_TAG, code.phone.number);
                                break;
                            case Barcode.PRODUCT:
                                Log.i(LOG_TAG, code.rawValue);
                                break;
                            case Barcode.SMS:
                                Log.i(LOG_TAG, code.sms.message);
                                break;
                            case Barcode.TEXT:
                                Log.i(LOG_TAG, code.displayValue);
                                break;
                            case Barcode.URL:
                                Log.i(LOG_TAG, "url: " + code.displayValue);
                                break;
                            case Barcode.WIFI:
                                Log.i(LOG_TAG, code.wifi.ssid);
                                break;
                            case Barcode.GEO:
                                Log.i(LOG_TAG, code.geoPoint.lat + ":" + code.geoPoint.lng);
                                break;
                            case Barcode.CALENDAR_EVENT:
                                Log.i(LOG_TAG, code.calendarEvent.description);
                                break;
                            case Barcode.DRIVER_LICENSE:
                                Log.i(LOG_TAG, code.driverLicense.licenseNumber);
                                break;
                            default:
                                Log.i(LOG_TAG, code.rawValue);
                                break;
                        }
                    }
                    if (barcodes.size() == 0) {
                        Toast.makeText(this, "No barcode could be detected. Please try again.", Toast.LENGTH_LONG).show();

                    }
                } else {

                    Toast.makeText(this, "Detector initialisation failed", Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                Toast.makeText(this, "Failed to load Image", Toast.LENGTH_SHORT).show();
                Log.e(LOG_TAG, e.toString());
            }
        }
    }

    private void launchMediaScanIntent() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        mediaScanIntent.setData(imageUri);
        this.sendBroadcast(mediaScanIntent);
    }

    private Bitmap decodeBitmapUri(Context ctx, Uri uri) throws FileNotFoundException {
        int targetW = 600;
        int targetH = 600;
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(ctx.getContentResolver().openInputStream(uri), null, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        int scaleFactor = Math.min(photoW / targetW, photoH / targetH);
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;

        return BitmapFactory.decodeStream(ctx.getContentResolver()
                .openInputStream(uri), null, bmOptions);
    }
}
