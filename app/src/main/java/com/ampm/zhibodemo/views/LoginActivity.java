package com.ampm.zhibodemo.views;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.ampm.zhibodemo.R;
import com.ampm.zhibodemo.config.DemoCache;
import com.ampm.zhibodemo.config.DemoServerHttpClient;
import com.ampm.zhibodemo.config.Preferences;
import com.ampm.zhibodemo.config.UserPreferences;
import com.netease.nim.uikit.cache.DataCacheManager;
import com.netease.nim.uikit.common.ui.dialog.DialogMaker;
import com.netease.nim.uikit.common.ui.widget.ClearableEditTextWithIcon;
import com.netease.nim.uikit.common.util.log.LogUtil;
import com.netease.nim.uikit.common.util.sys.NetworkUtil;
import com.netease.nim.uikit.permission.annotation.OnMPermissionGranted;
import com.netease.nimlib.sdk.AbortableFuture;
import com.netease.nimlib.sdk.NIMClient;
import com.netease.nimlib.sdk.RequestCallback;
import com.netease.nimlib.sdk.auth.AuthService;
import com.netease.nimlib.sdk.auth.LoginInfo;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;


/**
 * 登录/注册界面
 * <p/>
 * Created by huangjun on 2015/2/1.
 */
public class LoginActivity extends Activity {

    private static final String TAG = LoginActivity.class.getSimpleName();
    private static final String KICK_OUT = "KICK_OUT";
    private final int BASIC_PERMISSION_REQUEST_CODE = 110;
    @Bind(R.id.iv_logo)
    ImageView ivLogo;
    @Bind(R.id.tv_title)
    TextView tvTitle;
    @Bind(R.id.edit_login_account)
    ClearableEditTextWithIcon loginAccountEdit;
    @Bind(R.id.edit_login_password)
    ClearableEditTextWithIcon loginPasswordEdit;
    @Bind(R.id.edit_register_account)
    ClearableEditTextWithIcon editRegisterAccount;
    @Bind(R.id.edit_register_nickname)
    ClearableEditTextWithIcon editRegisterNickname;
    @Bind(R.id.edit_register_password)
    ClearableEditTextWithIcon editRegisterPassword;
    @Bind(R.id.btn_login_register)
    Button btnLoginRegister;
    @Bind(R.id.register_login_tip)
    TextView registerLoginTip;
    @Bind(R.id.login_root)
    LinearLayout loginRoot;

    private TextView loginRegisterBtn;  // 注册/登录 完成按钮
    private TextView switchModeBtn;  // 注册/登录切换按钮

//    private ClearableEditTextWithIcon loginAccountEdit;
//    private ClearableEditTextWithIcon loginPasswordEdit;

    private ClearableEditTextWithIcon registerAccountEdit;
    private ClearableEditTextWithIcon registerNickNameEdit;
    private ClearableEditTextWithIcon registerPasswordEdit;

    private View loginLayout;
    private View registerLayout;

    private LinearLayout rootView;
    private View iv_logo;
    private TextView tv_title;
    private float screenHeight;

    private AbortableFuture<LoginInfo> loginRequest;
    private boolean registerMode = false; // 注册模式
    private boolean registerPanelInited = false; // 注册面板是否初始化


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
        ButterKnife.bind(this);
//        onParseIntent();
//        initTitleView();
//        initLoginRegisterBtn();
//        setupLoginPanel();
//        setupRegisterPanel();
    }


    /**
     * 初始化按钮
     */
//    private void initLoginRegisterBtn() {
//        loginRegisterBtn = findView(R.id.btn_login_register);
//        loginRegisterBtn.setOnClickListener(new OnClickListener() {
//
//            @Override
//            public void onClick(View v) {
//                login();
////                if (registerMode) {
////                    register();
////                } else {
////                    //fakeLoginTest(); // 假登录代码示例
//////                    if(!checkWritePermission()){
//////                        return;
//////                    }
////                }
//            }
//        });
//    }


    /**
     * 注册面板
     */
