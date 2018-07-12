package com.fmsh.ehelathcard.SetUrl;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fmsh.ehelathcard.Global;
import com.fmsh.ehelathcard.MainActivity;
import com.fmsh.ehelathcard.R;
import com.fmsh.ehelathcardsdk.SDKHelper;

import org.json.JSONObject;

public class SetUrlActivity extends Activity {

    private TextView tip_info;
    private String tips;
    private EditText seturl;
    private String newurl;
    private Button btn_cancel, btn_ok;
    private SDKHelper sdkHelper ;


    public Handler myhandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            String resp = "";
            int type = 0;
            resp = (String)msg.obj;
            type = msg.arg1;
            new  AlertDialog.Builder(SetUrlActivity.this).setTitle("设置卡管平台地址接口").setMessage(resp).setPositiveButton("确定" ,  null ).show();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_url);

        tip_info = (TextView) findViewById(R.id.tip_info);
        tips = "";
        tip_info.setText(tips);
        seturl = (EditText) findViewById(R.id.seturl);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_cancel.setOnClickListener(mCancelClickListener);
        btn_ok.setOnClickListener(mOkClickListener);

        sdkHelper = new SDKHelper(myhandler);
    }

    private View.OnClickListener mCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SetUrlActivity.this, MainActivity.class);
            startActivity(intent);
            SetUrlActivity.this.finish();
        }
    };

    private View.OnClickListener mOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            newurl = seturl.getText().toString().trim();
            if(TextUtils.isEmpty(newurl))
            {
                tip_info.setHint("请输入卡管平台地址！");
                return;
            }
            boolean ifok = sdkHelper.sdk_seturl(newurl);
            if(ifok) {
                Global.urlOk = true;
                Global.url_ok = newurl;
                tip_info.setHint("卡管平台地址设置成功！");
            }
            else {
                Global.urlOk = false;
                tip_info.setHint("卡管平台地址设置失败！");
            }
        }
    };
}
