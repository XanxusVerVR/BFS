package com.example.bfs;

import android.content.Intent;
import android.os.Bundle;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

import javabean.menu.AddMeal;
import javabean.menu.ServerSideResponseMenuList;
import javabean.order.Meal;
import javabean.order.OrderBean;
import pojo.OrderSend;
import pojo.ServiceURL;
import pojo.VolleyService;

import static pojo.OrderSend.sendOrderToHost;

public class MainCustomer extends AppCompatActivity implements TabHost.OnTabChangeListener,AdapterView.OnItemClickListener ,View.OnClickListener {

    TabHost customer, customerinside;
    ListView list1, list2, list3, list4, list5, list6;
    Gson gson = new Gson();
    private String catchresponse;
    Button minus, plus;
    public String id;
    public int passcheck = 0;
    public ArrayList<HashMap<String, Object>> hamburger = new ArrayList<HashMap<String, Object>>();
    public ArrayList<HashMap<String, Object>> omolet = new ArrayList<HashMap<String, Object>>();
    public ArrayList<HashMap<String, Object>> toast = new ArrayList<HashMap<String, Object>>();
    public ArrayList<HashMap<String, Object>> drink = new ArrayList<HashMap<String, Object>>();
    public ArrayList<HashMap<String, Object>> other = new ArrayList<HashMap<String, Object>>();
    public ArrayList<HashMap<String, Object>> setmeal = new ArrayList<HashMap<String, Object>>();
    //private ArrayList<Meal> mealPass = new ArrayList<Meal>();
    //private OrderBean mealBean;
    private int totalPrice = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maincustomer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        id = getIntent().getStringExtra("ID");
        minus = (Button) findViewById(R.id.minus);
        plus = (Button) findViewById(R.id.plus);

        customer = (TabHost) findViewById(R.id.mainmenu);
        customer.setup();
        TabHost.TabSpec spec;
        spec = customer.newTabSpec("Tab1");
        spec.setIndicator("瀏覽菜單");
        spec.setContent(R.id.tab1);
        customer.addTab(spec);
        spec = customer.newTabSpec("Tab2");
        spec.setIndicator("訂單管理");
        spec.setContent(R.id.tab2);
        customer.addTab(spec);
        customer.setCurrentTabByTag("Tab1");
        customer.setOnTabChangedListener(this);

        customerinside = (TabHost) findViewById(R.id.tab1inside);
        customerinside.setup();
        TabHost.TabSpec specinside;
        specinside = customerinside.newTabSpec("tab1inside_1");
        specinside.setIndicator("漢堡");
        specinside.setContent(R.id.tab1inside_1);
        customerinside.addTab(specinside);
        specinside = customerinside.newTabSpec("tab1inside_2");
        specinside.setIndicator("蛋餅");
        specinside.setContent(R.id.tab1inside_2);
        customerinside.addTab(specinside);
        specinside = customerinside.newTabSpec("tab1inside_3");
        specinside.setIndicator("吐司");
        specinside.setContent(R.id.tab1inside_3);
        customerinside.addTab(specinside);
        specinside = customerinside.newTabSpec("tab1inside_4");
        specinside.setIndicator("飲料");
        specinside.setContent(R.id.tab1inside_4);
        customerinside.addTab(specinside);
        specinside = customerinside.newTabSpec("tab1inside_5");
        specinside.setIndicator("其他");
        specinside.setContent(R.id.tab1inside_5);
        customerinside.addTab(specinside);
        specinside = customerinside.newTabSpec("tab1inside_6");
        specinside.setIndicator("套餐");
        specinside.setContent(R.id.tab1inside_6);
        customerinside.addTab(specinside);

        customerinside.setCurrentTabByTag("tab1inside_1");
        customerinside.setOnTabChangedListener(this);

