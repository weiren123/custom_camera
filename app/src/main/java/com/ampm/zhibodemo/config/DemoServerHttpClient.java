package com.ampm.zhibodemo.config;

import android.app.Activity;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

import com.alibaba.fastjson.JSONException;
import com.alibaba.fastjson.JSONObject;
import com.ampm.zhibodemo.model.RoomInfoEntity;
import com.ampm.zhibodemo.utils.AndTools;
import com.netease.nim.uikit.common.http.NimHttpClient;
import com.netease.nim.uikit.common.util.log.LogUtil;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * 与应用服务器通信
 */
public class DemoServerHttpClient {
    private static final String TAG = "ContactHttpClient";

    //private static final String API_SERVER = "http://10.240.76.35:8080/"; //罗伟
    //private static final String API_SERVER = "http://106.2.44.145:8181/"; //测试
    public static final String API_SERVER = "http://vcloud.163.com/appdemo/";
    public static final String TEST_HOST = "vcloud.163.com"; //用于 ping ip地址,测试网络是否连通

    // code
    private static final int RESULT_CODE_SUCCESS = 200;

    // api
    private static final String API_NAME_REGISTER = "user/reg";
    private static final String API_LOGIN = "user/login";
    private static final String API_LOGOUT = "user/logout";
    private static final String API_CREATE_ROOM = "room/create";
    private static final String API_ENTER_ROOM = "room/enter";
    private static final String API_LEAVE_ROOM = "room/leave";

    // header
    private static final String HEADER_KEY_APP_KEY = "appkey";
    private static final String HEADER_CONTENT_TYPE = "Content-Type";
    private static final String HEADER_USER_AGENT = "User-Agent";

    // request login
    private static final String REQUEST_USER_NAME = "username";
    private static final String REQUEST_NICK_NAME = "nickname";
    private static final String REQUEST_PASSWORD = "password";

    //request room
    public static final String REQUEST_SID = "sid";
    public static final String REQUEST_ROOM_ID = "roomid";
    public static final String REQUEST_PULL_URL = "pullUrl";
    public static final String CID = "cid";
    public static final String REQUEST_DEVICE_ID = "deviceid";

    // result
    private static final String RESULT_KEY_CODE = "code";
    private static final String RESULT_KEY_ERROR_MSG = "msg";

    private static final String RESULT_KEY_RES = "ret";
    private static final String RESULT_KEY_TOKEN = "token";
    private static final String RESULT_KEY_SID = "sid";

    public static final String RESULT_ROOMID = "roomid";
    public static final String RESULT_PUSH_URL = "pushUrl";
    public static final String RESULT_RTMP_URL = "rtmpPullUrl";
    public static final String RESULT_HLS_URL = "hlsPullUrl";
    public static final String RESULT_HTTP_URL = "httpPullUrl";
    private static final String RESULT_OWNER = "owner";
    public static final String RESULT_STATUS = "status";


    public interface ContactHttpCallback<T> {
        void onSuccess(T t);

        void onFailed(int code, String errorMsg);
    }

    private static DemoServerHttpClient instance;

    public static synchronized DemoServerHttpClient getInstance() {
        if (instance == null) {
            instance = new DemoServerHttpClient();
        }

        return instance;
    }

    private DemoServerHttpClient() {
        NimHttpClient.getInstance().init(DemoCache.getContext());
    }

