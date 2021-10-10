package rafa.test.currencyrates.di.module;


import android.content.Context;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import rafa.test.currencyrates.BuildConfig;
import rafa.test.currencyrates.data.rest.DashboardService;
import rafa.test.currencyrates.di.util.AuthenticationInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

@Module(includes = ViewModelModule.class)
public class ApplicationModule {


    @Singleton
    @Provides
    static Retrofit provideRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder().baseUrl(BuildConfig.API_URL)
                .client(okHttpClient)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    @Singleton
    @Provides
    static DashboardService provideDashboardService(Retrofit retrofit) {
        DashboardService dashboardService = retrofit.create(DashboardService.class);
        return dashboardService;
    }

    @Singleton
    @Provides
    HttpLoggingInterceptor provideHttpLoggingInterceptor() {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    @Singleton
    @Provides
    AuthenticationInterceptor provideAuthorizationInterceptor(Context context) {
        return new AuthenticationInterceptor(context);
    }



    @Singleton
    @Provides
    OkHttpClient provideHttpClient(HttpLoggingInterceptor logging, AuthenticationInterceptor auth) {
        return new OkHttpClient.Builder()
                .addInterceptor(logging)
                .addInterceptor(auth)
                .build();
    }
}