        try {
            getMealinfo();
            setList();
            Thread.sleep(5000);//在這裡我讓線程進行耗時操作
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void getMealinfo() {
        RequestQueue mQueue;//json包裝
        mQueue = Volley.newRequestQueue(this);
        JSONObject json_search = new JSONObject();//發送請求查詢的動作
        try {
            json_search.put("queryNumber", Integer.parseInt("1"));
            json_search.put("queryAction", "select");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        //回來的包裝為jsonarray
        JsonArrayRequest mJsonArrayRequest = new JsonArrayRequest(Request.Method.POST,
                ServiceURL.MENU,
                json_search,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        catchresponse = response.toString();
                        ServerSideResponseMenuList[] enums2 = gson.fromJson(catchresponse, ServerSideResponseMenuList[].class);
                        for (ServerSideResponseMenuList b : enums2) {
                            if (b.getMealCategory().equals("漢堡")) {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("ItemName", b.getMealName().toString());
                                map.put("count", "0");
                                map.put("price", b.getMealPrice().toString());
                                map.put("description", b.getMealDescription().toString());
                                map.put("category", b.getMealCategory().toString());
                                map.put("number", b.getMealNumber().toString());
                                map.put("ID",id);
                                hamburger.add(map);
                            } else if (b.getMealCategory().equals("蛋餅")) {
                                HashMap<String, Object> map2 = new HashMap<String, Object>();
                                map2.put("ItemName", b.getMealName().toString());
                                map2.put("count", "0");
                                map2.put("price", b.getMealPrice().toString());
                                map2.put("description", b.getMealDescription().toString());
                                map2.put("category", b.getMealCategory().toString());
                                map2.put("ID",id);
                                omolet.add(map2);
                            } else if (b.getMealCategory().equals("吐司")) {
                                HashMap<String, Object> map3 = new HashMap<String, Object>();
                                map3.put("ItemName", b.getMealName().toString());
                                map3.put("count", "0");
                                map3.put("price", b.getMealPrice().toString());
                                map3.put("description", b.getMealDescription().toString());
                                map3.put("category", b.getMealCategory().toString());
                                map3.put("ID",id);
                                toast.add(map3);
                            } else if (b.getMealCategory().equals("飲料")) {
                                HashMap<String, Object> map4 = new HashMap<String, Object>();
                                map4.put("ItemName", b.getMealName().toString());
                                map4.put("count", "0");
                                map4.put("price", b.getMealPrice().toString());
                                map4.put("description", b.getMealDescription().toString());
                                map4.put("category", b.getMealCategory().toString());
                                map4.put("ID",id);
                                drink.add(map4);
                            } else if (b.getMealCategory().equals("其他")) {
                                HashMap<String, Object> map5 = new HashMap<String, Object>();
                                map5.put("ItemName", b.getMealName().toString());
                                map5.put("count", "0");
                                map5.put("price", b.getMealPrice().toString());
                                map5.put("description", b.getMealDescription().toString());
                                map5.put("category", b.getMealCategory().toString());
                                map5.put("ID",id);
                                other.add(map5);
                            } else if (b.getMealCategory().equals("套餐")) {
                                HashMap<String, Object> map6 = new HashMap<String, Object>();
                                map6.put("ItemName", b.getMealName().toString());
                                map6.put("count", "0");
                                map6.put("price", b.getMealPrice().toString());
                                map6.put("description", b.getMealDescription().toString());
                                map6.put("category", b.getMealCategory().toString());
                                map6.put("ID",id);
                                setmeal.add(map6);
                            }
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.d("err", error.toString());
                    }
                }
        );
        mQueue.add(mJsonArrayRequest);
    }

    public void setList() {
        AddMeal hamburgeradp = new AddMeal(
                this,
                hamburger,
                R.layout.additemadapter,
                new String[]{"ItemName", "minus", "count", "plus", "price", "description","ID"},
                new int[]{R.id.itemname, R.id.minus, R.id.count, R.id.plus,R.id.itemprice}
        );

        AddMeal omoletadp = new AddMeal(
                this,
                omolet,
                R.layout.additemadapter,
                new String[]{"ItemName", "minus", "count", "plus", "price", "description","ID"},
                new int[]{R.id.itemname, R.id.minus, R.id.count, R.id.plus,R.id.itemprice}
        );

        AddMeal toastadp = new AddMeal(
                this,
                toast,
                R.layout.additemadapter,
                new String[]{"ItemName", "minus", "count", "plus", "price", "description","ID"},
                new int[]{R.id.itemname, R.id.minus, R.id.count, R.id.plus,R.id.itemprice}
        );

        AddMeal drinkadp = new AddMeal(
                this,
                drink,
                R.layout.additemadapter,
                new String[]{"ItemName", "minus", "count", "plus", "price", "description","ID"},
                new int[]{R.id.itemname, R.id.minus, R.id.count, R.id.plus,R.id.itemprice}
        );

        AddMeal otheradp = new AddMeal(
                this,
                other,
                R.layout.additemadapter,
                new String[]{"ItemName", "minus", "count", "plus", "price", "description","ID"},
                new int[]{R.id.itemname, R.id.minus, R.id.count, R.id.plus,R.id.itemprice}
        );

        AddMeal setmealadp = new AddMeal(
                this,
                setmeal,
                R.layout.additemadapter,
                new String[]{"ItemName", "minus", "count", "plus", "price", "description","ID"},
                new int[]{R.id.itemname, R.id.minus, R.id.count, R.id.plus,R.id.itemprice}
        );

        list1 = (ListView) findViewById(R.id.hamburgerc);
        list1.setAdapter(hamburgeradp);

        list2 = (ListView) findViewById(R.id.omeletc);
        list2.setAdapter(omoletadp);


        list3 = (ListView) findViewById(R.id.toastc);
        list3.setAdapter(toastadp);

        list4 = (ListView) findViewById(R.id.drinkc);
        list4.setAdapter(drinkadp);

        list5 = (ListView) findViewById(R.id.otherc);
        list5.setAdapter(otheradp);

        list6 = (ListView) findViewById(R.id.setmealc);
        list6.setAdapter(setmealadp);
    }

    public class BeanPass implements Serializable {
        private static final long serialVersionUID = -7060210544600464481L;
    }

    @Override
    public void onTabChanged(String s) {
        ArrayList<Meal> mealPass = new ArrayList<Meal>();
        OrderBean mealBean;
        if(s.equals("Tab2")) {
            Intent CustomerOrderActivity = new Intent();
            CustomerOrderActivity.setClass(MainCustomer.this, CustomerOrderActivity.class);
            for(int i = 0;i<hamburger.size();i++){
                if(Integer.parseInt(hamburger.get(i).get("count").toString())>0){
                    Meal newMeal = new Meal(hamburger.get(i).get("ItemName").toString(),Integer.parseInt(hamburger.get(i).get("count").toString()));
                    mealPass.add(newMeal);
                    totalPrice = totalPrice +Integer.parseInt(hamburger.get(i).get("price").toString())*Integer.parseInt(hamburger.get(i).get("count").toString());
                    passcheck = 1;
                    Log.d("取值測試","品名"+hamburger.get(i).get("ItemName").toString()+"數量"+hamburger.get(i).get("count").toString());
                }
            }
            for(int i = 0;i<omolet.size();i++){
                if(Integer.parseInt(omolet.get(i).get("count").toString())>0){
                    Meal newMeal = new Meal(omolet.get(i).get("ItemName").toString(),Integer.parseInt(omolet.get(i).get("count").toString()));
                    totalPrice = totalPrice +Integer.parseInt(omolet.get(i).get("price").toString())*Integer.parseInt(omolet.get(i).get("count").toString());
                    mealPass.add(newMeal);
                    passcheck = 1;
                    Log.d("取值測試","品名"+omolet.get(i).get("ItemName").toString()+"數量"+omolet.get(i).get("count").toString());
                }
            }
            for(int i = 0;i<toast.size();i++){
                if(Integer.parseInt(toast.get(i).get("count").toString())>0){
                    Meal newMeal = new Meal(toast.get(i).get("ItemName").toString(),Integer.parseInt(toast.get(i).get("count").toString()));
                    totalPrice = totalPrice +Integer.parseInt(toast.get(i).get("price").toString())*Integer.parseInt(toast.get(i).get("count").toString());
                    mealPass.add(newMeal);
                    passcheck = 1;
                    Log.d("取值測試","品名"+toast.get(i).get("ItemName").toString()+"數量"+toast.get(i).get("count").toString());
                }
            }
            for(int i = 0;i<drink.size();i++){
                if(Integer.parseInt(drink.get(i).get("count").toString())>0){
                    Meal newMeal = new Meal(drink.get(i).get("ItemName").toString(),Integer.parseInt(drink.get(i).get("count").toString()));
                    totalPrice = totalPrice +Integer.parseInt(drink.get(i).get("price").toString())*Integer.parseInt(drink.get(i).get("count").toString());
                    mealPass.add(newMeal);
                    passcheck = 1;
                    Log.d("取值測試","品名"+drink.get(i).get("ItemName").toString()+"數量"+drink.get(i).get("count").toString());
                }
            }
            for(int i = 0;i<other.size();i++){
                if(Integer.parseInt(other.get(i).get("count").toString())>0){
                    Meal newMeal = new Meal(other.get(i).get("ItemName").toString(),Integer.parseInt(other.get(i).get("count").toString()));
                    totalPrice = totalPrice +Integer.parseInt(other.get(i).get("price").toString())*Integer.parseInt(other.get(i).get("count").toString());
                    mealPass.add(newMeal);
                    passcheck = 1;
                    Log.d("取值測試","品名"+other.get(i).get("ItemName").toString()+"數量"+other.get(i).get("count").toString());
                }
            }
            if(passcheck!=0) {
                mealBean = new OrderBean(id, "", totalPrice, mealPass, -1, "");
                mealBean.setTotalPrice(totalPrice);
                //傳送至顧客訂單部分
                Bundle bundle = new Bundle();
                bundle.putSerializable("OrderBean", (Serializable) mealBean);
                CustomerOrderActivity.putExtras(bundle);
                Log.d("總價格", String.valueOf(totalPrice) + "元");
                Log.d("bean", mealBean.toString());
                totalPrice = 0;
                startActivity(CustomerOrderActivity);
                customer.setCurrentTabByTag("Tab1");
                passcheck = 0;
            }
            else{
                Toast.makeText(MainCustomer.this,"餐點不可為空",Toast.LENGTH_LONG).show();
                customer.setCurrentTabByTag("Tab1");
            }
        }
    }

    //listview項目設置監聽事件
    @Override
    public void onItemClick(AdapterView arg0, View arg1, int arg2, long arg3) {
        Intent Menucustomer = new Intent();
    }

    @Override
    public void onClick(View view) {

    }

}
