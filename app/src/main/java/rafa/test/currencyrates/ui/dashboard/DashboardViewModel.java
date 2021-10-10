package rafa.test.currencyrates.ui.dashboard;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.LinkedTreeMap;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import io.reactivex.Completable;
import io.reactivex.CompletableObserver;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import rafa.test.currencyrates.data.local.CurrencyRate;
import rafa.test.currencyrates.data.local.Symbols;
import rafa.test.currencyrates.data.local.TbRatesRepo;
import rafa.test.currencyrates.data.model.ApiResponse;
import rafa.test.currencyrates.data.model.LatestRatesResponse;
import rafa.test.currencyrates.data.model.SymbolResponse;
import rafa.test.currencyrates.data.model.TimeSeriesResponse;
import rafa.test.currencyrates.data.rest.DashboardRepo;

public class DashboardViewModel extends ViewModel {

    private final DashboardRepo repo;
    private final TbRatesRepo localRepo;
    private CompositeDisposable disposable;
    private final MutableLiveData<ApiResponse> latestRatesResponse = new MutableLiveData<>();
    private final MutableLiveData<ApiResponse> timeSeriesResponse = new MutableLiveData<>();

    private Map<String, String> symbolList = new HashMap<>();

    @Inject
    public DashboardViewModel(DashboardRepo repo, TbRatesRepo localRepo) {
        this.repo = repo;
        this.localRepo = localRepo;
        disposable = new CompositeDisposable();
    }


    public MutableLiveData<ApiResponse> getLatestRatesResponse() {
        return latestRatesResponse;
    }

    public void getSymbolsAndRates() {
        getSymbolList();
    }

    public void getLatestRates() {
        disposable.add(repo.getLatestRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> latestRatesResponse.setValue(ApiResponse.loading()))
                .subscribe(
                        this::storeInLocal,
                        throwable -> latestRatesResponse.setValue(ApiResponse.error(throwable))
                ));
    }


    public MutableLiveData<ApiResponse> getTimeSeriesResponse() {
        return timeSeriesResponse;
    }


    public void getRateIntervalByCurrency(String startDate, String endDate, String currency) {
        disposable.add(repo.getRateIntervalByCurrency(startDate, endDate, currency)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> timeSeriesResponse.setValue(ApiResponse.loading()))
                .subscribe(
                        response -> {
                            timeSeriesResponse.setValue(ApiResponse.success(response.rates));
                        },
                        throwable -> timeSeriesResponse.setValue(ApiResponse.error(throwable))
                ));
    }

    public void getSymbolList() {
        disposable.add(repo.getSymbolList()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .doOnSubscribe((d) -> latestRatesResponse.setValue(ApiResponse.loading()))
                .subscribe(
                        response -> {
                            symbolList = parseSymbols(response);
                            getLatestRates();
                        },
                        throwable -> latestRatesResponse.setValue(ApiResponse.error(throwable))
                ));
    }

    private void storeInLocal(LatestRatesResponse response) {
        Completable.fromAction(() -> localRepo.insertRates(parseCurrencyRates(response)))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new CompletableObserver() {
                    @Override
                    public void onSubscribe(Disposable d) { }

                    @Override
                    public void onComplete() {
                        getLocalRates();
                    }

                    @Override
                    public void onError(Throwable e) {
                        latestRatesResponse.setValue(ApiResponse.error(e));
                    }
                });
    }

    private List<CurrencyRate> parseCurrencyRates(LatestRatesResponse response) {
        List<CurrencyRate> result = new ArrayList<>();
        for (Map.Entry<String, Double> entry : response.rates.entrySet()) {
            String key = entry.getKey();
            Double value = entry.getValue();
            String details = symbolList.get(key);
            result.add(new CurrencyRate(key, value, details));
        }

        return result;
    }

    private Map<String, String> parseSymbols(SymbolResponse response) {
        Map<String, String> result = new HashMap<>();

        if (response.symbols != null)
            for (Map.Entry<String, Symbols> entry : response.symbols.entrySet()) {
                Symbols item = entry.getValue();
                result.put(item.getCode(), item.getDescription());
            }

        return result;
    }

    public void getLocalRates() {
        disposable.add(localRepo.getRates()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        result -> {
                            latestRatesResponse.setValue(ApiResponse.success(result));
                        },
                        throwable -> latestRatesResponse.setValue(ApiResponse.error(throwable))
                ));
    }


    @Override
    protected void onCleared() {
        super.onCleared();
        if (disposable != null) {
            disposable.clear();
            disposable = null;
        }
    }
}
