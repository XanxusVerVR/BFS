package javabean.order;

import java.io.Serializable;

/**
 * Created by 卓俊傑 on 2017/12/13.
 */

public class Meal implements Serializable{

    // 餐點名
    private String name;
    // 數量
    private int quantity;

    public Meal() {
        this.name = null;
        this.quantity = -1;
    }

    public Meal(String name, int quantity) {
        this.name = name;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    @Override
    public String toString() {
        return "Meal{" + "name=" + name + ", quantity=" + quantity + '}';
    }

}
