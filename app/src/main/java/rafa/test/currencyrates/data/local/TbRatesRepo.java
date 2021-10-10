package rafa.test.currencyrates.data.local;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Maybe;

public class TbRatesRepo {
    TbRatesDao ratesDao;


    @Inject
    public TbRatesRepo(TbRatesDao ratesDao) {
        this.ratesDao = ratesDao;
    }


    public Maybe<List<CurrencyRate>> getRates() {
        return ratesDao.getRates();
    }

    public void insertRates(List<CurrencyRate> rates) {
        deleteRates();
        ratesDao.insertRates((rates));
    }

    public void deleteRates() {
        ratesDao.removeRates();
    }

}
