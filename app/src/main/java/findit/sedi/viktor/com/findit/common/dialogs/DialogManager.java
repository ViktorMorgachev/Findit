package findit.sedi.viktor.com.findit.common.dialogs;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import findit.sedi.viktor.com.findit.R;
import findit.sedi.viktor.com.findit.presenter.NotificatorManager;
import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;

public class DialogManager {

    private static final DialogManager ourInstance = new DialogManager();


    private Activity mActivity;
    private AlertDialog mAlertDialog;
    NotificatorManager notificatorManager;

    public static DialogManager getInstance() {
        return ourInstance;
    }

    private DialogManager() {
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void setActivity(Activity activity) {
        mActivity = activity;
    }

    public void showDialog(String message, String tiitle, IAction iAction, String buttonOk, String buttonCancel, String buttonNeitral, boolean cancelable, boolean showNotification) {


        mAlertDialog = new AlertDialog.Builder(mActivity).create();

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

            notificatorManager.showCompatibilityNotification(mActivity.getApplicationContext(), message,
                    R.drawable.ic_tournament_24dp, "CHANNEL_ID", tiitle, mActivity.getApplicationContext().getResources().getString(R.string.channel_name),
                    mActivity.getApplicationContext().getResources().getString(R.string.channel_descrioption), null);
        }

        mAlertDialog.show();

    }

    public void showDialog(Activity activity, String message, String tiitle, IAction iAction) {

    }
}
