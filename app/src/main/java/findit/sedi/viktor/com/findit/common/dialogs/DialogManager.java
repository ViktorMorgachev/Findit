package findit.sedi.viktor.com.findit.common.dialogs;

import android.app.Activity;
import android.support.v7.app.AlertDialog;

import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;

public class DialogManager {

    private static final DialogManager ourInstance = new DialogManager();


    private Activity mActivity;
    private AlertDialog mAlertDialog;

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

    public void showDialog(String message, String tiitle, IAction iAction, String buttonOk, String buttonCancel, String buttonNeitral, boolean cancelable) {


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

        mAlertDialog.show();

    }

    public void showDialog(Activity activity, String message, String tiitle, IAction iAction) {

    }
}
