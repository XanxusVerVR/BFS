package com.example.bfs;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.TaskStackBuilder;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import javabean.order.Meal;
import javabean.order.OrderBean;
import pojo.MqttService;

import static pojo.OrderSend.sendOrderToHost;

/**
 *        (  ・ω・)✄╰ひ╯
 * Created by Joe on 2017/12/24.
 * 顧客訂單 Activity
 */

public class CustomerOrderActivity extends AppCompatActivity {

    private OrderBean orderBean;

    // 定義時間格式
    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    private Intent intent;
    private MqttService mqttService;
    private String arrivedMqttMessage;
    private Gson gson = new Gson();

    // <視圖元件-------------------
    private TextView tvSerialNumber;
    private TextView tvTotalPrice;
    private Button btnSendCustomer;
    private ListView listView;
    private ListAdapter listAdapter;
    // --------------------------->

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_order);
        intent = this.getIntent();
        orderBean = (OrderBean)intent.getSerializableExtra("OrderBean");
        initView();
        initMqtt();
    }

    // 初始化視圖
    private void initView() {
        tvSerialNumber = (TextView) findViewById(R.id.tvSerialNumber);
        tvTotalPrice = (TextView) findViewById(R.id.tvTotalPrice);
        btnSendCustomer = (Button) findViewById(R.id.btnSendCustomer);
        listView = (ListView) findViewById(R.id.listOrderCustomer);

        btnSendCustomer.setBackgroundTintList(getResources().getColorStateList(R.color.colorNoStatus));
        btnSendCustomer.setTextColor(getResources().getColorStateList(R.color.colorTextLight));
        btnSendCustomer.setText("");
        tvTotalPrice.setText("總計：" + orderBean.getTotalPrice() + " 元");
        listAdapter = new BaseAdapter() {
            @Override
            public int getCount() {
                return orderBean.getMeals().size();
            }

            @Override
            public Object getItem(int position) {
                return orderBean.getMeals().get(position);
            }

            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public View getView(int position, View view, ViewGroup viewGroup) {
                LinearLayout linearLayout = new  LinearLayout(CustomerOrderActivity.this);
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));

                // 餐點名稱
                TextView textViewLeft = new TextView(CustomerOrderActivity.this);
                textViewLeft.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 4f));
                textViewLeft.setPadding(120, 30, 0, 30);
                textViewLeft.setTextSize(18);
                textViewLeft.setText(orderBean.getMeals().get(position).getName());

                // 餐點數量
                TextView textViewRight = new TextView((CustomerOrderActivity.this));
                textViewRight.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                textViewRight.setGravity(Gravity.RIGHT);
                textViewRight.setPadding(0, 30, 120, 30);
                textViewRight.setTextSize(18);
                textViewRight.setText(orderBean.getMeals().get(position).getQuantity() + "");

                linearLayout.addView(textViewLeft);
                linearLayout.addView(textViewRight);

                return linearLayout;
            }
        };
        listView.setAdapter(listAdapter);
    }

    // 初始化MQTT連線
    private void initMqtt() {
        mqttService = new MqttService(CustomerOrderActivity.this);
        // 嘗試連線
        mqttService.getToken().setActionCallback(new IMqttActionListener() {
            // 連線成功
            @Override
            public void onSuccess(IMqttToken iMqttToken) {
                Toast.makeText(CustomerOrderActivity.this, "連線成功", Toast.LENGTH_LONG).show();
                try {
                    mqttService.getClient().subscribe(orderBean.getId(), 2);
                } catch (MqttException e) {
                    e.printStackTrace();
                }
                btnSendCustomer.setEnabled(true);
                btnSendCustomer.setBackgroundTintList(getResources().getColorStateList(R.color.colorPrimary));
                btnSendCustomer.setText(R.string.send);
            }

            // 連線出錯，例如 : 連線超時、防火牆問題等
            @Override
            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                Toast.makeText(CustomerOrderActivity.this, "連線失敗", Toast.LENGTH_LONG).show();
            }
        });

        mqttService.getClient().setCallback(new MqttCallback() {
            // 失去連線
            @Override
            public void connectionLost(Throwable throwable) {
                Toast.makeText(CustomerOrderActivity.this, "失去連線", Toast.LENGTH_LONG).show();
            }

            // 訊息到達
            @Override
            public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                arrivedMqttMessage = new String(mqttMessage.getPayload());
                checkMessage(arrivedMqttMessage);
            }

            // 交付完成
            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
    }

    // 發佈訂單給老闆
    private void sendOrderToHost(OrderBean orderBean) {
        Date date = new Date();
        orderBean.setTime(simpleDateFormat.format(date));
        String json = gson.toJson(orderBean);
        try {
            mqttService.getClient().publish("order", json.getBytes(), 2, false);
        } catch (MqttException e) {
            Toast.makeText(CustomerOrderActivity.this, "訂單傳送失敗", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // 發佈訂單給自己
    private void sentOrderToMyself(OrderBean orderBean) {
        try {
            String json = gson.toJson(orderBean);
            mqttService.getClient().publish(orderBean.getId(), json.getBytes(), 2, true);
        } catch (MqttException e) {
            Toast.makeText(CustomerOrderActivity.this, "訂單傳送失敗", Toast.LENGTH_LONG).show();
            e.printStackTrace();
        }
    }

    // 清空訂閱看板
    private void cleanMqttTopic(String id) {
        try {
            mqttService.getClient().publish(id, new byte[0], 2, true);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    // 依老闆端回應進行動作
    private void checkMessage(String arrivedMqttMessage) {
        JsonObject jsonObject = new Gson().fromJson(arrivedMqttMessage, JsonObject.class);
        String status = jsonObject.get("status").getAsString();
        String number = jsonObject.get("serialNumber").getAsString();
        String id = jsonObject.get("id").getAsString();
        if (status.equals("")) {
            btnSendCustomer.setEnabled(false);
            btnSendCustomer.setBackgroundTintList(getResources().getColorStateList(R.color.colorNoStatus));
            btnSendCustomer.setTextColor(getResources().getColorStateList(R.color.colorTextLight));
            btnSendCustomer.setText(R.string.customer_null);
        }else if (status.equals(getResources().getString(R.string.order_status_refuse))) {
            btnSendCustomer.setEnabled(false);
            btnSendCustomer.setBackgroundTintList(getResources().getColorStateList(R.color.colorRefuse));
            btnSendCustomer.setTextColor(getResources().getColorStateList(R.color.colorTextLight));
            btnSendCustomer.setText(R.string.customer_refuse);
            cleanMqttTopic(id);
            // 無法發送通知
            makeNotification("BFS通知", "您的訂單已被拒絕，請稍後再試。", null);
        } else if (status.equals(getResources().getString(R.string.order_status_accept))) {
            btnSendCustomer.setEnabled(false);
            btnSendCustomer.setBackgroundTintList(getResources().getColorStateList(R.color.colorAccept));
            btnSendCustomer.setTextColor(getResources().getColorStateList(R.color.colorTextLight));
            btnSendCustomer.setText(R.string.customer_accept);
            tvSerialNumber.setText(number);
            tvSerialNumber.setVisibility(View.VISIBLE);
        } else if (status.equals(getResources().getString(R.string.order_status_complete))) {
            btnSendCustomer.setEnabled(false);
            btnSendCustomer.setBackgroundTintList(getResources().getColorStateList(R.color.colorComplete));
            btnSendCustomer.setTextColor(getResources().getColorStateList(R.color.colorTextLight));
            btnSendCustomer.setText(R.string.customer_complete);
            tvSerialNumber.setText(number);
            tvSerialNumber.setVisibility(View.VISIBLE);
        } else if (status.equals(getResources().getString(R.string.order_status_end))) {
            btnSendCustomer.setEnabled(false);
            btnSendCustomer.setBackgroundTintList(getResources().getColorStateList(R.color.colorRefuse));
            btnSendCustomer.setTextColor(getResources().getColorStateList(R.color.colorTextLight));
            btnSendCustomer.setText(R.string.customer_end);
            tvSerialNumber.setText(number);
            tvSerialNumber.setVisibility(View.VISIBLE);
            cleanMqttTopic(id);
        }
    }

    // 發出系統通知
    private void makeNotification(String title, String text, @Nullable String info) {
        NotificationManager manager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        int defaults = 0;
        int nId = 123;
        defaults |= Notification.DEFAULT_ALL;
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setWhen(System.currentTimeMillis())
                .setContentTitle(title)
                .setContentText(text)
                .setDefaults(defaults);

                //.setContentInfo(info);
        Notification notification = builder.build();
        manager.notify(nId, notification);

        /*
        Intent resultIntent = new Intent(this, CustomerOrderActivity.class);
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
        stackBuilder.addParentStack(CustomerOrderActivity.class);
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_CANCEL_CURRENT
                );
        builder.setContentIntent(resultPendingIntent);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        notificationManager.notify(0, builder.build());
        */
    }

    // 送出/狀態按鈕OnClick
    public void btnSendOnClick(View v) {
        sendOrderToHost(orderBean);
        sentOrderToMyself(orderBean);
        btnSendCustomer.setBackgroundTintList(getResources().getColorStateList(R.color.colorNoStatus));
        btnSendCustomer.setTextColor(getResources().getColorStateList(R.color.colorTextLight));
        btnSendCustomer.setText(R.string.customer_null);
        btnSendCustomer.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttService.getClient().unregisterResources();
        mqttService.getClient().close();
    }

}
