package com.fmsh.ehelathcard.SetAppInfo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fmsh.ehelathcard.Global;
import com.fmsh.ehelathcard.MainActivity;
import com.fmsh.ehelathcard.R;
import com.fmsh.ehelathcardsdk.SDKHelper;

public class SetAppInfoActivity extends Activity {

    private RadioGroup app_type;
    private String apptypevalue = "";

    private TextView tip_info;
    private String tips;
    private EditText setappId, setappSecret, setsystemKey, setpin;
    private String appId, appSecret, systemKey, pin;
    private Button btn_cancel, btn_ok;
    private SDKHelper sdkHelper ;

    public Handler myhandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            String resp = "";
            int type = 0;
            resp = (String)msg.obj;
            type = msg.arg1;
            new  AlertDialog.Builder(SetAppInfoActivity.this).setTitle("设置APP基本信息接口").setMessage(resp).setPositiveButton("确定" ,  null ).show();
        };
    };
   @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_app_info);
       tip_info = (TextView) findViewById(R.id.tip_info);
       tips = "";
       tip_info.setText(tips);
       setappId = (EditText) findViewById(R.id.appId);
       setappSecret = (EditText) findViewById(R.id.appSecret);
       setsystemKey = (EditText) findViewById(R.id.systemKey);
       setpin = (EditText) findViewById(R.id.pin);
       btn_cancel = (Button)findViewById(R.id.btn_cancel);
       btn_ok = (Button)findViewById(R.id.btn_ok);
       btn_cancel.setOnClickListener(mCancelClickListener);
       btn_ok.setOnClickListener(mOkClickListener);

       app_type = (RadioGroup)findViewById(R.id.app_type);
       app_type.setOnCheckedChangeListener(apptypeListener);
       sdkHelper = new SDKHelper(myhandler);
       //读取用户信息
       SharedPreferences pref_app = this.getSharedPreferences(Global.APPINFO_SHAREFILE, Context.MODE_PRIVATE);
       appId = pref_app.getString(Global.share_appId, MainActivity.appId);
       appSecret = pref_app.getString(Global.share_appSecret, MainActivity.appSecret);
       systemKey = pref_app.getString(Global.share_systemKey, MainActivity.systemKey);
       pin = pref_app.getString(Global.share_pin, MainActivity.pin);
       apptypevalue = pref_app.getString(Global.share_apptype, "0");

       setappId.setText(appId);
       setappSecret.setText(appSecret);
       setsystemKey.setText(systemKey);
       setpin.setText(pin);

       if(apptypevalue.length() > 0)
       {
           RadioButton radioButton_tongyong = (RadioButton)findViewById(R.id.rb_tongyong);
           RadioButton radioButton_yuansheng = (RadioButton)findViewById(R.id.rb_yuansheng);
           switch (apptypevalue)
           {
               case "0":
                   radioButton_tongyong.setChecked(true);
                   radioButton_yuansheng.setChecked(false);
                   break;
               case "1":
                   radioButton_tongyong.setChecked(false);
                   radioButton_yuansheng.setChecked(true);
                   break;
               default:
                   break;
           }
       }
       else
       {
           RadioButton radioButton = (RadioButton)findViewById(app_type.getCheckedRadioButtonId());
           apptypevalue = radioButton.getTag().toString();
       }
   }

    RadioGroup.OnCheckedChangeListener apptypeListener=new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup Group, int Checkid) {
            // TODO Auto-generated method stub
            RadioButton radioButton = (RadioButton)findViewById(app_type.getCheckedRadioButtonId());
            apptypevalue = radioButton.getTag().toString();
            //    utilsHelper.updataAppType(apptypevalue);
        }
    };

    private View.OnClickListener mCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SetAppInfoActivity.this, MainActivity.class);
            startActivity(intent);
            SetAppInfoActivity.this.finish();
        }
    };

    private View.OnClickListener mOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            appId = setappId.getText().toString().trim();
            if(TextUtils.isEmpty(appId))
            {
                tip_info.setHint("请输入appId！");
                return;
            }
            //appId = "bmwt7kpc079gmmoj0e";

            appSecret = setappSecret.getText().toString().trim();
            if(TextUtils.isEmpty(appSecret))
            {
                tip_info.setHint("请输入appSecret！");
                return;
            }
            //appSecret = "B37496C26E475D07F032F1657E80D7A69FD87E1CCC819A8D74B751293E6A6D76";

            systemKey = setsystemKey.getText().toString().trim();
            if(TextUtils.isEmpty(systemKey))
            {
                tip_info.setHint("请输入systemKey！");
                return;
            }
            //systemKey = "DE85EF24E0560FBC00BD6E3FB9D19E4D5F9C3C518AECAD1E777346C55F3CE435";

            pin = setpin.getText().toString().trim();
            if(TextUtils.isEmpty(pin))
            {
                tip_info.setHint("请输入pin！");
                return;
            }
            //pin = "12345678";

            sdkHelper.sdk_config(apptypevalue, appId, appSecret, systemKey, pin);
            Global.configOk = true;
            Global.appId_ok = appId;
            Global.appSecret_ok = appSecret;
            Global.systemKey_ok = systemKey;
            Global.pin_ok = pin;
            Global.apptype_ok = apptypevalue;

            //TODO 存储用户信息
            SharedPreferences userinfo = getApplicationContext().getSharedPreferences(
                    Global.APPINFO_SHAREFILE, Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = userinfo.edit();
            editor.putString(Global.share_appId, appId);
            //TODO int 类型的可能需要修改
            //TODO 验证码的有效期，应该在获取验证码的时候返回才对
            editor.putString(Global.share_appSecret, appSecret);
            editor.putString(Global.share_systemKey, systemKey);
            editor.putString(Global.share_pin, pin);
            editor.putString(Global.share_apptype, apptypevalue);
            editor.commit();

            tip_info.setHint("APP信息设置完成！");

            // close current intent, open the main activity
            Intent intent = new Intent(SetAppInfoActivity.this, MainActivity.class);
            startActivity(intent);
            SetAppInfoActivity.this.finish();


        }
    };
}
