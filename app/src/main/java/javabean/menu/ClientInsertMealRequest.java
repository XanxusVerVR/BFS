package javabean.menu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ClientInsertMealRequest {

    @SerializedName("queryNumber")
    @Expose
    private Integer queryNumber;
    @SerializedName("queryAction")
    @Expose
    private String queryAction;
    @SerializedName("mealData")
    @Expose
    private MealData mealData;

    /**
     * No args constructor for use in serialization
     */
    public ClientInsertMealRequest() {
    }

    /**
     * @param mealData
     * @param queryAction
     * @param queryNumber
     */
    public ClientInsertMealRequest(Integer queryNumber, String queryAction, MealData mealData) {
        super();
        this.queryNumber = queryNumber;
        this.queryAction = queryAction;
        this.mealData = mealData;
    }

    public Integer getQueryNumber() {
        return queryNumber;
    }

    public void setQueryNumber(Integer queryNumber) {
        this.queryNumber = queryNumber;
    }

    public String getQueryAction() {
        return queryAction;
    }

    public void setQueryAction(String queryAction) {
        this.queryAction = queryAction;
    }

    public MealData getMealData() {
        return mealData;
    }

    public void setMealData(MealData mealData) {
        this.mealData = mealData;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("queryNumber", queryNumber).append("queryAction", queryAction).append("mealData", mealData).toString();
    }
}
