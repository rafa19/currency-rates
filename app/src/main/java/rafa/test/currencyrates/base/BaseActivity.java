package rafa.test.currencyrates.base;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;


import java.io.IOException;
import java.net.SocketTimeoutException;

import dagger.android.support.DaggerAppCompatActivity;
import rafa.test.currencyrates.R;
import rafa.test.currencyrates.utils.CommonUtils;
import rafa.test.currencyrates.utils.NoConnectivityException;
import retrofit2.HttpException;

public abstract class BaseActivity extends DaggerAppCompatActivity {

    @LayoutRes
    protected abstract int layoutRes();

    private ProgressDialog mProgressDialog;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(layoutRes());
    }


    public void hideLoading() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.cancel();
        }
    }


    public void showLoading() {
        if (mProgressDialog == null || !mProgressDialog.isShowing()) {
            mProgressDialog = CommonUtils.showLoadingDialog(this);
        }
    }

    public void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    public void handleError(Throwable throwable) {
        try {
            if (throwable instanceof NoConnectivityException) {
                showDialog(throwable.getMessage());
            } else if (throwable instanceof HttpException) {
                HttpException exception = ((HttpException) throwable);
                String errorMessage = exception.getMessage();
                if (errorMessage == null) {
                    errorMessage = getString(R.string.general_error_message);
                }
                showDialog(errorMessage);
            } else if (throwable instanceof SocketTimeoutException) {
                throwable.printStackTrace();
                showDialog(getString(R.string.general_error_message));
            } else if (throwable instanceof IOException) {
                throwable.printStackTrace();
                showDialog(getString(R.string.general_error_message));
            } else {
                showDialog(getString(R.string.general_error_message));
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            showDialog(getString(R.string.general_error_message));
        }
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (getCurrentFocus() != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
        }
        return super.dispatchTouchEvent(ev);
    }

    public void showDialog(String message) {
        new AlertDialog.Builder(this)
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setMessage(message)
                .setNeutralButton("OK", (dialogInterface, i) -> dialogInterface.dismiss())
                .show();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        hideLoading();
    }


}
