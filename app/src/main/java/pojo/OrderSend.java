package pojo;

import android.view.View;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

import javabean.order.Meal;
import javabean.order.OrderBean;

/**
 * Created by 卓俊傑 on 2017/12/13.
 */

public class OrderSend {

    private static List<Meal> meals = new ArrayList();

    public static String sendOrderToHost(String status, String id,int totalPrice) { // 將選取的餐點資訊丟進來會用Gson包好
        OrderBean bean = new OrderBean();
        bean.setSerialNumber(-1);
        bean.setStatus("");
        bean.setId(id);
        bean.setTime("");
        bean.setTotalPrice(totalPrice);
        bean.setMeals(meals);
        return bean.getMeals().toString();
    }

    public void addMeal(String mealName,int mealquantity){
        Meal meal = new Meal(mealName,mealquantity);
        meals.add(meal);
    }
}