//    private void setupRegisterPanel() {
//        loginLayout = findView(R.id.login_layout);
//        registerLayout = findView(R.id.register_layout);
//        switchModeBtn = findView(R.id.register_login_tip);
//
//        switchModeBtn.setOnClickListener(new OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                switchMode();
//            }
//        });
//    }

    private TextWatcher textWatcher = new TextWatcher() {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            // 更新右上角按钮状态
            if (!registerMode) {
                // 登录模式
                boolean isEnable = loginAccountEdit.getText().length() > 0
                        && loginPasswordEdit.getText().length() > 0;
                loginRegisterBtn.setEnabled(isEnable);
            }
        }
    };

    /**
     * ***************************************** 登录 **************************************
     */

    /**
     * 登录应用服务器
     */
    private void login() {
//        DialogMaker.showProgressDialog(this, null, "登录中...", true, new DialogInterface.OnCancelListener() {
//            @Override
//            public void onCancel(DialogInterface dialog) {
//                if (loginRequest != null) {
//                    loginRequest.abort();
//                    onLoginDone();
//                }
//            }
//        }).setCanceledOnTouchOutside(false);
        loginNim(loginAccountEdit.getEditableText().toString().toLowerCase(), DemoCache.getToken());
//        DemoServerHttpClient.getInstance().login(loginAccountEdit.getEditableText().toString().toLowerCase(), loginPasswordEdit.getEditableText().toString(), new DemoServerHttpClient.ContactHttpCallback<Void>() {
//            @Override
//            public void onSuccess(Void aVoid) {
//                loginNim(loginAccountEdit.getEditableText().toString().toLowerCase(), DemoCache.getToken());
//            }
//
//            @Override
//            public void onFailed(int code, String errorMsg) {
////                onLoginDone();
//                Toast.makeText(LoginActivity.this, "登录失败: " + errorMsg, Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    /**
     * 登录云信服务器
     *
     * @param account
     * @param token
     */
    private void loginNim(final String account, final String token) {

        // 登录
        loginRequest = NIMClient.getService(AuthService.class).login(new LoginInfo(account, token, readAppKey()));
        loginRequest.setCallback(new RequestCallback<LoginInfo>() {
            @Override
            public void onSuccess(LoginInfo param) {
                LogUtil.i(TAG, "login success");

//                onLoginDone();
                DemoCache.setAccount(account);
                saveLoginInfo(account, token);

                // 初始化消息提醒
                NIMClient.toggleNotification(UserPreferences.getNotificationToggle());

                // 初始化免打扰
                if (UserPreferences.getStatusConfig() == null) {
                    UserPreferences.setStatusConfig(DemoCache.getNotificationConfig());
                }
                NIMClient.updateStatusBarNotificationConfig(UserPreferences.getStatusConfig());

                // 构建缓存
                DataCacheManager.buildDataCacheAsync();

                // 进入主界面
                Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }

            @Override
            public void onFailed(int code) {
                onLoginDone();
                if (code == 302 || code == 404) {
                    Toast.makeText(LoginActivity.this, "帐号或密码错误", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "登录失败: " + code, Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onException(Throwable exception) {
                Toast.makeText(LoginActivity.this, "无效输入", Toast.LENGTH_LONG).show();
                onLoginDone();
            }
        });
    }

    private boolean checkWritePermission() {
        if (bWritePermission) {
            return true;
        } else {
//            requestBasicPermission();
            Toast.makeText(LoginActivity.this, "授权读写存储卡权限后,才能正常使用", Toast.LENGTH_LONG).show();
            return false;
        }
    }

    private boolean bWritePermission;

    @OnMPermissionGranted(BASIC_PERMISSION_REQUEST_CODE)
    public void onBasicPermissionSuccess() {
        //Toast.makeText(this, "授权成功", Toast.LENGTH_SHORT).show();
        bWritePermission = true;
    }

    private void onLoginDone() {
        loginRequest = null;
        DialogMaker.dismissProgressDialog();
    }

    private void saveLoginInfo(final String account, final String token) {
        Preferences.saveUserAccount(account);
        Preferences.saveUserToken(token);
    }

    private String readAppKey() {
        try {
            ApplicationInfo appInfo = getPackageManager().getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            if (appInfo != null) {
                return appInfo.metaData.getString("com.netease.nim.appKey");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * ***************************************** 注册 **************************************
     */

    private void register() {
        if (!registerMode || !registerPanelInited) {
            return;
        }

        if (!checkRegisterContentValid()) {
            return;
        }

        if (!NetworkUtil.isNetAvailable(LoginActivity.this)) {
            Toast.makeText(LoginActivity.this, R.string.network_is_not_available, Toast.LENGTH_SHORT).show();
            return;
        }

        DialogMaker.showProgressDialog(this, "注册中...", false);

        // 注册流程
        final String account = registerAccountEdit.getText().toString();
        final String nickName = registerNickNameEdit.getText().toString();
        final String password = registerPasswordEdit.getText().toString();

        DemoServerHttpClient.getInstance().register(account, nickName, password, new DemoServerHttpClient.ContactHttpCallback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Toast.makeText(LoginActivity.this, "注册成功", Toast.LENGTH_SHORT).show();
//                switchMode();  // 切换回登录
                loginAccountEdit.setText(account);
                loginPasswordEdit.setText(password);

                registerAccountEdit.setText("");
                registerNickNameEdit.setText("");
                registerPasswordEdit.setText("");

                DialogMaker.dismissProgressDialog();
            }

            @Override
            public void onFailed(int code, String errorMsg) {
                Toast.makeText(LoginActivity.this, "注册失败,错误码%1$s:%2$s", Toast.LENGTH_SHORT)
                        .show();

                DialogMaker.dismissProgressDialog();
            }
        });
    }

    private boolean checkRegisterContentValid() {
        if (!registerMode || !registerPanelInited) {
            return false;
        }

        // 帐号检查
        String account = registerAccountEdit.getText().toString().trim();
        if (account.length() <= 0 || account.length() > 20) {
            Toast.makeText(this, "帐号限20位字母或者数字", Toast.LENGTH_SHORT).show();

            return false;
        }

        // 昵称检查
        String nick = registerNickNameEdit.getText().toString().trim();
        if (!nick.matches("^[\\u4E00-\\u9FA5A-Za-z0-9]{1,10}$")) {
            Toast.makeText(this, "昵称限10位汉字、字母或者数字", Toast.LENGTH_SHORT).show();
            return false;
        }

        // 密码检查
        String password = registerPasswordEdit.getText().toString().trim();
        if (password.length() < 6 || password.length() > 20) {
            Toast.makeText(this, "密码必须为6~20位字母或者数字", Toast.LENGTH_SHORT).show();

            return false;
        }

        return true;
    }

    @OnClick(R.id.btn_login_register)
    public void onViewClicked() {
        login();
    }

    /**
     * ***************************************** 注册/登录切换 **************************************
     */
//    private void switchMode() {
//        registerMode = !registerMode;
//
//        if (registerMode && !registerPanelInited) {
//            registerAccountEdit = findView(R.id.edit_register_account);
//            registerNickNameEdit = findView(R.id.edit_register_nickname);
//            registerPasswordEdit = findView(R.id.edit_register_password);
//
//            registerAccountEdit.setIconResource(R.drawable.user_account_icon);
//            registerNickNameEdit.setIconResource(R.drawable.nick_name_icon);
//            registerPasswordEdit.setIconResource(R.drawable.user_pwd_lock_icon);
//
//            registerAccountEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
//            registerNickNameEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(10)});
//            registerPasswordEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
//
//            registerAccountEdit.addTextChangedListener(textWatcher);
//            registerNickNameEdit.addTextChangedListener(textWatcher);
//            registerPasswordEdit.addTextChangedListener(textWatcher);
//
//            registerPanelInited = true;
//        }
//
//        setTitle(registerMode ? R.string.register : R.string.login);
//        //tv_title.setText(registerMode ? R.string.register : R.string.login);
//        loginLayout.setVisibility(registerMode ? View.GONE : View.VISIBLE);
//        registerLayout.setVisibility(registerMode ? View.VISIBLE : View.GONE);
//        loginRegisterBtn.setText(registerMode? "注册" : "登录");
//        switchModeBtn.setText(registerMode ? R.string.login_has_account : R.string.register_quickly);
//        if (registerMode) {
//            loginRegisterBtn.setEnabled(true);
//        } else {
//            boolean isEnable = loginAccountEdit.getText().length() > 0
//                    && loginPasswordEdit.getText().length() > 0;
//            loginRegisterBtn.setEnabled(isEnable);
//        }
//    }
}
