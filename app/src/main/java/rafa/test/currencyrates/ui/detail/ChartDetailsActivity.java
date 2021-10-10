package rafa.test.currencyrates.ui.detail;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModelProviders;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import rafa.test.currencyrates.R;
import rafa.test.currencyrates.base.BaseActivity;
import rafa.test.currencyrates.data.model.ApiResponse;
import rafa.test.currencyrates.databinding.ActivityChartDetailsBinding;
import rafa.test.currencyrates.ui.dashboard.DashboardViewModel;
import rafa.test.currencyrates.utils.CommonUtils;
import rafa.test.currencyrates.utils.ViewModelFactory;

public class ChartDetailsActivity extends BaseActivity {

    @Inject
    ViewModelFactory viewModelFactory;
    private DashboardViewModel dashboardViewModel;
    ActivityChartDetailsBinding binding;
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Calendar cal = Calendar.getInstance();
    String currency;
    double highest = 0.0, lowest = 0.0;

    @Override
    protected int layoutRes() {
        return R.layout.activity_chart_details;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChartDetailsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        setSupportActionBar(binding.toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        Intent intent = getIntent();
        currency = intent.getStringExtra("currency");

        dashboardViewModel = ViewModelProviders.of(this, viewModelFactory).get(DashboardViewModel.class);
        dashboardViewModel.getTimeSeriesResponse().observe(this, this::consumeTimeSeriesResponse);

        String endDate = getFormattedDate(cal.getTimeInMillis());
        cal.add(Calendar.DAY_OF_YEAR, -6);//last 7 days including today
        String startDate = getFormattedDate(cal.getTimeInMillis());

        dashboardViewModel.getRateIntervalByCurrency(startDate, endDate, currency);


    }

    private String getFormattedDate(long date) {
        return simpleDateFormat.format(new Date(date));
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    private void bindData(Map<String, Map<String, Double>> response) {
        binding.lytContent.setVisibility(View.VISIBLE);
        binding.txtEmpty.setVisibility(View.GONE);
        binding.chart.setInteractive(false);
        binding.chart.setLineChartData(populateChartData(response));
        binding.txtLowest.setText(CommonUtils.formatMoney(lowest));
        binding.txtHighest.setText(CommonUtils.formatMoney(highest));
        binding.lblRate.setText(String.format("%s%s", currency, getString(R.string.usd_pair_last_7_days)));
    }

    private LineChartData populateChartData(Map<String, Map<String, Double>> response) {

        highest = 0.0;
        lowest = 0.0;
        List<PointValue> values = new ArrayList<PointValue>();
        PointValue pointValue;
        int xIndex = 0;
        for (Map.Entry<String, Map<String, Double>> ratesEntry : response.entrySet()) {
            if (ratesEntry.getValue() != null && !ratesEntry.getValue().isEmpty()) {
                Map.Entry<String, Double> itemEntry = ratesEntry.getValue().entrySet().iterator().next();
                Double value = itemEntry.getValue();
                if (value > highest) {
                    highest = value;
                }
                if (value < lowest || lowest == 0.0) {
                    lowest = value;
                }
                pointValue = new PointValue(xIndex, value.floatValue());
                pointValue.setLabel(CommonUtils.formatMoney(itemEntry.getValue()) + "$");
                values.add(pointValue);

                xIndex++;
            }
        }

        Line line = new Line(values).setPointColor(Color.BLUE).setColor(Color.CYAN).setCubic(true);
        line.setHasLines(true);
        line.setHasLabels(true);
        List<Line> lines = new ArrayList<Line>();
        lines.add(line);
        LineChartData data = new LineChartData();
        data.setLines(lines);
        return data;
    }

    private void dataNotFound() {
        binding.lytContent.setVisibility(View.GONE);
        binding.txtEmpty.setVisibility(View.VISIBLE);
        showDialog(getString(R.string.no_rate_awailable));
    }


    @SuppressWarnings("unchecked")
    private void consumeTimeSeriesResponse(ApiResponse apiResponse) {
        switch (apiResponse.status) {

            case LOADING:
                showLoading();
                break;

            case SUCCESS:
                hideLoading();
                if (apiResponse.data != null) {
                    Map<String, Map<String, Double>> response = (Map<String, Map<String, Double>>) apiResponse.data;
                    if (!response.isEmpty()) {
                        bindData(response);
                    } else {
                        dataNotFound();
                    }

                } else {
                    dataNotFound();
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
}