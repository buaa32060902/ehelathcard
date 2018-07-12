package com.fmsh.ehelathcard.Close;

import android.app.Activity;
import android.content.Intent;
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
import com.fmsh.ehelathcard.SM3Main;
import com.fmsh.ehelathcardsdk.SDKHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CloseActivity extends Activity {

    private TextView main_method, main_appid, main_termid, main_version,main_timestamp,main_digesttype;
    private RadioGroup main_enctype;
    private String method,app_id,term_id,version,timestamp,digest_type,digest,enc_type;
    private TextView tip_info;
    private String tips;
    private Button btn_cancel, btn_ok;

    private EditText biz_close_ehealth_card_id, biz_close_revoke_no;
    private TextView biz_close_revoke_time;
    private String close_ehealth_card_id,close_revoke_no, close_revoke_time;


    private SDKHelper sdkHelper ;
    private String appId,appSecret,systemKey,pin, apptypevalue;

    public Handler myhandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            String resp = "";
            int type = 0;
            resp = (String)msg.obj;
            type = msg.arg1;
            new  AlertDialog.Builder(CloseActivity.this).setTitle("注销电子健康卡接口").setMessage(resp).setPositiveButton("确定" ,  null ).show();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_close);
        Intent intent = getIntent();
        Bundle bundleExtra = intent.getBundleExtra("bundle");
        appId = bundleExtra.getString("appId");
        appSecret = bundleExtra.getString("appSecret");
        systemKey = bundleExtra.getString("systemKey");
        pin = bundleExtra.getString("pin");
        apptypevalue = bundleExtra.getString("apptype");
        initMainView();
        initBizView();
        sdkHelper = new SDKHelper(myhandler);
        sdkHelper.sdk_seturl(Global.url_ok);
        sdkHelper.sdk_config(Global.apptype_ok, Global.appId_ok, Global.appSecret_ok, Global.systemKey_ok, Global.pin_ok);
        sdkHelper.sdk_setVerify(Global.sdk_ok);
    }

    private void initMainView()
    {
        main_method = (TextView) findViewById(R.id.main_method);
        main_appid = (TextView) findViewById(R.id.main_appid);
        main_termid = (TextView) findViewById(R.id.main_termid);
        main_version = (TextView) findViewById(R.id.main_version);
        main_timestamp = (TextView) findViewById(R.id.main_timestamp);
        main_digesttype = (TextView) findViewById(R.id.main_digesttype);
        main_enctype = (RadioGroup)findViewById(R.id.main_enctype);
        main_enctype.setOnCheckedChangeListener(enctypeListener);

        tip_info = (TextView) findViewById(R.id.tip_info);
        tips = "";
        tip_info.setText(tips);

        btn_cancel = (Button)findViewById(R.id.btn_cancel);
        btn_ok = (Button)findViewById(R.id.btn_ok);
        btn_cancel.setOnClickListener(mCancelClickListener);
        btn_ok.setOnClickListener(mOkClickListener);

        method = "close";
        app_id = "sdfaasdfsd";
        term_id = "x00dd";
        version = "v1.0.0";
        timestamp = Long.toString(System.currentTimeMillis());
        digest_type = "SM3";
        RadioButton radioButton = (RadioButton)findViewById(main_enctype.getCheckedRadioButtonId());
        enc_type = radioButton.getText().toString();

        main_method.setText(method);
        main_appid.setText(app_id);
        main_termid.setText(term_id);
        main_version.setText(version);
        main_timestamp.setText(timestamp);
        main_digesttype.setText(digest_type);
    }
    RadioGroup.OnCheckedChangeListener enctypeListener=new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup Group, int Checkid) {
            // TODO Auto-generated method stub
            RadioButton radioButton = (RadioButton)findViewById(main_enctype.getCheckedRadioButtonId());
            enc_type = radioButton.getText().toString();
        }
    };

    private void initBizView()
    {
        biz_close_ehealth_card_id = (EditText) findViewById(R.id.biz_close_ehealth_card_id);
        biz_close_revoke_no= (EditText) findViewById(R.id.biz_close_revoke_no);
        biz_close_revoke_time = (TextView)findViewById(R.id.biz_close_revoke_time);
        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        close_revoke_time = dateTimeformat.format(new Date());
        biz_close_revoke_time.setText(close_revoke_time);
    }
    private View.OnClickListener mCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(CloseActivity.this, MainActivity.class);
            startActivity(intent);
            CloseActivity.this.finish();
        }
    };

    private View.OnClickListener mOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            close_ehealth_card_id = biz_close_ehealth_card_id.getText().toString().trim();
            if(TextUtils.isEmpty(close_ehealth_card_id))
            {
                tip_info.setHint("请输入电子健康卡ID！");
                return;
            }

            close_revoke_no = biz_close_revoke_no.getText().toString().trim();
            if(TextUtils.isEmpty(close_revoke_no))
            {
                tip_info.setHint("请输入外部注销流水号！");
                return;
            }
            close_revoke_time = biz_close_revoke_time.getText().toString().trim();

            JSONObject bizJson = sdkHelper.getBizJson_close(close_ehealth_card_id, close_revoke_no, close_revoke_time);
            if(bizJson == null)
            {
                tip_info.setHint("接口参数错误！");
                return;
            }
//            JSONObject paraJson = getAllParaJson(bizJson);
            JSONObject paraJson = sdkHelper.getAllParaJson(method, app_id, term_id, version, timestamp, digest_type, enc_type, bizJson);
            if(paraJson == null)
            {
                tip_info.setHint("通用参数错误！");
                return;
            }

            String digest = summary(paraJson);
            sdkHelper.close(method, app_id, term_id, version, timestamp, digest_type, digest, enc_type, close_ehealth_card_id, close_revoke_no, close_revoke_time);

        }
    };
    private JSONObject getAllParaJson(JSONObject jsonObjBiz)
    {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject();//请求参数对象，json形式
            jsonObj.put("method", method);//向公共请求参数对象里面添加值
            jsonObj.put("app_id", app_id);//向公共请求参数对象里面添加值
            jsonObj.put("term_id", term_id);//向公共请求参数对象里面添加值
            jsonObj.put("version", version);//向公共请求参数对象里面添加值
            jsonObj.put("timestamp", timestamp);//向公共请求参数对象里面添加值
            jsonObj.put("digest_type", digest_type);//向公共请求参数对象里面添加值
            jsonObj.put("enc_type", enc_type);//向公共请求参数对象里面添加值
            if(jsonObjBiz != null)
                jsonObj.put("biz_content", jsonObjBiz);//向公共请求参数对象里面添加值
        } catch (JSONException e) {
            tips = e.getMessage();
            e.printStackTrace();
        }
        return jsonObj;
    }

    private JSONObject getBizJson()
    {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject();//请求参数对象，json形式
            jsonObj.put("ehealth_card_id", close_ehealth_card_id);//向接口请求参数对象里面添加值
            jsonObj.put("revoke_no", close_revoke_no);//向接口请求参数对象里面添加值
            jsonObj.put("revoke_time", close_revoke_time);//向接口请求参数对象里面添加值
        } catch (JSONException e) {
            tips = e.getMessage();
            e.printStackTrace();
        }
        return jsonObj;
    }

    private String summary(JSONObject paraJson) {
        String strData = paraJson.toString();
        // 使用请求包和app_secret生成摘要值
        String digest = SM3Main.digest((strData + appSecret).getBytes());
        return digest;
    }
}
