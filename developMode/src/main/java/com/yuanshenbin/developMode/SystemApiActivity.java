package com.yuanshenbin.developMode;

import android.app.Activity;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


/**
 * author : yuanshenbin
 * time   : 2018/8/29
 * desc   :
 */

public class SystemApiActivity extends DevelopBaseActivity {
    private ViewHolder mVH;
    private List<SystemApiModel> mApiModels = new ArrayList<>();
    private ApiAdapter mAdapter;
    private SystemApiModel mApiModel = new SystemApiModel();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.develop_mode_activity_systemapi);
        mVH = new ViewHolder(this);
        mVH.tv_title.setText("系统api切换");
        mVH.tv_right_operate.setText("确定切换");

        mVH.ed_code.setText(getAppCode() + "");
        mVH.ed_version.setText(getAppVersion());

        mApiModels.clear();
        mApiModels.addAll(DevelopMode.getInstance().getDevelopConfig().getApiModels());


        mVH.layout_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mVH.tv_right_operate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SystemApiModel model = new SystemApiModel();
                if (mApiModel.getType() == 999) {
                    //自定义
                    if (TextUtils.isEmpty(mVH.ed_api.getText().toString().trim())) {
                        Toast.makeText(SystemApiActivity.this, "请选择需要切换api地址", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    model.setAppCode(TextUtils.isEmpty(mVH.ed_code.getText().toString().trim()) ? 0 : Integer.parseInt(mVH.ed_code.getText().toString().trim()));
                    model.setAppVersion(mVH.ed_version.getText().toString().trim());

                    String apis = mVH.ed_api.getText().toString();
                    model.setApiUrl(apis.split(","));

                    model.setType(mApiModel.getType());
                    model.setAmbient(mApiModel.getAmbient());
                } else {
                    model = mApiModel;
                    model.setAppCode(TextUtils.isEmpty(mVH.ed_code.getText().toString().trim()) ? 0 : Integer.parseInt(mVH.ed_code.getText().toString().trim()));
                    model.setAppVersion(mVH.ed_version.getText().toString().trim());

                }
                if (DevelopMode.getInstance().getListener() != null) {
                    DevelopMode.getInstance().getListener().onSwitch(model);
                } else {
                    Toast.makeText(SystemApiActivity.this, "需实现回调接口", Toast.LENGTH_SHORT).show();
                }
                DevelopMode.getInstance().clear();

            }
        });

        mAdapter = new ApiAdapter();
        mVH.list.setAdapter(mAdapter);
        mVH.list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


                mApiModel =mApiModels.get(position);
                for (int i = 0; i < mApiModels.size(); i++) {
                    mApiModels.get(i).setChecked(false);
                }
                mApiModels.get(position).setChecked(true);
                StringBuffer api = new StringBuffer();

                if (mApiModels.get(position).getApiUrl() != null) {
                    String[] urls = mApiModels.get(position).getApiUrl();
                    for (int i = 0; i < urls.length; i++) {
                        api.append(urls[i]);
                        if (urls.length - 1 != i) {
                            api.append(",");
                            api.append("\n");
                        }

                    }
                }
                mVH.ed_api.setText(api.toString());
                mAdapter.notifyDataSetChanged();


            }
        });

    }


    class ViewHolder {
        public ImageView layout_back;
        public TextView tv_title;
        public TextView tv_right_operate;
        public ListView list;
        public EditText ed_api;
        public EditText ed_code;
        public EditText ed_version;

        public ViewHolder(Activity rootView) {
            this.layout_back = (ImageView) rootView.findViewById(R.id.layout_back);
            this.tv_title = (TextView) rootView.findViewById(R.id.tv_title);
            this.tv_right_operate = (TextView) rootView.findViewById(R.id.tv_right_operate);
            this.list = (ListView) rootView.findViewById(R.id.list);
            this.ed_api = (EditText) rootView.findViewById(R.id.ed_api);
            this.ed_code = (EditText) rootView.findViewById(R.id.ed_code);
            this.ed_version = (EditText) rootView.findViewById(R.id.ed_version);
        }

    }

    private String getAppVersion() {
        String version = "";

        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            version = packInfo.versionName;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return version;
    }

    private int getAppCode() {
        int code = 0;

        try {
            PackageManager packageManager = this.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(this.getPackageName(), 0);
            code = packInfo.versionCode;
        } catch (Exception var4) {
            var4.printStackTrace();
        }

        return code;
    }

    private class ApiAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mApiModels.size();
        }

        @Override
        public Object getItem(int position) {
            return mApiModels.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder item;
            if (convertView == null) {
                convertView = View.inflate(parent.getContext(), R.layout.develop_mode_item_activity_systemapi, null);
                item = new ViewHolder(convertView);
                convertView.setTag(item);
            } else {
                item = (ViewHolder) convertView.getTag();
            }

            SystemApiModel data = mApiModels.get(position);
            item.setShow(data);
            return convertView;
        }

        public class ViewHolder {
            public TextView tv_api_name, tv_api;
            public LinearLayout ll_api_bg;

            public ViewHolder(View view) {
                tv_api_name = (TextView) view.findViewById(R.id.tv_api_name);
                tv_api = (TextView) view.findViewById(R.id.tv_api);
                ll_api_bg = view.findViewById(R.id.ll_api_bg);

            }

            public void setShow(SystemApiModel data) {

                StringBuffer name = new StringBuffer();

                for (int i = 0; i < data.getAmbient().length; i++) {
                    name.append(data.getAmbient()[i]);
                    if (data.getAmbient().length - 1 != i) {
                        name.append(" ➯  ");
                    }
                }
                tv_api_name.setText(name.toString());

                StringBuffer api = new StringBuffer();

                if (data.getApiUrl() != null) {
                    for (int i = 0; i < data.getApiUrl().length; i++) {
                        api.append(data.getApiUrl()[i]);
                        if (data.getApiUrl().length - 1 != i) {
                            api.append("\n");
                        }

                    }
                }
                tv_api.setText(api.toString());

                if (data.isChecked()) {
                    ll_api_bg.setBackgroundColor(Color.parseColor("#FF8C8787"));
                } else {
                    ll_api_bg.setBackgroundColor(Color.parseColor("#ffffff"));
                }
            }
        }
    }
}
