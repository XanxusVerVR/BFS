package javabean.order;

import android.support.annotation.Nullable;

import java.io.Serializable;
import java.util.List;

/**
 *        (  ・ω・)✄╰ひ╯
 * Created by Joe on 2017/12/9.
 */

public class OrderBean implements Serializable {

    // 顧客ID
    private String id;
    // 送出訂單的時間
    private String time;
    // 總金額
    private int totalPrice;
    // 餐點清單
    private List<Meal> meals;
    // 序號
    private int serialNumber;
    // 狀態 ( 拒絕、接受、完成、結束 )
    private String status;

    public OrderBean() {
        this.id = null;
        this.time = null;
        this.meals = null;
        this.serialNumber = -1;
        this.status = null;
    }

    public OrderBean(String id, String time, int totalPrice, List<Meal> meals, int serialNumber, String status) {
        this.id = id;
        this.time = time;
        this.meals = meals;
        this.serialNumber = serialNumber;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getSerialNumber() {
        return serialNumber;
    }

    public void setSerialNumber(int serialNumber) {
        this.serialNumber = serialNumber;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<Meal> getMeals() {
        return meals;
    }

    public void setMeals(List<Meal> meals) {//標注一下
        this.meals = meals;
    }


    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    @Override
    public String toString() {
        return "OrderBean{" + "id=" + id + ", time=" + time + ", totalPrice=" + totalPrice + ", meals=" + meals + ", serialNumber=" + serialNumber + ", status=" + status + '}';
    }
}

