package findit.sedi.viktor.com.findit.presenter;

import android.content.Context;
import android.os.Handler;
import android.widget.ProgressBar;
import findit.sedi.viktor.com.findit.R;

public class ProgressLoder {
    private static final ProgressLoder ourInstance = new ProgressLoder();

    public static ProgressLoder getInstance() {
        return ourInstance;
    }


    private Handler mProgressHandler = new Handler();
    private Thread mThread;
    private boolean interrupted;

    private ProgressLoder() {
    }

    public void showingProgress(Context context) {

        ProgressBar mProgressBar = new ProgressBar(context);
        mProgressBar.setIndeterminate(true);
        mProgressBar.setIndeterminateDrawable(context.getResources().getDrawable(R.drawable.blue));

        if (mThread == null) {
            mThread = new Thread(new Runnable() {
                public void run() {
                    while (!interrupted) {
                        final int[] rotation = {0};
                        mProgressHandler.post(new Runnable() {
                            public void run() {
                                if (rotation[0] == 360)
                                    rotation[0] = 0;
                                mProgressBar.setRotation(rotation[0]);
                                rotation[0]++;
                            }
                        });
                        try {
                            // Sleep for 200 milliseconds.
                            Thread.sleep(200);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            });

            mThread.start();
        } else if (!mThread.isAlive()) mThread.start();
    }

    public void breakProgress(boolean really){
        interrupted = really;
    }
}
