package org.apache.fineract.ui.base;

import android.content.Context;

import org.apache.fineract.exceptions.NoConnectivityException;
import org.apache.fineract.utils.MifosErrorUtils;

/**
 * Base class that implements the Presenter interface and provides a base implementation for
 * attachView() and detachView(). It also handles keeping a reference to the mvpView that
 * can be accessed from the children classes by calling getMvpView().
 */
public class BasePresenter<T extends MvpView> implements Presenter<T> {

    protected Context context;
    private T mvpView;

    protected BasePresenter(Context context) {
        this.context = context;
    }

    @Override
    public void attachView(T mvpView) {
        this.mvpView = mvpView;
    }

    @Override
    public void detachView() {
        mvpView = null;
    }

    public void showExceptionError(Throwable throwable, String errorMessage) {
        if (throwable instanceof NoConnectivityException) {
            getMvpView().showNoInternetConnection();
        } else {
            getMvpView().showError(MifosErrorUtils.getFineractError(throwable, errorMessage));
        }
    }

    public boolean isViewAttached() {
        return mvpView != null;
    }

    public T getMvpView() {
        return mvpView;
    }

    public void checkViewAttached() {
        if (!isViewAttached()) throw new MvpViewNotAttachedException();
    }

    public static class MvpViewNotAttachedException extends RuntimeException {
        public MvpViewNotAttachedException() {
            super("Please call Presenter.attachView(MvpView) before" +
                    " requesting data to the Presenter");
        }
    }
}

