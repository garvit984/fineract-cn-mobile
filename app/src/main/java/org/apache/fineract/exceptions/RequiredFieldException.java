package org.apache.fineract.exceptions;

import android.content.Context;
import android.widget.Toast;

public class RequiredFieldException extends Exception {

    private String fieldName;
    private String localisedErrorMessage;

    public RequiredFieldException(String fieldName, String localisedErrorMessage) {

        this.fieldName = fieldName;
        this.localisedErrorMessage = localisedErrorMessage;
    }

    @Override
    public String toString() {
        return fieldName + " " + localisedErrorMessage;
    }

    public void notifyUserWithToast(Context context) {
        Toast.makeText(context, toString(), Toast.LENGTH_SHORT).show();
    }
}