    /**
     * 向应用服务器创建账号（注册账号）
     * 由应用服务器调用WEB SDK接口将新注册的用户数据同步到云信服务器
     */
    public void register(String account, String nickName, String password, final ContactHttpCallback<Void> callback) {
        String url = API_SERVER + API_NAME_REGISTER;
        //password = MD5.getStringMD5(password);
        try {
            nickName = URLEncoder.encode(nickName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

        Map<String, String> headers = new HashMap<>(1);
        String appKey = readAppKey();
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");
//        headers.put(HEADER_USER_AGENT, "nim_demo_android");
//        headers.put(HEADER_KEY_APP_KEY, appKey);

        StringBuilder body = new StringBuilder();
        body.append(REQUEST_USER_NAME).append("=").append(account.toLowerCase()).append("&")
                .append(REQUEST_NICK_NAME).append("=").append(nickName).append("&")
                .append(REQUEST_PASSWORD).append("=").append(password);

        NimHttpClient.getInstance().execute(url, headers, body.toString(), new NimHttpClient.NimHttpCallback() {
            @Override
            public void onResponse(String response, int code, Throwable exception) {
                if (code != 200 || exception != null) {
                    LogUtil.e(TAG, "register failed : code = " + code + ", errorMsg = "
                            + (exception != null ? exception.getMessage() : "null"));
                    if (callback != null) {
                        callback.onFailed(code, exception != null ? exception.getMessage() : "null");
                    }
                    return;
                }

                try {
                    JSONObject resObj = JSONObject.parseObject(response);
                    int resCode = resObj.getIntValue(RESULT_KEY_CODE);
                    if (resCode == RESULT_CODE_SUCCESS) {
                        callback.onSuccess(null);
                    } else {
                        String error = resObj.getString(RESULT_KEY_ERROR_MSG);
                        callback.onFailed(resCode, error);
                    }
                } catch (JSONException e) {
                    callback.onFailed(-1, e.getMessage());
                }
            }
        });
    }


    /**
     * 向应用服务器创建账号（注册账号）
     * 由应用服务器调用WEB SDK接口将新注册的用户数据同步到云信服务器
     */
    public void login(String account, String password, final ContactHttpCallback<Void> callback) {
        String url = API_SERVER + API_LOGIN;

        Map<String, String> headers = new HashMap<>(1);
        String appKey = readAppKey();
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");
//        headers.put(HEADER_USER_AGENT, "nim_demo_android");
//        headers.put(HEADER_KEY_APP_KEY, appKey);

        StringBuilder body = new StringBuilder();
        body.append(REQUEST_USER_NAME).append("=").append(account.toLowerCase()).append("&")
                .append(REQUEST_PASSWORD).append("=").append(password);

        NimHttpClient.getInstance().execute(url, headers, body.toString(), new NimHttpClient.NimHttpCallback() {
            @Override
            public void onResponse(String response, int code, Throwable exception) {
                if (code != 200 || exception != null) {
                    LogUtil.e(TAG, "register failed : code = " + code + ", errorMsg = "
                            + (exception != null ? exception.getMessage() : "null"));
                    if (callback != null) {
                        callback.onFailed(code, exception != null ? exception.getMessage() : "null");
                    }
                    return;
                }

                try {
                    JSONObject resObj = JSONObject.parseObject(response);
                    int resCode = resObj.getIntValue(RESULT_KEY_CODE);
                    if (resCode == RESULT_CODE_SUCCESS) {
                        JSONObject retObj = resObj.getJSONObject(RESULT_KEY_RES);
                        DemoCache.setAccount(resObj.getString(REQUEST_USER_NAME));
                        DemoCache.setToken(retObj.getString(RESULT_KEY_TOKEN));
                        DemoCache.setSid(retObj.getString(RESULT_KEY_SID));
                        callback.onSuccess(null);
                    } else {
                        String error = resObj.getString(RESULT_KEY_ERROR_MSG);
                        callback.onFailed(resCode, error);
                    }
                } catch (JSONException e) {
                    callback.onFailed(-1, e.getMessage());
                } catch (Exception e){
                    callback.onFailed(-1, e.getMessage());
                }
            }
        });
    }

    /**
     * 向应用服务器发送登出请求
     */
    public void logout(){
        String url = API_SERVER + API_LOGOUT;

        Map<String, String> headers = new HashMap<>(1);
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");

        StringBuilder body = new StringBuilder();
        body.append(REQUEST_SID).append("=").append(DemoCache.getSid());

        NimHttpClient.getInstance().execute(url, headers, body.toString(), new NimHttpClient.NimHttpCallback() {
            @Override
            public void onResponse(String response, int code, Throwable exception) {
                //登出不保证是否成功
            }
        });
    }

    public void createRoom(Activity context, final ContactHttpCallback<RoomInfoEntity> callback){
        String url = API_SERVER + API_CREATE_ROOM;

        Map<String, String> headers = new HashMap<>(1);
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");

        StringBuilder body = new StringBuilder();
        body.append(REQUEST_SID).append("=").append(DemoCache.getSid()).append("&")
            .append(REQUEST_DEVICE_ID).append("=").append(AndTools.getDeviceId(context));

        NimHttpClient.getInstance().execute(url, headers, body.toString(), new NimHttpClient.NimHttpCallback() {
            @Override
            public void onResponse(String response, int code, Throwable exception) {
                if (code != 200 || exception != null) {
                    LogUtil.e(TAG, "register failed : code = " + code + ", errorMsg = "
                            + (exception != null ? exception.getMessage() : "null"));
                    if (callback != null) {
                        callback.onFailed(code, exception != null ? exception.getMessage() : "null");
                    }
                    return;
                }
                try {
                    JSONObject resObj = JSONObject.parseObject(response);
                    int resCode = resObj.getIntValue(RESULT_KEY_CODE);
                    if (resCode == RESULT_CODE_SUCCESS) {
                        JSONObject retObj = resObj.getJSONObject(RESULT_KEY_RES);
                        RoomInfoEntity roomInfoEntity = new RoomInfoEntity();
                        roomInfoEntity.setRoomid(retObj.getInteger(RESULT_ROOMID));
                        roomInfoEntity.setPushUrl(retObj.getString(RESULT_PUSH_URL));
                        roomInfoEntity.setRtmpPullUrl(retObj.getString(RESULT_RTMP_URL));
                        roomInfoEntity.setHlsPullUrl(retObj.getString(RESULT_HLS_URL));
                        roomInfoEntity.setHttpPullUrl(retObj.getString(RESULT_HTTP_URL));
                        roomInfoEntity.setCid(retObj.getString(CID));
                        callback.onSuccess(roomInfoEntity);
                    } else {
                        String error = resObj.getString(RESULT_KEY_ERROR_MSG);
                        callback.onFailed(resCode, error);
                    }
                } catch (JSONException e) {
                    callback.onFailed(-1, e.getMessage());
                } catch (Exception e){
                    callback.onFailed(-1, e.getMessage());
                }
            }
        });
    }

    public void getRoomInfo(int mode, String address, final ContactHttpCallback<RoomInfoEntity> callback){
        String url = API_SERVER + API_ENTER_ROOM;

        Map<String, String> headers = new HashMap<>(1);
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");

        StringBuilder body = new StringBuilder();
        body.append(REQUEST_SID).append("=").append(DemoCache.getSid()).append("&");
//        if(mode == EnterAudienceActivity.MODE_ROOM){
//            body.append(REQUEST_ROOM_ID);
//        }else{
//            body.append(REQUEST_PULL_URL);
//        }
        body.append("=").append(address);

        NimHttpClient.getInstance().execute(url, headers, body.toString(), new NimHttpClient.NimHttpCallback() {
            @Override
            public void onResponse(String response, int code, Throwable exception) {
                if (code != 200 || exception != null) {
                    LogUtil.e(TAG, "register failed : code = " + code + ", errorMsg = "
                            + (exception != null ? exception.getMessage() : "null"));
                    if (callback != null) {
                        callback.onFailed(code, exception != null ? exception.getMessage() : "null");
                    }
                    return;
                }
                try {
                    JSONObject resObj = JSONObject.parseObject(response);
                    int resCode = resObj.getIntValue(RESULT_KEY_CODE);
                    if (resCode == RESULT_CODE_SUCCESS) {
                        JSONObject retObj = resObj.getJSONObject(RESULT_KEY_RES);
                        RoomInfoEntity roomInfoEntity = new RoomInfoEntity();
                        roomInfoEntity.setRoomid(retObj.getInteger(RESULT_ROOMID));
                        roomInfoEntity.setPushUrl(retObj.getString(RESULT_PUSH_URL));
                        roomInfoEntity.setRtmpPullUrl(retObj.getString(RESULT_RTMP_URL));
                        roomInfoEntity.setHlsPullUrl(retObj.getString(RESULT_HLS_URL));
                        roomInfoEntity.setHttpPullUrl(retObj.getString(RESULT_HTTP_URL));
                        roomInfoEntity.setOwner(retObj.getString(RESULT_OWNER));
                        roomInfoEntity.setStatus(retObj.getInteger(RESULT_STATUS));
                        callback.onSuccess(roomInfoEntity);
                    } else {
                        String error = resObj.getString(RESULT_KEY_ERROR_MSG);
                        callback.onFailed(resCode, error);
                    }
                } catch (JSONException e) {
                    callback.onFailed(-1, e.getMessage());
                } catch (Exception e){
                    callback.onFailed(-1, e.getMessage());
                }
            }
        });
    }

    /**
     * 主播退出直播时,调用该接口,通知解散聊天室
     */
    public void anchorLeave(String roomId, final ContactHttpCallback<Void> callback){
        String url = API_SERVER + API_LEAVE_ROOM;
        Map<String, String> headers = new HashMap<>(1);
        headers.put(HEADER_CONTENT_TYPE, "application/x-www-form-urlencoded; charset=utf-8");

        StringBuilder body = new StringBuilder();
        body.append(REQUEST_SID).append("=").append(DemoCache.getSid()).append("&").append(REQUEST_ROOM_ID)
        .append("=").append(roomId);

        NimHttpClient.getInstance().execute(url, headers, body.toString(), new NimHttpClient.NimHttpCallback() {
            @Override
            public void onResponse(String response, int code, Throwable exception) {
                if (code != 200 || exception != null) {
                    LogUtil.e(TAG, "register failed : code = " + code + ", errorMsg = "
                            + (exception != null ? exception.getMessage() : "null"));
                    callback.onFailed(code, exception.getMessage());
                    return;
                }
                try {
                    JSONObject resObj = JSONObject.parseObject(response);
                    int resCode = resObj.getIntValue(RESULT_KEY_CODE);
                    if (resCode == RESULT_CODE_SUCCESS) {
                        callback.onSuccess(null);
                    } else {
                        String error = resObj.getString(RESULT_KEY_ERROR_MSG);
                        callback.onFailed(resCode, error);
                    }
                } catch (JSONException e) {
                } catch (Exception e){
                }
            }
        });

    }

    public static String readAppKey() {
        try {
            ApplicationInfo appInfo = DemoCache.getContext().getPackageManager()
                    .getApplicationInfo(DemoCache.getContext().getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
