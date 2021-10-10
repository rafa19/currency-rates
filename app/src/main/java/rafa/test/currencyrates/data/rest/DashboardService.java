package rafa.test.currencyrates.data.rest;

import io.reactivex.Single;
import rafa.test.currencyrates.data.model.ApiResponse;
import rafa.test.currencyrates.data.model.LatestRatesResponse;
import rafa.test.currencyrates.data.model.SymbolResponse;
import rafa.test.currencyrates.data.model.TimeSeriesResponse;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;

public interface DashboardService {

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("latest")
    Single<LatestRatesResponse> getLatestRates();

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("timeseries")
    Single<TimeSeriesResponse> getRateIntervalByCurrency(@Query("start_date") String startDate,
                                                         @Query("end_date") String endDate, @Query("symbols") String symbols);

    @Headers({"Content-Type:application/json", "Accept:application/json"})
    @GET("symbols")
    Single<SymbolResponse> getSymbolList();

}
