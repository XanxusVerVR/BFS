package com.example.bfs;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.CompoundButtonCompat;
import android.support.v7.widget.AppCompatCheckBox;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

import javabean.order.OrderBean;
import pojo.LayoutTool;
import pojo.VolleyService;
import sqlite.OperationCRUD;

/**
 * Created by Joe on 2018/1/2.
 */

public class HostOrderPageTwo extends PageView {

    // SQLite
    private OperationCRUD crud = new OperationCRUD(getContext());

    // 視圖元件、適配器
    public static ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    public HostOrderPageTwo(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.content_host_order_pagetwo, null);
        initView(view);
        addView(view);
    }

    // 初始化視圖
    private void initView(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.exListOrderHost2);
        newExpandableListAdapter();
        expandableListView.setAdapter(expandableListAdapter);
        expandableListView.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            int previousGroup = -1;

            @Override
            public void onGroupExpand(int groupPosition) {
                if (groupPosition != previousGroup) {
                    expandableListView.collapseGroup(previousGroup);
                }
                previousGroup = groupPosition;
            }
        });
    }

    // 初始化 ExpandableListAdapter
    private void newExpandableListAdapter() {
        expandableListAdapter = new BaseExpandableListAdapter() {
            @Override
            public int getGroupCount() {
                return HostOrderActivity.pageTwoOrderBeansList.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return HostOrderActivity.pageTwoOrderBeansList.get(groupPosition).getMeals().size();
            }

            @Override
            public Object getGroup(int groupPosition) {
                return HostOrderActivity.pageTwoOrderBeansList.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return HostOrderActivity.pageTwoOrderBeansList.get(groupPosition).getMeals().get(childPosition);
            }

            @Override
            public long getGroupId(int groupPosition) {
                return groupPosition;
            }

            @Override
            public long getChildId(int groupPosition, int childPosition) {
                return childPosition;
            }

            @Override
            public boolean hasStableIds() {
                return true;
            }

            @Override
            public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
                // 最外層 - 組佈局
                LinearLayout groupLinearLayout = new LinearLayout(getContext());
                groupLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                groupLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.listview_background));
                groupLinearLayout.setPadding(0, 30, 0, 30);
                groupLinearLayout.setElevation(15);

                // 訂單完成CheckBox
                LinearLayout.LayoutParams cbCompleteParams =
                        new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                cbCompleteParams.setMargins(30, 0, 30, 0);
                cbCompleteParams.gravity = Gravity.CENTER;
                final CheckBox cbComplete = new CheckBox(getContext());
                cbComplete.setFocusable(false);
                cbComplete.setLayoutParams(cbCompleteParams);
                // 判斷訂單狀態是否為"完成"，是則鎖定，否則監聽
                if (HostOrderActivity.pageTwoOrderBeansList.get(groupPosition).getStatus().equals(
                        getResources().getString(R.string.order_status_complete))) {
                    cbComplete.setChecked(true);
                    cbComplete.setEnabled(false);
                } else{
                    cbComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            if (isChecked) {
                                OrderBean orderBean = HostOrderActivity.pageTwoOrderBeansList.get(groupPosition);
                                orderBean.setStatus(getResources().getString(R.string.order_status_complete));
                                try {
                                    HostOrderActivity.mqttService.getClient().publish(
                                            orderBean.getId(),
                                            HostOrderActivity.gson.toJson(orderBean).getBytes(),
                                            2, true);
                                } catch (MqttException e) {
                                    e.printStackTrace();
                                }
                                crud.updateOrder(orderBean);
                                HostOrderActivity.pageTwoOrderBeansList = crud.getAllOrder();
                                refresh();
                            }
                        }
                    });
                }
                groupLinearLayout.addView(cbComplete);

                // 文字佈局
                LinearLayout.LayoutParams textLinearLayoutParams =
                        new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                textLinearLayoutParams.weight = 1;
                LinearLayout textLinearLayout = new LinearLayout(getContext());
                textLinearLayout.setOrientation(LinearLayout.VERTICAL);
                textLinearLayout.setLayoutParams(textLinearLayoutParams);

                // 訂單序號文字
                TextView textNum = new TextView(getContext());
                textNum.setTextColor(getResources().getColorStateList(R.color.colorTextImportant));
                textNum.setText(String.valueOf(HostOrderActivity.pageTwoOrderBeansList.get(groupPosition).getSerialNumber()));
                textLinearLayout.addView(textNum);

                // 送出時間文字
                TextView textTime = new TextView(getContext());
                textTime.setText("    時間  " + HostOrderActivity.pageTwoOrderBeansList.get(groupPosition).getTime());
                textLinearLayout.addView(textTime);

                // 總金額文字
                TextView textPrice = new TextView(getContext());
                textPrice.setText("    總計  " + String.valueOf(HostOrderActivity.pageTwoOrderBeansList.get(groupPosition).getTotalPrice()) + "  元");
                textLinearLayout.addView(textPrice);

                groupLinearLayout.addView(textLinearLayout);

                // 結束按鈕
                LinearLayout.LayoutParams btnEndParams =
                        new LinearLayout.LayoutParams(90, 90);
                btnEndParams.setMargins(60, 0, 60, 0);
                btnEndParams.gravity = Gravity.CENTER;
                Button btnEnd = new Button(getContext());
                btnEnd.setFocusable(false);
                btnEnd.setBackgroundResource(R.drawable.host_accept_btn_background);
                btnEnd.setLayoutParams(btnEndParams);
                btnEnd.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                        // 設定狀態為"結束"
                        OrderBean orderBean = HostOrderActivity.pageTwoOrderBeansList.get(groupPosition);
                        orderBean.setStatus(getResources().getString(R.string.order_status_end));
                        try {
                            HostOrderActivity.mqttService.getClient().publish(
                                    orderBean.getId(),
                                    HostOrderActivity.gson.toJson(orderBean).getBytes(),
                                    2, true);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        HostOrderActivity.saveOrderAsHistory(orderBean);
                        crud.deleteOrder(orderBean.getSerialNumber());
                        HostOrderActivity.pageTwoOrderBeansList = crud.getAllOrder();
                        refresh();
                    }
                });
                groupLinearLayout.addView(btnEnd);

                return groupLinearLayout;
            }

            @Override
            public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
                LinearLayout linearLayout = new  LinearLayout(getContext());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                linearLayout.setBackgroundColor(getResources().getColor(R.color.colorExList));
                linearLayout.setPadding(0, 30, 0, 30);

                // 餐點名稱
                TextView textName = new TextView(getContext());
                textName.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 4f));
                textName.setPadding(150, 0, 0, 0);
                textName.setText(HostOrderActivity.pageTwoOrderBeansList.get(groupPosition).getMeals().get(childPosition).getName());
                linearLayout.addView(textName);

                // 餐點數量
                TextView textQuantity = new TextView((getContext()));
                textQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                textQuantity.setPadding(0, 0, 150, 0);
                textQuantity.setGravity(Gravity.RIGHT);
                textQuantity.setText(String.valueOf(HostOrderActivity.pageTwoOrderBeansList.get(groupPosition).getMeals().get(childPosition).getQuantity()));
                linearLayout.addView(textQuantity);

                return linearLayout;
            }

            @Override
            public boolean isChildSelectable(int groupPosition, int childPosition) {
                return false;
            }

        };
    }

    // 更新頁面
    public static void refresh() {
        expandableListView.invalidateViews();
    }

}
