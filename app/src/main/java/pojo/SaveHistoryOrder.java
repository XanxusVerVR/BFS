package pojo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javabean.order.OrderBean;

/**
 * Created by surpr on 2018/1/6.
 */

public class SaveHistoryOrder {
    public static String SaveHistoryOrder(OrderBean OB) throws JSONException{

        JSONArray meals = new JSONArray();
        for (int i = 0; i < OB.getMeals().size(); i++) {
            JSONObject meal = new JSONObject();
            meal.put("mealName", OB.getMeals().get(i).getName());
            meal.put("mealQuantity", OB.getMeals().get(i).getQuantity());
            meals.put(meal);
        }

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", OB.getId());
        jsonObject.put("serialNumber", OB.getSerialNumber());
        jsonObject.put("status", OB.getStatus());
        jsonObject.put("time", OB.getTime());
        jsonObject.put("totalPrice", OB.getTotalPrice());
        jsonObject.put("meal",meals);

        return jsonObject.toString();
    }
}
