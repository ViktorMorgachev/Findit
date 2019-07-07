package findit.sedi.viktor.com.findit.common.dialogs;

import android.content.Context;
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

    public Context getContext() {
        return mContext;
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


}
