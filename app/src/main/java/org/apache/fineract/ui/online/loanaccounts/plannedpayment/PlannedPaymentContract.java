package org.apache.fineract.ui.online.loanaccounts.plannedpayment;

import org.apache.fineract.data.models.payment.PlannedPayment;
import org.apache.fineract.ui.base.MvpView;

import java.util.List;

/**
 * @author Rajan Maurya
 *         On 13/07/17.
 */

public interface PlannedPaymentContract {

    interface View extends MvpView {

        void fetchPlannedPayment();

        void showUserInterface();

        void showPlannedPayment(List<PlannedPayment> plannedPayments);

        void showMorePlannedPayments(List<PlannedPayment> plannedPayments);

        void showEmptyPayments(String message);

        void showRecyclerView(boolean visible);

        void showProgressbar();

        void hideProgressbar();

        void showMessage(String message);
    }

    interface Presenter {

        void fetchPlannedPayment(String productIdentifier, String caseIdentifier,
                Integer pageIndex, String initialDisbursalDate, Boolean loadmore);

        void fetchPlannedPayment(String productIdentifier, String caseIdentifier,
                Integer pageIndex, String initialDisbursalDate);

        void showPlannedPayment(List<PlannedPayment> customers);
    }
}
