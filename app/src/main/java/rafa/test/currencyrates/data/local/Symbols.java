package rafa.test.currencyrates.data.local;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Symbols {
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("code")
    @Expose
    private String code;

    public Symbols(String description, String code) {
        this.description = description;
        this.code = code;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
