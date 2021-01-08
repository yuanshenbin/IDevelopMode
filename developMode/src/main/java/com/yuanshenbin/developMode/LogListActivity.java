package com.yuanshenbin.developMode;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;

/**
 * author : yuanshenbin
 * time   : 2018/2/9
 * desc   :
 */

public class LogListActivity extends DevelopBaseActivity {

    private LogAdapter mAdapter;
    private List<LogModel> mLogModels = new ArrayList<>();

    private ViewHolder mVH;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.develop_mode_activity_log_list);
        mVH = new ViewHolder(this);
        mVH.tv_title.setText("接口列表");
        mVH.tv_right_operate.setText("清空日志");
        mLogModels.clear();
        mLogModels.addAll(DevelopMode.getInstance().getLogModels());
        Collections.reverse(mLogModels);
        mAdapter = new LogAdapter();
        mVH.list.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
        mVH.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(LogListActivity.this, LogDetailActivity.class);
                intent.putExtra(LogDetailActivity.LOG_DATA, mLogModels.get(position));
                startActivity(intent);
            }
        });
        mVH.tv_right_operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DevelopMode.getInstance().setLogModels(new ArrayList<LogModel>());
                mLogModels.clear();
                mLogModels.addAll(DevelopMode.getInstance().getLogModels());
                mAdapter.notifyDataSetChanged();

            }
        });
        mVH.layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private class LogAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mLogModels.size();
        }

        @Override
        public Object getItem(int position) {
            return mLogModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder item;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.develop_mode_item_activity_log_list, null);
                item = new ViewHolder(convertView);
                convertView.setTag(item);
            } else {
                item = (ViewHolder) convertView.getTag();
            }

            LogModel data = mLogModels.get(position);
            item.setShow(data);
            return convertView;
        }


            public class ViewHolder {
                public TextView tv_time, tv_api_url,tv_request_time,tv_ambient,tv_KBitsPerSecond;

                public ViewHolder(View view) {
                    tv_time = (TextView) view.findViewById(R.id.tv_time);
                    tv_api_url = (TextView) view.findViewById(R.id.tv_api_url);
                    tv_request_time = (TextView) view.findViewById(R.id.tv_request_time);
                    tv_ambient = (TextView) view.findViewById(R.id.tv_ambient);
                    tv_KBitsPerSecond = (TextView) view.findViewById(R.id.tv_KBitsPerSecond);


                }

                public void setShow(LogModel data) {
                    tv_ambient.setText("网络状况:"+data.getConnectionQuality()+"");
                    tv_KBitsPerSecond.setText("请求网速:"+(double) Math.round(data.getKBitsPerSecond() * 100) / 100+"kb/s");

                    tv_request_time.setText("请求时长:"+String.valueOf(data.getRequestTime())+"毫秒");
                    tv_api_url.setText(data.getUrl());
                    tv_time.setText(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss zzzz").format(new Date(data.getCreateTime())));
                }
        }
    }

    public static class ViewHolder {
        public ImageView layout_back;
        public TextView tv_title;
        public TextView tv_right_operate;
        public ListView list;

        public ViewHolder(Activity rootView) {
            this.layout_back = (ImageView) rootView.findViewById(R.id.layout_back);
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_right_operate = (TextView) rootView.findViewById(R.id.tv_right_operate);
            this.list = (ListView) rootView.findViewById(R.id.list);
        }

    }
}
