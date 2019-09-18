package com.example.bfs;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TabHost;

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

import java.util.ArrayList;
import java.util.HashMap;

import javabean.menu.HostMeal;
import javabean.menu.ServerSideResponseMenuList;
import pojo.ServiceURL;

import static pojo.OrderSend.sendOrderToHost;

public class MainOwner extends AppCompatActivity implements TabHost.OnTabChangeListener ,AdapterView.OnItemClickListener {

    TabHost host,hostinside;
    Gson gson = new Gson();
    private String catchresponse;
    ListView list1,list2,list3,list4,list5,list6;
    static Activity hostactivity;
    public ArrayList<HashMap<String, Object>> hamburger = new ArrayList<HashMap<String, Object>>();
    public ArrayList<HashMap<String, Object>> omolet = new ArrayList<HashMap<String, Object>>();
    public ArrayList<HashMap<String, Object>> toast = new ArrayList<HashMap<String, Object>>();
    public ArrayList<HashMap<String, Object>> drink = new ArrayList<HashMap<String, Object>>();
    public ArrayList<HashMap<String, Object>> other = new ArrayList<HashMap<String, Object>>();
    public ArrayList<HashMap<String, Object>> setmeal = new ArrayList<HashMap<String, Object>>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        hostactivity = this;
        setContentView(R.layout.activity_mainowner);
        Toolbar toolbar = (Toolbar) findViewById(R.id.hostbar);
        setSupportActionBar(toolbar);

        //最外層tab
        host = (TabHost) findViewById(R.id.mainmenu2);
        host.setup();
        TabHost.TabSpec spec;
        spec = host.newTabSpec("Tab3");
        spec.setIndicator("菜單管理");
        spec.setContent(R.id.tab3);
        host.addTab(spec);
        spec = host.newTabSpec("Tab4");
        spec.setIndicator("訂單管理");
        spec.setContent(R.id.tab4);
        host.addTab(spec);
        host.setCurrentTabByTag("Tab3");
        host.setOnTabChangedListener(this);

        //內層tab
        hostinside = (TabHost) findViewById(R.id.tab3inside);
        hostinside.setup();
        TabHost.TabSpec specinside;
        specinside = hostinside.newTabSpec("tab3inside_1");
        specinside.setIndicator("漢堡");
        specinside.setContent(R.id.tab3inside_1);
        hostinside.addTab(specinside);
        specinside = hostinside.newTabSpec("tab3inside_2");
        specinside.setIndicator("蛋餅");
        specinside.setContent(R.id.tab3inside_2);
        hostinside.addTab(specinside);
        specinside = hostinside.newTabSpec("tab3inside_3");
        specinside.setIndicator("吐司");
        specinside.setContent(R.id.tab3inside_3);
        hostinside.addTab(specinside);
        specinside = hostinside.newTabSpec("tab3inside_4");
        specinside.setIndicator("飲料");
        specinside.setContent(R.id.tab3inside_4);
        hostinside.addTab(specinside);
        specinside = hostinside.newTabSpec("tab3inside_5");
        specinside.setIndicator("其他");
        specinside.setContent(R.id.tab3inside_5);
        hostinside.addTab(specinside);
        specinside = hostinside.newTabSpec("tab3inside_6");
        specinside.setIndicator("套餐");
        specinside.setContent(R.id.tab3inside_6);
        hostinside.addTab(specinside);

        hostinside.setCurrentTabByTag("tab3inside_1");
        hostinside.setOnTabChangedListener(this);

