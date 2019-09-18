package com.example.bfs;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListAdapter;
import android.widget.ExpandableListView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttException;

import java.util.List;

import javabean.order.OrderBean;
import pojo.LayoutTool;
import sqlite.OperationCRUD;

/**
 * Created by Joe on 2018/1/2.
 */

public class HostOrderPageOne extends PageView {

    // SQLite
    private OperationCRUD crud = new OperationCRUD(getContext());

    // 視圖元件、適配器
    private ExpandableListView expandableListView;
    private ExpandableListAdapter expandableListAdapter;

    public HostOrderPageOne(Context context) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.content_host_order_pageone, null);
        initView(view);
        addView(view);
    }

    // 初始化視圖
    private void initView(View view) {
        expandableListView = (ExpandableListView) view.findViewById(R.id.exListOrderHost1);
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
                return HostOrderActivity.pageOneOrderBeansList.size();
            }

            @Override
            public int getChildrenCount(int groupPosition) {
                return HostOrderActivity.pageOneOrderBeansList.get(groupPosition).getMeals().size();
            }

            @Override
            public Object getGroup(int groupPosition) {
                return HostOrderActivity.pageOneOrderBeansList.get(groupPosition);
            }

            @Override
            public Object getChild(int groupPosition, int childPosition) {
                return HostOrderActivity.pageOneOrderBeansList.get(groupPosition).getMeals().get(childPosition);
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
                return false;
            }

            @Override
            public View getGroupView(final int groupPosition, final boolean isExpanded, View convertView, final ViewGroup parent) {
                // 最外層 - 組佈局
                LinearLayout groupLinearLayout = new LinearLayout(getContext());
                groupLinearLayout.setOrientation(LinearLayout.HORIZONTAL);
                groupLinearLayout.setBackground(ContextCompat.getDrawable(getContext(), R.drawable.listview_background));
                groupLinearLayout.setPadding(0, 30, 0, 30);
                groupLinearLayout.setElevation(15);

                // 拒絕按鈕
                LinearLayout.LayoutParams btnRefuseParams =
                        new LinearLayout.LayoutParams(90, 90);
                btnRefuseParams.setMargins(60, 0, 60, 0);
                btnRefuseParams.gravity = Gravity.CENTER;
                Button btnRefuse = new Button(getContext());
                btnRefuse.setFocusable(false);
                btnRefuse.setBackgroundResource(R.drawable.host_refuse_btn_background);
                btnRefuse.setLayoutParams(btnRefuseParams);
                btnRefuse.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                        HostOrderActivity.pageOneOrderBeansList.get(groupPosition).setStatus(
                                getResources().getString(R.string.order_status_refuse));
                        OrderBean orderBean = HostOrderActivity.pageOneOrderBeansList.get(groupPosition);
                        try {
                            HostOrderActivity.mqttService.getClient().publish(
                                    orderBean.getId(),
                                    HostOrderActivity.gson.toJson(orderBean).getBytes(),
                                    2, false);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        HostOrderActivity.pageOneOrderBeansList.remove(groupPosition);
                        refresh();
                    }
                });
                groupLinearLayout.addView(btnRefuse);

                // 文字佈局
                LinearLayout.LayoutParams textLinearLayoutParams =
                        new LinearLayout.LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
                textLinearLayoutParams.weight = 1;
                LinearLayout textLinearLayout = new LinearLayout(getContext());
                textLinearLayout.setOrientation(LinearLayout.VERTICAL);
                textLinearLayout.setLayoutParams(textLinearLayoutParams);
                //textLinearLayout.setPadding(150, 30, 150, 30);

                // 使用者ID文字
                TextView textId = new TextView(getContext());
                textId.setTextColor(getResources().getColorStateList(R.color.colorTextDark));
                textId.setText(HostOrderActivity.pageOneOrderBeansList.get(groupPosition).getId());
                textLinearLayout.addView(textId);

                // 送出時間文字
                TextView textTime = new TextView(getContext());
                textTime.setText("    時間  " + HostOrderActivity.pageOneOrderBeansList.get(groupPosition).getTime());
                textLinearLayout.addView(textTime);

                // 總金額文字
                TextView textPrice = new TextView(getContext());
                textPrice.setText("    總計  " + String.valueOf(HostOrderActivity.pageOneOrderBeansList.get(groupPosition).getTotalPrice()) + "  元");
                textLinearLayout.addView(textPrice);

                groupLinearLayout.addView(textLinearLayout);

                // 接受按鈕
                LinearLayout.LayoutParams btnAcceptParams =
                        new LinearLayout.LayoutParams(90, 90);
                btnAcceptParams.setMargins(60, 0, 60, 0);
                btnAcceptParams.gravity = Gravity.CENTER;
                Button btnAccept = new Button(getContext());
                btnAccept.setFocusable(false);
                btnAccept.setBackgroundResource(R.drawable.host_accept_btn_background);
                btnAccept.setLayoutParams(btnAcceptParams);
                btnAccept.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (isExpanded) ((ExpandableListView) parent).collapseGroup(groupPosition);
                        // 設定狀態為"接受"
                        HostOrderActivity.pageOneOrderBeansList.get(groupPosition).setStatus(
                                getResources().getString(R.string.order_status_accept));
                        // 設定序號
                        HostOrderActivity.pageOneOrderBeansList.get(groupPosition).setSerialNumber(
                                (int)crud.getMostNewSerialNumber() + 1);
                        OrderBean orderBean = HostOrderActivity.pageOneOrderBeansList.get(groupPosition);
                        try {
                            HostOrderActivity.mqttService.getClient().publish(
                                    orderBean.getId(),
                                    HostOrderActivity.gson.toJson(orderBean).getBytes(),
                                    2, true);
                        } catch (MqttException e) {
                            e.printStackTrace();
                        }
                        long id = crud.insertOneOrder(orderBean);
                        HostOrderActivity.pageOneOrderBeansList.remove(groupPosition);
                        HostOrderActivity.pageTwoOrderBeansList = crud.getAllOrder();
                        refresh();
                        HostOrderPageTwo.refresh();
                    }
                });
                groupLinearLayout.addView(btnAccept);

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
                textName.setText(HostOrderActivity.pageOneOrderBeansList.get(groupPosition).getMeals().get(childPosition).getName());
                linearLayout.addView(textName);

                // 餐點數量
                TextView textQuantity = new TextView((getContext()));
                textQuantity.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT, 1f));
                textQuantity.setPadding(0, 0, 150, 0);
                textQuantity.setGravity(Gravity.RIGHT);
                textQuantity.setText(String.valueOf(HostOrderActivity.pageOneOrderBeansList.get(groupPosition).getMeals().get(childPosition).getQuantity()));
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
    public void refresh() {
        expandableListView.invalidateViews();
    }

}
