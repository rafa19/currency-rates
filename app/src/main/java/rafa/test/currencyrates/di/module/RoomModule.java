package rafa.test.currencyrates.di.module;


import android.app.Application;


import androidx.room.Room;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import rafa.test.currencyrates.data.local.AppDatabase;
import rafa.test.currencyrates.data.local.TbRatesDao;
import rafa.test.currencyrates.data.local.TbRatesRepo;

@Module(includes = ViewModelModule.class)
public class RoomModule {

    private AppDatabase database;

    @Singleton
    @Provides
    AppDatabase providesRoomDatabase(Application mApplication) {

        database = Room.databaseBuilder(mApplication, AppDatabase.class, "CurrencyDatabase")
                .build();
        return database;
    }

    @Singleton
    @Provides
    TbRatesDao provideTbRatesDao(AppDatabase database) {
        return database.getTbRatesDao();
    }

    @Singleton
    @Provides
    TbRatesRepo provideTbRatesRepo(TbRatesDao ratesDao) {
        return new TbRatesRepo(ratesDao);
    }

}
