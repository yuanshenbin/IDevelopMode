package com.yuanshenbin.developMode;


import android.content.Context;

import java.util.List;


/**
 * author : yuanshenbin
 * time   : 2018/8/30
 * desc   :
 */

public class DevelopConfig {

    private Mode mode;

    private boolean debug;

    private List<SystemApiModel> apiModels;

    private int windowIcon;

    private Context context;
    private CustomRootListener mRootListener;


    public enum Mode {
        VIEW, WINDOW, NOTICE
    }

    public DevelopConfig(final Builder builder) {
        mode = builder.mode;
        debug = builder.debug;
        apiModels = builder.apiModels;
        if (apiModels == null || apiModels.size() == 0) {
            throw new ExceptionInInitializerError("Please invoke  SystemApi~");
        }
        windowIcon = builder.windowIcon;
        context = builder.context;
        mRootListener =builder.rootListener;

    }

    public static final class Builder {

        public Builder(Context context) {
            this.context = context;
        }

        private Mode mode = Mode.WINDOW;

        private boolean debug;

        private List<SystemApiModel> apiModels;

        private int windowIcon = R.drawable.develop_mode_ic_launcher_round;

        private Context context;
        private CustomRootListener rootListener;

        public Builder mode(Mode mode) {
            this.mode = mode;
            return this;
        }

        public Builder systemApi(List<SystemApiModel> apiModels) {
            SystemApiModel systemApiModel = new SystemApiModel();
            systemApiModel.setAmbient("自定义");
            systemApiModel.setType(999);
            apiModels.add(systemApiModel);
            this.apiModels = apiModels;
            return this;
        }

        public Builder windowIcon(int windowIcon) {
            this.windowIcon = windowIcon;
            return this;
        }

        public Builder debug(boolean debug) {
            this.debug = debug;
            return this;
        }

        public Builder customRoot(CustomRootListener rootListener) {
            this.rootListener = rootListener;
            return this;
        }

        public DevelopConfig build() {
            return new DevelopConfig(this);
        }
    }

    public Mode getMode() {
        return mode;
    }

    public boolean isDebug() {
        return debug;
    }

    public List<SystemApiModel> getApiModels() {
        for (int i = 0; i < apiModels.size(); i++) {
            apiModels.get(i).setChecked(false);
        }
        return apiModels;
    }

    public int getWindowIcon() {
        return windowIcon;
    }

    public Context getContext() {
        return context;
    }

    public CustomRootListener getRootListener() {
        return mRootListener;
    }

    public void setRootListener(CustomRootListener rootListener) {
        mRootListener = rootListener;
    }
}
