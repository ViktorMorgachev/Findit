package findit.sedi.viktor.com.findit.ui.find_tainik;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDialog;
import android.view.View;

import findit.sedi.viktor.com.findit.R;

// Сюда передадим обьект QrPlace или QrPlace и будем использовать либо инстансы либо интерфейсы необходимые
// в зависимости от типа тайника
public class DiscoveredTainitDialogue extends AppCompatActivity {


    private static final String ORDER_ID = "orderId";
    private String mOrderId = "-1";
    public static final int LAYOUT_RES_ID = R.layout.nearby_tainik_layout;

    public static Intent getIntent(Context context, String orderId) {
        Intent intent = new Intent(context, DiscoveredTainitDialogue.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_HISTORY);
        intent.putExtra(ORDER_ID, orderId);
        return intent;
    }


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(LAYOUT_RES_ID);
        readIntentExtras();


    }

    private void readIntentExtras() {
        if (getIntent().getExtras() != null) {
            mOrderId = getIntent().getExtras().getString(ORDER_ID);
        }

    }


}