package com.yuanshenbin.developMode;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.ClipboardManager;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * author : yuanshenbin
 * time   : 2018/8/29
 * desc   :
 */

public class LogDetailActivity extends DevelopBaseActivity {

    private ViewHolder mVH;


    public static final String LOG_DATA = "log_data";
    private LogModel query_model;
    private String mShareData;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        query_model = (LogModel) getIntent().getSerializableExtra(LOG_DATA);
        setContentView(R.layout.develop_mode_activity_log_detail);
        mVH = new ViewHolder(this);
        mVH.tv_right_operate.setVisibility(View.VISIBLE);

        mVH.tv_title.setText("接口详情");
        mVH.tv_right_operate.setText("分享接口日志");

        mVH.tv_url.setText(query_model.getUrl());

        StringBuffer param = new StringBuffer();
        try {
            String[] lines = stringToJson(query_model.getParam()).split(System.getProperty("line.separator"));
            for (String line : lines) {
                param.append(line);
                param.append("\n");
            }
            mVH.tv_param.setText(param.toString());
        } catch (Exception e) {
            mVH.tv_param.setText(query_model.getParam());
        }


        StringBuffer result = new StringBuffer();
        try {
            String[] lines = stringToJson(query_model.getResult()).split(System.getProperty("line.separator"));
            for (String line : lines) {
                result.append(line);
                result.append("\n");
            }
            mVH.tv_result.setText(result.toString());
        } catch (Exception e) {
            mVH.tv_result.setText(query_model.getResult());
        }
        if(query_model.getHeaders()!=null&&query_model.getHeaders().size()!=0)
        {
            StringBuffer headers = new StringBuffer();
            try {
                String[] lines = stringToJson(new JSONObject(query_model.getHeaders()).toString()).split(System.getProperty("line.separator"));
                for (String line : lines) {
                    headers.append(line);
                    headers.append("\n");
                }
                mVH.tv_headers.setText(headers.toString());
            } catch (Exception e) {

                mVH.tv_headers.setText(new JSONObject(query_model.getHeaders()).toString());
            }
        }


        mShareData = "url:\n" + query_model.getUrl() + "\n" + "param:\n" + mVH.tv_param.getText().toString() + "\n" + "result:\n" + mVH.tv_result.getText().toString();

        mVH.layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mVH.tv_right_operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent it = new Intent("android.intent.action.SEND");
                it.putExtra("android.intent.extra.SUBJECT", "发送接口日志");
                it.putExtra("android.intent.extra.TEXT", mShareData);
                it.setType("text/plain");
                startActivity(Intent.createChooser(it, "发送接口日志"));
            }
        });
        mVH.btn_copy_url.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到剪贴板管理器
                ClipboardManager cmb = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText("url:\n" + mVH.tv_url.getText().toString());
                mShareData = "url:\n" + mVH.tv_url.getText().toString();
                Toast.makeText(LogDetailActivity.this, "粘贴url成功", Toast.LENGTH_SHORT).show();
            }
        });
        mVH.btn_copy_param.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到剪贴板管理器
                ClipboardManager cmb = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText("param:\n" + mVH.tv_param.getText().toString());
                mShareData = "param:\n" + mVH.tv_param.getText().toString();
                Toast.makeText(LogDetailActivity.this, "粘贴param成功", Toast.LENGTH_SHORT).show();
            }
        });
        mVH.btn_copy_result.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到剪贴板管理器
                ClipboardManager cmb = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText("result:\n" + mVH.tv_result.getText().toString());
                mShareData = "result:\n" + mVH.tv_result.getText().toString();
                Toast.makeText(LogDetailActivity.this, "粘贴result成功", Toast.LENGTH_SHORT).show();
            }
        });


        mVH.btn_copy_headers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 得到剪贴板管理器
                ClipboardManager cmb = (ClipboardManager) getApplication().getSystemService(Context.CLIPBOARD_SERVICE);
                cmb.setText("result:\n" + mVH.tv_headers.getText().toString());
                mShareData = "result:\n" + mVH.tv_headers.getText().toString();
                Toast.makeText(LogDetailActivity.this, "粘贴headers成功", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String stringToJson(String result) {
        int JSON_INDENT = 2;
        if (result.startsWith("{")) {
            JSONObject jsonObject = null;
            try {
                jsonObject = new JSONObject(result);
                return jsonObject.toString(JSON_INDENT);
            } catch (JSONException e) {
                e.printStackTrace();
                return result;
            }
        } else if (result.startsWith("[")) {
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(result);
                return jsonArray.toString(JSON_INDENT);
            } catch (JSONException e) {
                e.printStackTrace();
                return result;
            }

        }
        return result;
    }

    public static class ViewHolder {
        public ImageView layout_back;
        public TextView tv_title;
        public TextView tv_right_operate;
        public RelativeLayout layout_top;
        public Button btn_copy_url;
        public TextView tv_url;
        public Button btn_copy_param;
        public TextView tv_param;
        public Button btn_copy_result;
        public TextView tv_result;
        public Button btn_copy_headers;
        public TextView tv_headers;

        public ViewHolder(Activity rootView) {
            this.layout_back = (ImageView) rootView.findViewById(R.id.layout_back);
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_right_operate = (TextView) rootView.findViewById(R.id.tv_right_operate);
            this.layout_top = (RelativeLayout) rootView.findViewById(R.id.layout_top);
            this.btn_copy_url = (Button) rootView.findViewById(R.id.btn_copy_url);
            this.tv_url = (TextView) rootView.findViewById(R.id.tv_url);
            this.btn_copy_param = (Button) rootView.findViewById(R.id.btn_copy_param);
            this.tv_param = (TextView) rootView.findViewById(R.id.tv_param);
            this.btn_copy_result = (Button) rootView.findViewById(R.id.btn_copy_result);
            this.tv_result = (TextView) rootView.findViewById(R.id.tv_result);
            this.btn_copy_headers = (Button) rootView.findViewById(R.id.btn_copy_headers);
            this.tv_headers = (TextView) rootView.findViewById(R.id.tv_headers);
        }

    }
}
