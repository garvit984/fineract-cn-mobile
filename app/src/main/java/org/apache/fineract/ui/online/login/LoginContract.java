package org.apache.fineract.ui.online.login;

import org.apache.fineract.data.models.Authentication;
import org.apache.fineract.ui.base.MvpView;

/**
 * @author Rajan Maurya
 *         On 17/06/17.
 */

public interface LoginContract {

    interface View extends MvpView {

        void showUserLoginSuccessfully(Authentication user);

        void showError(String errorMessage);

        void showProgressDialog();

        void hideProgressDialog();
    }

    interface Presenter {

        void login(String username, String password);
    }
}
