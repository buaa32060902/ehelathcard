package com.fmsh.ehelathcard.AlterInfo;

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

public class AlterInfoActivity extends Activity {

    private TextView main_method, main_appid, main_termid, main_version,main_timestamp,main_digesttype;
    private RadioGroup main_enctype;
    private String method,app_id,term_id,version,timestamp,digest_type,digest,enc_type;
    private TextView tip_info;
    private String tips;
    private Button btn_cancel, btn_ok;

    private EditText biz_alter_ehealth_card_id, biz_alter_birthday, biz_alter_cellphone, biz_alter_telephone, biz_alter_address, biz_alter_unit, biz_alter_no;
    private TextView biz_alter_time;
    private RadioGroup biz_alter_gender;
    private String alter_gender,alter_ehealth_card_id, alter_birthday, alter_cellphone,alter_telephone, alter_address,alter_unit, alter_time,alter_no;


    private SDKHelper sdkHelper ;
    private String appId,appSecret,systemKey,pin, apptypevalue;

    public Handler myhandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            String resp = "";
            int type = 0;
            resp = (String)msg.obj;
            type = msg.arg1;
            new  AlertDialog.Builder(AlterInfoActivity.this).setTitle("修改电子健康卡接口").setMessage(resp).setPositiveButton("确定" ,  null ).show();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alter_info2);
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

        method = "alterinfo";
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
        biz_alter_ehealth_card_id = (EditText) findViewById(R.id.biz_alter_ehealth_card_id);
        biz_alter_birthday= (EditText) findViewById(R.id.biz_alter_birthday);
        biz_alter_cellphone= (EditText) findViewById(R.id.biz_alter_cellphone);
        biz_alter_telephone = (EditText)findViewById(R.id.biz_alter_telephone);
        biz_alter_address = (EditText)findViewById(R.id.biz_alter_address);
        biz_alter_unit = (EditText)findViewById(R.id.biz_alter_unit);
        biz_alter_gender = (RadioGroup)findViewById(R.id.biz_alter_gender);
        biz_alter_gender.setOnCheckedChangeListener(genderListener);
        RadioButton radioButton = (RadioButton)findViewById(biz_alter_gender.getCheckedRadioButtonId());
        alter_gender = radioButton.getTag().toString();

        biz_alter_no = (EditText)findViewById(R.id.biz_alter_no);
        biz_alter_time = (TextView)findViewById(R.id.biz_alter_time);
        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        alter_time = dateTimeformat.format(new Date());
        biz_alter_time.setText(alter_time);



    }

    RadioGroup.OnCheckedChangeListener genderListener=new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup Group, int Checkid) {
            // TODO Auto-generated method stub
            RadioButton radioButton = (RadioButton)findViewById(biz_alter_gender.getCheckedRadioButtonId());
            alter_gender = radioButton.getTag().toString();
        }
    };
    private View.OnClickListener mCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(AlterInfoActivity.this, MainActivity.class);
            startActivity(intent);
            AlterInfoActivity.this.finish();
        }
    };

    private View.OnClickListener mOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            alter_ehealth_card_id = biz_alter_ehealth_card_id.getText().toString().trim();
            if(TextUtils.isEmpty(alter_ehealth_card_id))
            {
                tip_info.setHint("请输入电子健康卡ID！");
                return;
            }

            alter_no = biz_alter_no.getText().toString().trim();
            if(TextUtils.isEmpty(alter_no))
            {
                tip_info.setHint("请输入外部变更流水号！");
                return;
            }
            alter_time = biz_alter_time.getText().toString().trim();
            alter_birthday = biz_alter_birthday.getText().toString().trim();
            alter_cellphone = biz_alter_cellphone.getText().toString().trim();
            alter_telephone = biz_alter_telephone.getText().toString().trim();
            alter_address = biz_alter_address.getText().toString().trim();
            alter_unit = biz_alter_unit.getText().toString().trim();

            JSONObject bizJson = sdkHelper.getBizJson_alterinfor(alter_ehealth_card_id, alter_no, alter_time, alter_gender,alter_birthday, alter_cellphone, alter_telephone,alter_address, alter_unit);

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
            sdkHelper.alterinfo(method, app_id, term_id, version, timestamp, digest_type,digest,
                    enc_type,alter_ehealth_card_id, alter_no, alter_time, alter_gender,alter_birthday, alter_cellphone, alter_telephone,alter_address, alter_unit);

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
            jsonObj.put("ehealth_card_id", alter_ehealth_card_id);//向接口请求参数对象里面添加值
            jsonObj.put("alter_no", alter_no);//向接口请求参数对象里面添加值
            jsonObj.put("alter_time", alter_time);//向接口请求参数对象里面添加值
            jsonObj.put("gender", alter_gender);//向接口请求参数对象里面添加值
            if(alter_birthday.length() > 0)
                jsonObj.put("birthday", alter_birthday);//向接口请求参数对象里面添加值
            if(alter_cellphone.length() > 0)
                jsonObj.put("cellphone", alter_cellphone);//向接口请求参数对象里面添加值jsonObjBiz.put("method", method);//向接口请求参数对象里面添加值
            if(alter_telephone.length() > 0)
                jsonObj.put("telephone", alter_telephone);//向接口请求参数对象里面添加值
            if(alter_address.length() > 0)
                jsonObj.put("address", alter_address);//向接口请求参数对象里面添加值
            if(alter_unit.length() > 0)
                jsonObj.put("unit", alter_unit);//向接口请求参数对象里面添加值
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
