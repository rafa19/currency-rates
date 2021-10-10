package rafa.test.currencyrates.di.util;


import android.content.Context;
import java.io.IOException;
import javax.inject.Singleton;
import okhttp3.HttpUrl;
import rafa.test.currencyrates.BuildConfig;
import rafa.test.currencyrates.utils.CommonUtils;
import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;
import rafa.test.currencyrates.utils.NoConnectivityException;

@Singleton
public class AuthenticationInterceptor implements Interceptor {


    Context context;


    public AuthenticationInterceptor(Context context) {
        this.context = context;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {

        if (!CommonUtils.isNetworkAvailable(context)) {
            throw new NoConnectivityException();
        }

        try {
            HttpUrl url = chain.request()
                    .url()
                    .newBuilder()
                    .addQueryParameter("access_key", BuildConfig.ACCESS_KEY)
                    .addQueryParameter("base", "USD")
                    .build();

            Request request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build();
            return chain.proceed(request);

        } catch (Exception ex) {
            ex.printStackTrace();
            return chain.proceed(chain.request());
        }
    }


}