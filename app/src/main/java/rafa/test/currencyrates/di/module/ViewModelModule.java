package rafa.test.currencyrates.di.module;


import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import dagger.Binds;
import dagger.Module;
import dagger.multibindings.IntoMap;
import rafa.test.currencyrates.di.util.ViewModelKey;
import rafa.test.currencyrates.ui.dashboard.DashboardViewModel;
import rafa.test.currencyrates.utils.ViewModelFactory;

@Module
public abstract class ViewModelModule {


    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel.class)
    abstract ViewModel bindDashboardViewModel(DashboardViewModel viewModel);

    @Binds
    abstract ViewModelProvider.Factory bindViewModelFactory(ViewModelFactory factory);
}