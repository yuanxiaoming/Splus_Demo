
package com.example.splus_demo;

import com.android.splus.sdk.apiinterface.InitCallBack;
import com.android.splus.sdk.apiinterface.LoginCallBack;
import com.android.splus.sdk.apiinterface.UserAccount;
import com.android.splus.sdk.ui.FloatToolBar;
import com.android.splus.sdk.ui.FloatToolBar.FloatToolBarAlign;
import com.android.splus.sdk.ui.SplusPayManager;
import com.example.splus_demo.Api_GameActivity;
import com.example.splus_demo.R;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class LoginGameActivity extends Activity {
    private String appkey = "7Q/Rh-p_goN,zd?";

    private Button login_btn;

    public static final String localVersion = "1.3.3";

    public FloatToolBar mTooBar;

    /**
     * 本demo采用实现接口的回调的方式是匿名内部类 对接游戏方可采用自己的方式实现接口回调
     */

    /********************************** 各个接口回调实现 *******************************************************/

    /**
     * 游戏默认使用SDK 更新下载的方式 初始化回调接口
     */
    private InitCallBack mInitCallBackImp = new InitCallBack() {

        @Override
        public void initSuccess(String msg, JSONObject apkverJson) {
            Log.d("init", msg + apkverJson);
            SplusPayManager.getInstance().login(LoginGameActivity.this, mLoginCallBackImp);
        }

        @Override
        public void initFaile(String errorMsg) {
            Log.d("initFaile", errorMsg);

        }

    };

    /**
     * 登录 注册成功回调接口
     */
    private LoginCallBack mLoginCallBackImp = new LoginCallBack() {
        @Override
        public void loginSuccess(UserAccount account) {

            String name = account.getUserName();
            int uid = account.getUserUid();
            String sessionid = account.getSession();
            String str = "name=" + name + "," + "uid=" + uid + "," + "sessionid=" + sessionid;
            Log.d("accout---", str);
            // 自己判断处理逻辑想做一些界面的修改包过（拿sessionid去服务器做验证，验证登录成功就进入游戏）或者想回到那个游戏界面（可选）。

            /**
             * 测试
             */
            Intent intent=new Intent();
            intent.setClass(LoginGameActivity.this, Api_GameActivity.class);
            startActivity(intent);
            finish();

        }

        @Override
        public void loginFaile(String errorMsg) {
            // 登录失败的回调
            Log.d("loginFaile", errorMsg);
        }

        @Override
        public void backKey(String errorMsg) {
            // 取消登录或者是注册
            Log.d("backKey", errorMsg);
        }
    };

    /*********************************** 接口调用 ***************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_game);
        login_btn = (Button) findViewById(R.id.start_login_button);
        // 开启日志调试
        SplusPayManager.getInstance().setDBUG(true);
        /**
         * 1.默认SDK更新
         */
     //   Configuration.ORIENTATION_LANDSCAPE  横屏游戏
     //   Configuration.ORIENTATION_PORTRAIT;  竖屏游戏
        SplusPayManager.getInstance().init(this, appkey, mInitCallBackImp, true,Configuration.ORIENTATION_LANDSCAPE);
        /**
         * 悬浮按钮创建及显示
         */
        mTooBar  = SplusPayManager.getInstance().creatFloatButton(this, true, FloatToolBarAlign.Right, 0.5f);
        if (mTooBar != null) {
            mTooBar.show();
        }
        /**
         * 登录接口调用
         */
        login_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                SplusPayManager.getInstance().login(LoginGameActivity.this, mLoginCallBackImp);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        /**
         * 在线时长开始统计
         */

        SplusPayManager.getInstance().onResume(this);
    }


    @Override
    protected void onPause() {
        super.onPause();
        /**
         * 在线时长结束统计
         */
        SplusPayManager.getInstance().onPause(this);
    }



    protected void onDestroy() {
        super.onDestroy();
        /**
         * 取消悬浮按钮
         */
        if (mTooBar != null) {
            mTooBar.recycle();
        }
    }

}

