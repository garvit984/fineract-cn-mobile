package org.apache.fineract.data.remote;

import android.content.Context;

import org.apache.fineract.FineractApplication;
import org.apache.fineract.data.local.PreferenceKey;
import org.apache.fineract.data.local.PreferencesHelper;

import java.io.IOException;
import java.util.HashSet;

import javax.inject.Inject;

import okhttp3.Interceptor;
import okhttp3.Response;

public class ReceivedCookiesInterceptor implements Interceptor {

    @Inject
    PreferencesHelper preferencesHelper;

    public ReceivedCookiesInterceptor(Context context) {
        FineractApplication.get(context).getComponent().inject(this);
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());

        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();
            for (String header : originalResponse.headers("Set-Cookie")) {
                cookies.add(header);
            }
            preferencesHelper.putStringSet(PreferenceKey.PREF_KEY_COOKIES, cookies);
        }
        return originalResponse;
    }
}