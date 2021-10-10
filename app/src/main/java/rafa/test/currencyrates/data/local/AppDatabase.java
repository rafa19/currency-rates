package rafa.test.currencyrates.data.local;


import androidx.room.Database;
import androidx.room.RoomDatabase;


@Database(entities = {CurrencyRate.class}, version = AppDatabase.VERSION, exportSchema = false)

public abstract class AppDatabase extends RoomDatabase {

    static final int VERSION = 1;

    public abstract TbRatesDao getTbRatesDao();


}