package rafa.test.currencyrates.data.model;

import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.Map;

public class LatestRatesResponse extends ApiResponse{


    @SerializedName("rates")
    @Expose
    public Map<String,Double> rates;

    public LatestRatesResponse(Status status, @Nullable Object data, @Nullable Throwable error) {
        super(status, data, error);
    }

    public Map<String, Double> getRates() {
        return rates;
    }

    public void setRates(Map<String, Double> rates) {
        this.rates = rates;
    }

}
