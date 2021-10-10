package rafa.test.currencyrates.data.local;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.RoomWarnings;

import java.util.List;

import io.reactivex.Maybe;

@Dao
public interface TbRatesDao {

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("SELECT * FROM CurrencyRate")
    Maybe<List<CurrencyRate>> getRates();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertRates(List<CurrencyRate> rates);

    @Query("Delete from CurrencyRate")
    void removeRates();
}
