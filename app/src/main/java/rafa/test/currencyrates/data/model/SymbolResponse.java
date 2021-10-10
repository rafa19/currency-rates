package rafa.test.currencyrates.data.model;

import androidx.annotation.Nullable;

import java.util.HashMap;
import java.util.Map;

import rafa.test.currencyrates.data.local.Symbols;

public class SymbolResponse extends ApiResponse {

    @Nullable
    public Map<String, Symbols> symbols;

    public SymbolResponse(Status status, @Nullable Object data, @Nullable Throwable error) {
        super(status, data, error);
    }

    @Nullable
    public Map<String, Symbols> getSymbols() {
        return symbols;
    }

    public void setSymbols(@Nullable Map<String, Symbols> symbols) {
        this.symbols = symbols;
    }

}
