package rafa.test.currencyrates.data.model;

import static rafa.test.currencyrates.data.model.Status.ERROR;
import static rafa.test.currencyrates.data.model.Status.LOADING;
import static rafa.test.currencyrates.data.model.Status.SUCCESS;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;



public class ApiResponse {


    @SerializedName("apiStatus")
    @Expose
    public Status status;

    @Nullable
    public Object data;

    @Nullable
    public Throwable error;

    public ApiResponse() {
    }

    public ApiResponse(Status status, @Nullable Object data, @Nullable Throwable error) {
        this.status = status;
        this.data = data;
        this.error = error;
    }

    public static ApiResponse loading() {
        return new ApiResponse(LOADING, null, null);
    }

    public static ApiResponse success(@NonNull Object data) {
        return new ApiResponse(SUCCESS, data, null);
    }

    public static ApiResponse error(@NonNull Throwable error) {
        return new ApiResponse(ERROR, null, error);
    }
}
