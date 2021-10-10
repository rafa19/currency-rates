package rafa.test.currencyrates.di.component;


import android.app.Application;

import javax.inject.Singleton;

import dagger.BindsInstance;
import dagger.Component;
import dagger.android.AndroidInjector;
import dagger.android.support.AndroidSupportInjectionModule;
import dagger.android.support.DaggerApplication;
import rafa.test.currencyrates.base.BaseApplication;
import rafa.test.currencyrates.di.module.ActivityBindingModule;
import rafa.test.currencyrates.di.module.ApplicationModule;
import rafa.test.currencyrates.di.module.ContextModule;
import rafa.test.currencyrates.di.module.RoomModule;

@Singleton
@Component(modules = {RoomModule.class, ContextModule.class, ApplicationModule.class, AndroidSupportInjectionModule.class,
        ActivityBindingModule.class})
public interface ApplicationComponent extends AndroidInjector<DaggerApplication> {

    void inject(BaseApplication application);

    @Component.Builder
    interface Builder {
        @BindsInstance
        Builder application(Application application);


        ApplicationComponent build();
    }
}