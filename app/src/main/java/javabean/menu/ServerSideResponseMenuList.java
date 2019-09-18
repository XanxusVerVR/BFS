package javabean.menu;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ServerSideResponseMenuList {

    @SerializedName("mealNumber")
    @Expose
    private Integer mealNumber;
    @SerializedName("mealName")
    @Expose
    private String mealName;
    @SerializedName("mealPrice")
    @Expose
    private Integer mealPrice;
    @SerializedName("mealDescription")
    @Expose
    private String mealDescription;
    @SerializedName("mealCategory")
    @Expose
    private String mealCategory;
    @SerializedName("mealImage")
    @Expose
    private String mealImage;

    /**
     * No args constructor for use in serialization
     */
    public ServerSideResponseMenuList() {
    }

    /**
     * @param mealNumber
     * @param mealPrice
     * @param mealImage
     * @param mealName
     * @param mealDescription
     * @param mealCategory
     */
    public ServerSideResponseMenuList(Integer mealNumber, String mealName, Integer mealPrice, String mealDescription, String mealCategory, String mealImage) {
        super();
        this.mealNumber = mealNumber;
        this.mealName = mealName;
        this.mealPrice = mealPrice;
        this.mealDescription = mealDescription;
        this.mealCategory = mealCategory;
        this.mealImage = mealImage;
    }

    public Integer getMealNumber() {
        return mealNumber;
    }

    public void setMealNumber(Integer mealNumber) {
        this.mealNumber = mealNumber;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public Integer getMealPrice() {
        return mealPrice;
    }

    public void setMealPrice(Integer mealPrice) {
        this.mealPrice = mealPrice;
    }

    public String getMealDescription() {
        return mealDescription;
    }

    public void setMealDescription(String mealDescription) {
        this.mealDescription = mealDescription;
    }

    public String getMealCategory() {
        return mealCategory;
    }

    public void setMealCategory(String mealCategory) {
        this.mealCategory = mealCategory;
    }

    public String getMealImage() {
        return mealImage;
    }

    public void setMealImage(String mealImage) {
        this.mealImage = mealImage;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mealNumber", mealNumber).append("mealName", mealName).append("mealPrice", mealPrice).append("mealDescription", mealDescription).append("mealCategory", mealCategory).append("mealImage", mealImage).toString();
    }

}
