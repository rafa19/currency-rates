package rafa.test.currencyrates.di.module;


import dagger.Module;
import dagger.android.ContributesAndroidInjector;
import rafa.test.currencyrates.ui.dashboard.DashboardActivity;
import rafa.test.currencyrates.ui.detail.ChartDetailsActivity;

@Module
public abstract class ActivityBindingModule {

    @ContributesAndroidInjector
    abstract DashboardActivity bindDashboardActivity();

    @ContributesAndroidInjector
    abstract ChartDetailsActivity bindChartDetailsActivity();

}