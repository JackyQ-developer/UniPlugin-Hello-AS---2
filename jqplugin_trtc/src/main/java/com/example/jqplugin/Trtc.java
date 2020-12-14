package com.example.jqplugin;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.example.jqplugin.model.TRTCCalling;
import com.example.jqplugin.model.impl.TRTCCallingImpl;
import com.tencent.imsdk.v2.V2TIMCallback;
import com.tencent.imsdk.v2.V2TIMManager;
import com.tencent.imsdk.v2.V2TIMSDKConfig;
import com.tencent.imsdk.v2.V2TIMSDKListener;


import io.dcloud.feature.uniapp.annotation.UniJSMethod;
import io.dcloud.feature.uniapp.bridge.UniJSCallback;
import io.dcloud.feature.uniapp.common.UniModule;

import com.example.jqplugin.ui.videocall.TRTCVideoCallActivity;

import java.util.ArrayList;
import java.util.List;

public class Trtc extends UniModule {
    final String TAG = "JQ-TRTC";
    public UniJSCallback uniJSCallback = null;

    /**
     * IM登录
     * @param options
     * @param callback
     */
    @UniJSMethod(uiThread = true)
    public void config(final JSONObject options, UniJSCallback callback) {
        if(callback != null) {
            uniJSCallback = callback;

            V2TIMSDKConfig config = new V2TIMSDKConfig();
            config.setLogLevel(V2TIMSDKConfig.V2TIM_LOG_INFO);
            Context context = mUniSDKInstance.getContext();
            int sdkAppID = (int) options.get("appid");
            String userId = (String) options.get("userId");
            String userSig = (String) options.get("userSig");

            TRTCCalling sCall = TRTCCallingImpl.sharedInstance(context);
            sCall.login(sdkAppID, userId, userSig, new TRTCCalling.ActionCallBack() {
                @Override
                public void onError(int code, String msg) {
                    JSONObject data = new JSONObject();
                    data.put("code", "登录失敗");
                    uniJSCallback.invoke(data);
                }

                @Override
                public void onSuccess() {
                    JSONObject data = new JSONObject();
                    data.put("code", "登录成功");
                    uniJSCallback.invoke(data);
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
        int loginStatus = V2TIMManager.getInstance().getLoginStatus();
        if (loginStatus == V2TIMManager.V2TIM_STATUS_LOGOUT) {
            JSONObject data = new JSONObject();
            data.put("code", "未登录");
            callback.invoke(data);
            return;
        } else if (loginStatus == V2TIMManager.V2TIM_STATUS_LOGINING) {
            JSONObject data = new JSONObject();
            data.put("code", "登录中");
            callback.invoke(data);
            return;
        }
        Activity currentActivity = ((Activity)mUniSDKInstance.getContext());

        TRTCVideoCallActivity.UserInfo selfInfo = new TRTCVideoCallActivity.UserInfo();
        selfInfo.userId = (String) options.get("userId");
        selfInfo.userAvatar = (String) options.get("userAvatar");
        selfInfo.userName = (String) options.get("userName");

        List<TRTCVideoCallActivity.UserInfo> callUserInfoList = new ArrayList<>();
        TRTCVideoCallActivity.UserInfo callUserInfo = new TRTCVideoCallActivity.UserInfo();
        JSONObject targetTmp = (JSONObject) options.get("targetInfo");
        callUserInfo.userId = (String) targetTmp.get("userId");
        callUserInfo.userAvatar = (String) targetTmp.get("userAvatar");
        callUserInfo.userName = (String) targetTmp.get("userName");
        callUserInfoList.add(callUserInfo);
        TRTCVideoCallActivity.startCallSomeone(mUniSDKInstance.getContext(), selfInfo, callUserInfoList);
    }
}