        //設置arraylist跟ListView的內容
        try {
            getMealinfo();
            setList();
            Thread.sleep(5000);//在這裡我讓線程進行耗時操作
        }
        catch (InterruptedException e){
            e.printStackTrace();
        }

    }
    public void getMealinfo(){
        RequestQueue mQueue;//json包裝
        mQueue = Volley.newRequestQueue(this);
        JSONObject json_search= new JSONObject();//發送請求查詢的動作
        try {
            json_search.put("queryNumber",Integer.parseInt("1"));
            json_search.put("queryAction","select");
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
                        int i = 0;
                        catchresponse = response.toString();
                        ServerSideResponseMenuList[] enums2 = gson.fromJson(catchresponse, ServerSideResponseMenuList[].class);
                        HashMap<String, Object> init = new HashMap<String, Object>();
                        init.put("ItemName","新增餐點");
                        hamburger.add(init);
                        omolet.add(init);
                        toast.add(init);
                        drink.add(init);
                        other.add(init);
                        setmeal.add(init);
                        for(ServerSideResponseMenuList b : enums2){
                            if (b.getMealCategory().equals("漢堡")) {
                                HashMap<String, Object> map = new HashMap<String, Object>();
                                map.put("ItemName", b.getMealName().toString());
                                map.put("number",b.getMealNumber().toString());
                                map.put("price", b.getMealPrice().toString());
                                map.put("description", b.getMealDescription().toString());
                                map.put("category", b.getMealCategory().toString());
                                map.put("number", b.getMealNumber().toString());
                                hamburger.add(map);
                            } else if (b.getMealCategory().equals("蛋餅")) {
                                HashMap<String, Object> map2 = new HashMap<String, Object>();
                                map2.put("ItemName", b.getMealName().toString());
                                map2.put("number",b.getMealNumber().toString());
                                map2.put("price", b.getMealPrice().toString());
                                map2.put("description", b.getMealDescription().toString());
                                map2.put("category", b.getMealCategory().toString());
                                omolet.add(map2);
                            } else if (b.getMealCategory().equals("吐司")) {
                                HashMap<String, Object> map3 = new HashMap<String, Object>();
                                map3.put("ItemName", b.getMealName().toString());
                                map3.put("number",b.getMealNumber().toString());
                                map3.put("price", b.getMealPrice().toString());
                                map3.put("description", b.getMealDescription().toString());
                                map3.put("category", b.getMealCategory().toString());
                                toast.add(map3);
                            } else if (b.getMealCategory().equals("飲料")) {
                                HashMap<String, Object> map4 = new HashMap<String, Object>();
                                map4.put("ItemName", b.getMealName().toString());
                                map4.put("number",b.getMealNumber().toString());
                                map4.put("price", b.getMealPrice().toString());
                                map4.put("description", b.getMealDescription().toString());
                                map4.put("category", b.getMealCategory().toString());
                                drink.add(map4);
                            } else if (b.getMealCategory().equals("其他")) {
                                HashMap<String, Object> map5 = new HashMap<String, Object>();
                                map5.put("ItemName", b.getMealName().toString());
                                map5.put("number",b.getMealNumber().toString());
                                map5.put("price", b.getMealPrice().toString());
                                map5.put("description", b.getMealDescription().toString());
                                map5.put("category", b.getMealCategory().toString());
                                other.add(map5);
                            } else if (b.getMealCategory().equals("套餐")) {
                                HashMap<String, Object> map6 = new HashMap<String, Object>();
                                map6.put("ItemName", b.getMealName().toString());
                                map6.put("number",b.getMealNumber().toString());
                                map6.put("price", b.getMealPrice().toString());
                                map6.put("description", b.getMealDescription().toString());
                                map6.put("category", b.getMealCategory().toString());
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


    public void setList(){
        HostMeal hamburgeradp = new HostMeal(
                this,
                hamburger,
                R.layout.hostmenuadapter,
                new String[]{"ItemName", "minus", "number", "plus", "price", "description"},
                new int[]{R.id.hostmeal, R.id.hostprice}
        );

        HostMeal omoletadp = new HostMeal(
                this,
                omolet,
                R.layout.hostmenuadapter,
                new String[]{"ItemName", "minus", "number", "plus", "price", "description"},
                new int[]{R.id.hostmeal, R.id.hostprice}
        );

        HostMeal toastadp = new HostMeal(
                this,
                toast,
                R.layout.hostmenuadapter,
                new String[]{"ItemName", "minus", "number", "plus", "price", "description"},
                new int[]{R.id.hostmeal, R.id.hostprice}
        );

        HostMeal drinkadp = new HostMeal(
                this,
                drink,
                R.layout.hostmenuadapter,
                new String[]{"ItemName", "minus", "number", "plus", "price", "description"},
                new int[]{R.id.hostmeal, R.id.hostprice}
        );

        HostMeal otheradp = new HostMeal(
                this,
                other,
                R.layout.hostmenuadapter,
                new String[]{"ItemName", "minus", "number", "plus", "price", "description"},
                new int[]{R.id.hostmeal, R.id.hostprice}
        );

        HostMeal setmealadp = new HostMeal(
                this,
                setmeal,
                R.layout.hostmenuadapter,
                new String[]{"ItemName", "minus", "number", "plus", "price", "description"},
                new int[]{R.id.hostmeal, R.id.hostprice}
        );

        list1 = (ListView) findViewById(R.id.hamburger);
        list1.setAdapter(hamburgeradp);


        list2 = (ListView) findViewById(R.id.omelet);
        list2.setAdapter(omoletadp);

        list3 = (ListView) findViewById(R.id.toast);
        list3.setAdapter(toastadp);

        list4 = (ListView) findViewById(R.id.drink);
        list4.setAdapter(drinkadp);

        list5 = (ListView) findViewById(R.id.other);
        list5.setAdapter(otheradp);

        list6 = (ListView) findViewById(R.id.setmeal);
        list6.setAdapter(setmealadp);
    }


    @Override
    public void onTabChanged(String s) {
        if(s.equals("Tab4")){
            Intent HostOrderActivity = new Intent();
            HostOrderActivity.setClass(MainOwner.this,HostOrderActivity.class);
            startActivity(HostOrderActivity);
            host.setCurrentTabByTag("Tab3");
        }
    }
    //監聽事件
    @Override
    public void onItemClick(AdapterView arg0, View arg1, int arg2,long arg3) {

    }
}
