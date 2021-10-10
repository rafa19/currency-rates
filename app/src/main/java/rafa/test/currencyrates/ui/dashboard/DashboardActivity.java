package rafa.test.currencyrates.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import javax.inject.Inject;

import rafa.test.currencyrates.R;
import rafa.test.currencyrates.base.BaseActivity;
import rafa.test.currencyrates.data.local.CurrencyRate;
import rafa.test.currencyrates.data.model.ApiResponse;
import rafa.test.currencyrates.data.model.LatestRatesResponse;
import rafa.test.currencyrates.databinding.ActivityDashboardBinding;
import rafa.test.currencyrates.ui.detail.ChartDetailsActivity;
import rafa.test.currencyrates.ui.listener.ItemClickListener;
import rafa.test.currencyrates.utils.CommonUtils;
import rafa.test.currencyrates.utils.PreferenceUtil;
import rafa.test.currencyrates.utils.ViewModelFactory;

public class DashboardActivity extends BaseActivity implements ItemClickListener {


    private ArrayList<CurrencyRate> mCurrencyList = new ArrayList<>();
    ActivityDashboardBinding binding;
    CurrenciesAdapter mCurrencyAdapter;
    @Inject
    ViewModelFactory viewModelFactory;
    private DashboardViewModel dashboardViewModel;
    long currentTime;

    @Override
    protected int layoutRes() {
        return R.layout.activity_dashboard;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDashboardBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        dashboardViewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel.class);
        dashboardViewModel.getLatestRatesResponse().observe(this, this::consumeLatestRatesResponse);

        mCurrencyAdapter = new CurrenciesAdapter(new ArrayList<>());
        mCurrencyAdapter.setClickListener(this);
        binding.recyclerView.setAdapter(mCurrencyAdapter);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(DashboardActivity.this, LinearLayoutManager.VERTICAL, false));

        long lastRequestTime = PreferenceUtil.getLastRequestTime(this);

        currentTime = Calendar.getInstance().getTime().getTime();
        if (currentTime > 0 && (currentTime - lastRequestTime > 600000)) {
            dashboardViewModel.getSymbolsAndRates();
        } else {
            showToast("You are viewing cached data");
            dashboardViewModel.getLocalRates();
        }

        binding.txtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                mCurrencyAdapter.getFilter().filter(binding.txtSearch.getText().toString().trim());
            }
        });
    }


    private void consumeLatestRatesResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                showLoading();
                break;

            case SUCCESS:
                hideLoading();
                if (apiResponse.data != null) {
                    mCurrencyList = (ArrayList<CurrencyRate>) apiResponse.data;
                    mCurrencyAdapter.setRateList(mCurrencyList);
                    PreferenceUtil.saveRequestTime(currentTime, DashboardActivity.this);
                }
                break;

            case ERROR:
                hideLoading();
                try {
                    handleError(apiResponse.error);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onItemClick(String currency) {
        Intent intent = new Intent(this, ChartDetailsActivity.class);
        intent.putExtra("currency", currency);
        startActivity(intent);
    }
}