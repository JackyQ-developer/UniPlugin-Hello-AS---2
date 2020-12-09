package com.example.jqplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
//import com.tencent.imsdk.v2.V2TIMCallback;
//import com.tencent.imsdk.v2.V2TIMManager;
//import com.tencent.imsdk.v2.V2TIMSDKConfig;
//import com.tencent.imsdk.v2.V2TIMSDKListener;

import com.tencent.qcloud.tim.uikit.TUIKit;
import com.tencent.qcloud.tim.uikit.base.IUIKitCallBack;
import com.tencent.qcloud.tim.uikit.config.CustomFaceConfig;
import com.tencent.qcloud.tim.uikit.config.GeneralConfig;
import com.tencent.qcloud.tim.uikit.config.TUIKitConfigs;


import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

public class Trtc extends UniModule {
    final String TAG = "JQ-TRTC";
    private UniJSCallback uniJSCallback;
    private static Boolean loginStatus = false;

    /**
     * IM登录
     * @param options
     * @param callback
     */
    /*@UniJSMethod(uiThread = true)
    public void config(final JSONObject options, UniJSCallback callback) {
        uniJSCallback = callback;
        if(callback != null) {
            JSONObject data = new JSONObject();
            data.put("code", "success");
            V2TIMSDKConfig config = new V2TIMSDKConfig();
            config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO);
            Context context = mUniSDKInstance.getContext();
            int sdkAppID = (int) options.get("appid");

            V2TIMManager.getInstance().initSDK(context, sdkAppID, config, new V2TIMSDKListener() {
                // 5. 监听 V2TIMSDKListener 回调
                @Override
                public void onConnecting() {
                    // 正在连接到腾讯云服务器
                    Log.e(TAG, "正在连接到腾讯云服务器");
                    JSONObject data = new JSONObject();
                    data.put("code", "正在登录");
                    uniJSCallback.invokeAndKeepAlive(data);
                }
                @Override
                public void onConnectSuccess() {
                    // 已经成功连接到腾讯云服务器
                    Log.e(TAG, "已经成功连接到腾讯云服务器");
                    V2TIMManager.getInstance().login((String) options.get("userId"), (String) options.get("userSig"), new V2TIMCallback() {

                        @Override
                        public void onError(int code, String desc) {
                            JSONObject data = new JSONObject();
                            data.put("code", "登录失败");
                            Trtc.loginStatus = true;
                            uniJSCallback.invoke(data);
                        }

                        @Override
                        public void onSuccess() {
                            JSONObject data = new JSONObject();
                            data.put("code", "登录成功");
                            Trtc.loginStatus = true;
                            uniJSCallback.invoke(data);
                        }
                    });
                }
                @Override
                public void onConnectFailed(int code, String error) {
                    // 连接腾讯云服务器失败
                    Log.e(TAG, "连接腾讯云服务器失败");
                    JSONObject data = new JSONObject();
                    data.put("code", "登录失败");
                    uniJSCallback.invoke(data);
                }
            });
        }
    }*/

    @UniJSMethod(uiThread = false)
    public void config(final JSONObject options, UniJSCallback callback) {
        uniJSCallback = callback;
        if(callback != null) {
            TUIKitConfigs configs = TUIKit.getConfigs();
//            configs.setSdkConfig(new V2TIMSDKConfig());
//            configs.setCustomFaceConfig(new CustomFaceConfig());
//            configs.setGeneralConfig(new GeneralConfig());
            Context context = mUniSDKInstance.getContext();
            int sdkAppID = (int) options.get("appid");
            TUIKit.init(context, sdkAppID, configs);
            TUIKit.login((String) options.get("userId"), (String) options.get("userSig"), new IUIKitCallBack() {
                @Override
                public void onSuccess(Object data) {
                    // 登录成功
                    JSONObject result = new JSONObject();
                    result.put("code", "登录成功");
                    uniJSCallback.invoke(result);
                }

                @Override
                public void onError(String module, final int code, final String desc) {
                    // 登录失败
                    JSONObject result = new JSONObject();
                    result.put("code", "登录失败");
                    uniJSCallback.invoke(result);
                }
            });
        }
    }
    /**
     * 拨打电话
     * @param options
     * @param callback
     */
    @UniJSMethod(uiThread = true)
    public void call(JSONObject options, UniJSCallback callback) {
        Log.e(TAG, "testAsyncFunc--"+options);
        if (!loginStatus) {
            JSONObject data = new JSONObject();
            data.put("code", "未登录");
            callback.invoke(data);
            return;
        }
        Activity currentActivity = ((Activity)mUniSDKInstance.getContext());
        Intent intent=new Intent(mUniSDKInstance.getContext(), MainActivity.class);
        currentActivity.startActivity(intent);
    }
}
