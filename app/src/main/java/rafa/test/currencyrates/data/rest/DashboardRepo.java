package rafa.test.currencyrates.data.rest;

import javax.inject.Inject;

import io.reactivex.Single;
import rafa.test.currencyrates.data.model.ApiResponse;
import rafa.test.currencyrates.data.model.LatestRatesResponse;
import rafa.test.currencyrates.data.model.SymbolResponse;
import rafa.test.currencyrates.data.model.TimeSeriesResponse;

public class DashboardRepo {

    private final DashboardService dashboardService;

    @Inject
    public DashboardRepo(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    public Single<LatestRatesResponse> getLatestRates() {
        return dashboardService.getLatestRates();
    }

    public Single<TimeSeriesResponse> getRateIntervalByCurrency(String startDate, String endDate, String currency) {
        return dashboardService.getRateIntervalByCurrency(startDate, endDate, currency);
    }

    public Single<SymbolResponse> getSymbolList() {
        return dashboardService.getSymbolList();
    }
}
