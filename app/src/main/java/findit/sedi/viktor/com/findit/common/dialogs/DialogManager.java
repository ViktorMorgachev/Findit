package findit.sedi.viktor.com.findit.common.dialogs;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;

import androidx.appcompat.app.AlertDialog;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.presenter.NotificatorManager;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;

public class DialogManager {

    private static final DialogManager ourInstance = new DialogManager();


    private Context mContext;
    private AlertDialog mAlertDialog;
    NotificatorManager notificatorManager;

    public static DialogManager getInstance() {
        return ourInstance;
    }

    private DialogManager() {
    }

    public void setContext(Context activity) {
        mContext = activity;
    }

    public void showDialog(String message, String tiitle, IAction iAction, String buttonOk, String buttonCancel, String buttonNeitral, boolean cancelable, boolean showNotification) {


        mAlertDialog = new AlertDialog.Builder(mContext).create();

        if (tiitle != null) {
            mAlertDialog.setTitle(tiitle);
        }

        if (message != null) {
            mAlertDialog.setMessage(message);
        }

        mAlertDialog.setCancelable(cancelable);

        if (buttonOk != null)
            mAlertDialog.setButton(AlertDialog.BUTTON_POSITIVE, buttonOk,
                    (dialog, which) -> {


                        if (iAction != null)
                            iAction.action();

                        dialog.dismiss();
                    });

        if (buttonCancel != null)
            mAlertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, buttonCancel,
                    (dialog, which) -> dialog.dismiss());

        if (showNotification) {
            if (notificatorManager == null)
                notificatorManager = new NotificatorManager();

            notificatorManager.showCompatibilityNotification(mContext.getApplicationContext(), message,
                    R.drawable.ic_tournament_24dp, "CHANNEL_ID", tiitle, mContext.getApplicationContext().getResources().getString(R.string.channel_name),
                    mContext.getApplicationContext().getResources().getString(R.string.channel_descrioption), null);
        }


        mAlertDialog.show();

    }


    public void showBonusDialog() {

        mAlertDialog = new AlertDialog.Builder(mContext).create();

        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.item_input_bonus, null);

        v.findViewById(R.id.et_send_bonus_code).setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        mAlertDialog.setView(v);

        mAlertDialog.show();


    }
}
