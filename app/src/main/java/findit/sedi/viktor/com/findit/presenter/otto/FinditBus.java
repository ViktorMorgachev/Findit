package findit.sedi.viktor.com.findit.presenter.otto;

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;


public class FinditBus {
    private static FinditBus mThis;
    private Bus mBus = new Bus();
    private final Handler mHandler = new Handler(Looper.getMainLooper());

    public static FinditBus getInstance() {
        if (mThis == null)
            mThis = new FinditBus();
        return mThis;
    }

    public void register(Object o) {
        mBus.register(o);
    }

    public void unregister(Object o) {
        try {
            mBus.unregister(o);
        } catch (Exception e) {

        }
    }

    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            mBus.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    mBus.post(event);
                }
            });
        }
    }
}
