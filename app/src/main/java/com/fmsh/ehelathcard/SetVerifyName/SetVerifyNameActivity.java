package com.fmsh.ehelathcard.SetVerifyName;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.fmsh.ehelathcard.Global;
import com.fmsh.ehelathcard.MainActivity;
import com.fmsh.ehelathcard.R;
import com.fmsh.ehelathcard.SetUrl.SetUrlActivity;
import com.fmsh.ehelathcardsdk.SDKHelper;

public class SetVerifyNameActivity extends Activity {

    private TextView tip_info;
    private String tips;
    private EditText setsdkname;
    private String sdkname;
    private Button btn_cancel, btn_ok;
    private SDKHelper sdkHelper ;

    public Handler myhandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            String resp = "";
            int type = 0;
            resp = (String)msg.obj;
            type = msg.arg1;
            new  AlertDialog.Builder(SetVerifyNameActivity.this).setTitle("设置SDK校验名接口").setMessage(resp).setPositiveButton("确定" ,  null ).show();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_verify_name);
        tip_info = (TextView) findViewById(R.id.tip_info);

        tips = "";
        tip_info.setText(tips);
        setsdkname = (EditText) findViewById(R.id.setsdkname);
        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_cancel.setOnClickListener(mCancelClickListener);
        btn_ok.setOnClickListener(mOkClickListener);

        sdkHelper = new SDKHelper(myhandler);
    }

    private View.OnClickListener mCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(SetVerifyNameActivity.this, MainActivity.class);
            startActivity(intent);
            SetVerifyNameActivity.this.finish();
        }
    };

    private View.OnClickListener mOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            sdkname = setsdkname.getText().toString().trim();
            if(TextUtils.isEmpty(sdkname))
            {
                tip_info.setHint("请输入SDK校验名！");
                return;
            }
            boolean ifok = sdkHelper.sdk_setVerify(sdkname);
            if(ifok) {
                Global.sdkOk = true;
                Global.sdk_ok = sdkname;
                tip_info.setHint("SDK校验名设置成功！");
            }
            else {
                Global.sdkOk = false;
                tip_info.setHint("SDK校验名设置失败！");
            }
        }
    };

}
