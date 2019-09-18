package com.example.bfs;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import android.widget.Toolbar;

import com.android.volley.VolleyError;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import javabean.order.Meal;
import javabean.order.OrderBean;
import pojo.IVolleyResponse;
import pojo.MqttService;
import pojo.SaveHistoryOrder;
import pojo.ServiceURL;
import pojo.VolleyService;
import sqlite.OperationCRUD;

import static com.android.volley.VolleyLog.TAG;

/**
 *        (  ・ω・)✄╰ひ╯
 * Created by Joe on 2017/12/24.
 * 老闆訂單 Activity
 */

public class HostOrderActivity extends AppCompatActivity {

    public static List<OrderBean> pageOneOrderBeansList = new ArrayList<>();
    public static List<OrderBean> pageTwoOrderBeansList;

    // MQTT
    public static MqttService mqttService;
    private String arrivedMqttMessage;
    // Volley
    public static VolleyService volleyService;
    private IVolleyResponse resultCallback = null;
    // SQLite
    private OperationCRUD crud = new OperationCRUD(this);
    // Gson
    public static Gson gson = new Gson();

    private android.support.v7.widget.Toolbar toolbar;
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private ArrayList<PageView> pageList;
    private PagerAdapter mPagerAdapter;
    private HostOrderPageOne hostOrderPageOne;
    private HostOrderPageTwo hostOrderPageTwo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_host_order);
        pageTwoOrderBeansList = crud.getAllOrder();
        initPage();
        initView();
        initMqtt();
        initVolleyCallback();
        volleyService = new VolleyService(resultCallback, this);
   }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_host, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if (id == R.id.action_host_history) {
            Intent actionHistory = new Intent();
            actionHistory.setClass(this, HistoryOrderQuery.class);
            startActivity(actionHistory);
        }

        return super.onOptionsItemSelected(item);
    }

    // 初始化子頁面
    private void initPage() {
        pageList = new ArrayList<>();

        // Page one : 待確認頁面
        hostOrderPageOne = new HostOrderPageOne(this);
        pageList.add(hostOrderPageOne);

        // Page two : 待完成頁面
        hostOrderPageTwo = new HostOrderPageTwo(this);
        pageList.add(hostOrderPageTwo);
    }

    // 初始化視圖
    private void initView() {
        toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.host_order_toolbar);
        toolbar.setTitle("BFS");
        setSupportActionBar(toolbar);
        mTabLayout = (TabLayout) findViewById(R.id.tabs_host_order);
        mViewPager = (ViewPager) findViewById(R.id.vp_host_order);

        mPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return pageList.size();
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return object == view;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                container.addView(pageList.get(position));
                return pageList.get(position);
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };
        mViewPager.setAdapter(mPagerAdapter);
        initListener();
    }

    // 初始化監聽器
    private void initListener() {
        // 綁定TabLayout與ViewPager
        mTabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mViewPager));
        mViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayout));
    }

    // 初始化MQTT連線
    private void initMqtt() {
        mqttService = new MqttService(this);
        // 嘗試連線
        mqttService.getToken().setActionCallback(new IMqttActionListener() {
            // 連線成功
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Toast.makeText(HostOrderActivity.this, "連線成功", Toast.LENGTH_LONG).show();
                try {
                    mqttService.getClient().subscribe("order", 2);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
            }

            // 連線出錯，例如 : 連線超時、防火牆問題等
            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Toast.makeText(HostOrderActivity.this, "連線失敗", Toast.LENGTH_LONG).show();
            }
        });

        mqttService.getClient().setCallback(new MqttCallback() {
            // 失去連線
            @Override
            public void connectionLost(Throwable throwable) {
                Toast.makeText(HostOrderActivity.this, "失去連線", Toast.LENGTH_LONG).show();
            }

            // 訊息到達
            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                arrivedMqttMessage = new String(mqttMessage.getPayload());
                pageOneOrderBeansList.add((OrderBean) gson.fromJson(arrivedMqttMessage, OrderBean.class));
                hostOrderPageOne.refresh();
            }

            // 交付完成
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    // 初始化Volley連線
    private void initVolleyCallback() {
        resultCallback = new IVolleyResponse() {
            @Override
            public void notifySuccess(String response) {
                Log.d("歷史訂單_Response", response);
            }

            @Override
            public void notifyError(VolleyError error) {
                Toast.makeText(HostOrderActivity.this, "訂單儲存至伺服器時發生錯誤", Toast.LENGTH_LONG).show();
            }
        };
    }

    // 將訂單存至歷史訂單
    public static void saveOrderAsHistory(OrderBean orderBean) {
        try {
            String json = SaveHistoryOrder.SaveHistoryOrder(orderBean);
            volleyService.triggerStringRequest(json, ServiceURL.HISTORY_SAVE);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    // 回應訂單
    public void respondOrder(int position) {
        OrderBean orderBean = pageOneOrderBeansList.get(position);
        try {
            mqttService.getClient().publish(orderBean.getId(), gson.toJson(orderBean).getBytes(), 2, false);
        } catch (MqttException e) {
            e.printStackTrace();
        }
        if (orderBean.getStatus().equals("拒絕")) {
            pageOneOrderBeansList.remove(position);
        }
        hostOrderPageOne.refresh();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttService.getClient().unregisterResources();
        mqttService.getClient().close();
    }

}
