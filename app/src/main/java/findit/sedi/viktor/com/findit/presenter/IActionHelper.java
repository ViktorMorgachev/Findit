package findit.sedi.viktor.com.findit.presenter;

import findit.sedi.viktor.com.findit.presenter.interfaces.IAction;

public class IActionHelper implements IAction {
    private static final IActionHelper ourInstance = new IActionHelper();

    public static IActionHelper getInstance() {
        return ourInstance;
    }

    private IAction mIAction;

    private IActionHelper() {
    }

    @Override
    public void action() {

        if (mIAction != null)
            mIAction.action();
        mIAction = null;
    }


    public IAction getIAction() {
        return mIAction;
    }

    public void setIAction(IAction IAction) {
        mIAction = IAction;
    }

}
