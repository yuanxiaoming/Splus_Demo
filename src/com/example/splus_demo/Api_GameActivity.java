
package com.example.splus_demo;

import com.android.splus.sdk.api.PayManager;
import com.android.splus.sdk.apiinterface.LogoutCallBack;
import com.android.splus.sdk.apiinterface.RechargeCallBack;
import com.android.splus.sdk.apiinterface.UserAccount;
import com.android.splus.sdk.ui.FloatToolBar;
import com.android.splus.sdk.ui.FloatToolBar.FloatToolBarAlign;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class Api_GameActivity extends Activity {

    private EditText rechargeEditText;

    private Button recharge_btn;

    private Button rechargequato_btn;

    private Button usercenter_btn;

    private Button statistics_game_server_btn;

    private Button logout_btn;

    private Button exit_SDK_btn;

    private Button exit_Game_btn;

    private Button bbs_btn;

    public FloatToolBar mTooBar;

    private boolean isAppForeground = true;

    /**
     * 本demo采用实现接口的回调的方式是匿名内部类 对接游戏方可采用自己的方式实现接口回调
     */

    /********************************** 各个接口回调实现 *******************************************************/

    /**
     * 充值成功回调接口
     */
    private RechargeCallBack mRechargeCallBackImp = new RechargeCallBack() {

        @Override
        public void rechargeSuccess(UserAccount account) {
            String name = account.getUserName();
            int uid = account.getUserUid();
            String str = "name=" + name + "," + "uid=" + uid;
            Log.d("accout----", str);
            // 自己判断处理逻辑想做一些界面的修改
        }

        @Override
        public void rechargeFaile(String errorMsg) {
            Log.d("rechargeFaile", errorMsg);
            // 自己判断处理逻辑想做一些界面的修改
        }

        @Override
        public void backKey(String errorMsg) {
            Log.d("backKey", errorMsg);
            // 自己判断处理逻辑想做一些界面的修改
        }
    };

    /**
     * 注销接口回调
     */
    private LogoutCallBack mLogoutCallBackImp = new LogoutCallBack() {

        @Override
        public void logoutCallBack() {
            /***
             * 此方法及参考
             */
            Intent i = getBaseContext().getPackageManager().getLaunchIntentForPackage(
                    getBaseContext().getPackageName());
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);

        }
    };

    /*********************************** 接口调用 ***************************************************************/

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_userin_game);
        rechargeEditText = (EditText) findViewById(R.id.rechargeEditText);
        recharge_btn = (Button) findViewById(R.id.recharge);
        rechargequato_btn = (Button) findViewById(R.id.rechargequato);
        usercenter_btn = (Button) findViewById(R.id.usercenter);
        bbs_btn = (Button) findViewById(R.id.bbs);
        logout_btn = (Button) findViewById(R.id.loginout);
        exit_SDK_btn = (Button) findViewById(R.id.exit_sdk);
        exit_Game_btn = (Button) findViewById(R.id.exit_game);
        statistics_game_server_btn = (Button) findViewById(R.id.statistics_game);
        /**
         * 悬浮按钮创建及显示
         */
        mTooBar = PayManager.getInstance().creatFloatButton(this, true, FloatToolBarAlign.Right,
                0.5f);
        if (mTooBar != null) {
            mTooBar.show();
        }
        /**
         * 不定额充值接口调用
         */
        recharge_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // PayManager.getInstance().recharge(String serverName, String
                // roleName, String outOrderid, String pext, RechargeCallBack
                // mRechargeCallBack);
                PayManager.getInstance().recharge(Api_GameActivity.this, 1111,"湖南一区", 2222,"yuanxiaoming",
                        "外部订单号outorderid", "自己扩展pext", mRechargeCallBackImp);
            }
        });

        /***
         * 定额充值调用
         */

        rechargequato_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                String string = rechargeEditText.getText().toString();
                if (TextUtils.isEmpty(string)) {
                    Toast.makeText(Api_GameActivity.this, "请输入金额", Toast.LENGTH_LONG).show();
                    return;
                }
                Float money = Float.parseFloat(string);

                // 调用
                PayManager.getInstance().rechargeByQuota(Api_GameActivity.this, 1111,"湖南一区",2222,
                        "yuanxiaoming", "外部订单号outorderid", "自己扩展pext", money, mRechargeCallBackImp);
            }
        });

        /**
         * 个人中心
         */
        usercenter_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {

                PayManager.getInstance().enterUserCenter(Api_GameActivity.this, mLogoutCallBackImp);
            }
        });

        bbs_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PayManager.getInstance().enterBBS(Api_GameActivity.this);

            }
        });

        /**
         * 统计游戏角色区服等级
         */
        statistics_game_server_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PayManager.getInstance().sendGameStatics(Api_GameActivity.this, 1111,"湖南一区",2222,
                        "yuanxiaoming", "32级");
            }
        });
        /**
         * 注销
         */
        logout_btn.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                PayManager.getInstance().logout(Api_GameActivity.this, mLogoutCallBackImp);
            }
        });

        /**
         * 退出SDK
         */

        exit_SDK_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PayManager.getInstance().exitSDK();
            }
        });

        /**
         * 退出GAME
         */

        exit_Game_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                PayManager.getInstance().exitGame(Api_GameActivity.this);
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        PayManager.getInstance().onResume(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        PayManager.getInstance().onPause(this);

    }


    @Override
    protected void onStop() {
        super.onStop();
        PayManager.getInstance().onStop(this);

    }

    protected void onDestroy() {
        super.onDestroy();
        /**
         * 取消悬浮按钮
         */
        if (mTooBar != null) {
            mTooBar.recycle();
        }
        /**
         * 销毁实例
         */
        PayManager.getInstance().onDestroy(this);
    }


}
