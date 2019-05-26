package findit.sedi.viktor.com.findit.ui.find_tainik;

import android.content.Context;
import android.support.v7.app.AppCompatDialog;

// Сюда передадим обьект QrPlace или QrPlace и будем использовать либо инстансы либо интерфейсы необходимые
// в зависимости от типа тайника
public class DiscoveredTainitDialogue extends AppCompatDialog {



    protected DiscoveredTainitDialogue(Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);


    }

}
