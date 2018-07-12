package com.fmsh.ehelathcard.RegisterActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.fmsh.ehelathcard.Global;
import com.fmsh.ehelathcard.MainActivity;
import com.fmsh.ehelathcard.R;
import com.fmsh.ehelathcard.SM3Main;
import com.fmsh.ehelathcard.SpinnerData;
import com.fmsh.ehelathcardsdk.Commons;
import com.fmsh.ehelathcardsdk.JsonHelper;
import com.fmsh.ehelathcardsdk.SDKHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RegisterActivity extends Activity {

    private TextView main_method, main_appid, main_termid, main_version,main_timestamp,main_digesttype;
    private RadioGroup main_enctype;
    private String method,app_id,term_id,version,timestamp,digest_type,digest,enc_type;
    private TextView tip_info;
    private String tips;
    private Button btn_cancel, btn_ok;
    private TextView biz_registertime;
    private EditText biz_registerno, biz_name, biz_idno, biz_birthday, biz_cellphone, biz_telephone, biz_address, biz_unit;
    private RadioGroup biz_gender;
    private Spinner biz_applytype, biz_idtype;
    private ArrayAdapter<SpinnerData> arr_adapter_applytype, arr_adapter_idtype;
    private String register_no, register_time, apply_type,id_type,id_no,name,gender,birthday,cellphone,telephone,address,unit;
    private String paraStr;

    List<SpinnerData> biz_applytypelist = new ArrayList<SpinnerData>();
    List<SpinnerData> biz_idtypelist = new ArrayList<SpinnerData>();
    private SDKHelper sdkHelper ;
    private String appId,appSecret,systemKey,pin, apptypevalue;


    public Handler myhandler = new Handler(){
        public void handleMessage(android.os.Message msg) {
            int what = msg.what;
            String resp = "";
            int type = 0;
            resp = (String)msg.obj;
            type = msg.arg1;
            parseJson(resp);
            new  AlertDialog.Builder(RegisterActivity.this).setTitle("注册接口").setMessage(resp).setPositiveButton("确定" ,  null ).show();
        };
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Intent intent = getIntent();

        Bundle bundleExtra = intent.getBundleExtra("bundle");
        appId = bundleExtra.getString("appId");
        appSecret = bundleExtra.getString("appSecret");
        systemKey = bundleExtra.getString("systemKey");
        pin = bundleExtra.getString("pin");
        apptypevalue = bundleExtra.getString("apptype");
        initData();
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

        method = "register";
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

    private void initBizView()
    {
        biz_registertime = (TextView)findViewById(R.id.biz_registertime);
        biz_registerno = (EditText) findViewById(R.id.biz_registerno);
        biz_name = (EditText) findViewById(R.id.biz_name);
        biz_idno = (EditText) findViewById(R.id.biz_idno);
        biz_birthday= (EditText) findViewById(R.id.biz_birthday);
        biz_cellphone= (EditText) findViewById(R.id.biz_cellphone);
        biz_telephone = (EditText)findViewById(R.id.biz_telephone);
        biz_address = (EditText)findViewById(R.id.biz_address);
        biz_unit = (EditText)findViewById(R.id.biz_unit);
        biz_applytype = (Spinner) findViewById(R.id.biz_applytype);
        biz_idtype = (Spinner) findViewById(R.id.biz_idtype);
        biz_gender = (RadioGroup)findViewById(R.id.biz_gender);
        biz_gender.setOnCheckedChangeListener(genderListener);
        RadioButton radioButton = (RadioButton)findViewById(biz_gender.getCheckedRadioButtonId());
        gender = radioButton.getTag().toString();

        DateFormat dateTimeformat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        register_time = dateTimeformat.format(new Date());
        biz_registertime.setText(register_time);

        arr_adapter_applytype = new ArrayAdapter<SpinnerData>(this,android.R.layout.simple_spinner_item, biz_applytypelist);
        arr_adapter_applytype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        biz_applytype.setAdapter(arr_adapter_applytype);

        arr_adapter_idtype = new ArrayAdapter<SpinnerData>(this, android.R.layout.simple_spinner_item, biz_idtypelist);
        arr_adapter_idtype.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);//设置样式
        biz_idtype.setAdapter(arr_adapter_idtype);//加载适配器


    }

    private void initData()
    {
        SpinnerData sp = new SpinnerData("app在线", "1");
        biz_applytypelist.add(sp);
        sp = new SpinnerData("医疗卫生机构", "2");
        biz_applytypelist.add(sp);
        sp = new SpinnerData("医疗机构窗口", "3");
        biz_applytypelist.add(sp);
        sp = new SpinnerData("批量生成", "4");
        biz_applytypelist.add(sp);

        sp = new SpinnerData("居民身份证", "01");
        biz_idtypelist.add(sp);
        sp = new SpinnerData("居民户口簿", "02");
        biz_idtypelist.add(sp);
        sp = new SpinnerData("护照", "03");
        biz_idtypelist.add(sp);
        sp = new SpinnerData("军官证", "04");
        biz_idtypelist.add(sp);
        sp = new SpinnerData("驾驶证", "05");
        biz_idtypelist.add(sp);
        sp = new SpinnerData("港澳居民来往内地通行证", "06");
        biz_idtypelist.add(sp);
        sp = new SpinnerData("台湾居民来往内地通行证", "07");
        biz_idtypelist.add(sp);
        sp = new SpinnerData("其他法定有效证件", "99");
        biz_idtypelist.add(sp);
    }


    public String getSpinnerSelVal(Spinner sp){
        return ((SpinnerData)sp.getSelectedItem()).getValue();
    }
    //取得text
    public String getSpinnerSelName(Spinner sp) {
        return ((SpinnerData) sp.getSelectedItem()).getText();
    }


        @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode == KeyEvent.KEYCODE_BACK){
            startMainActity();
        }
        return super.onKeyDown(keyCode, event);
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

    RadioGroup.OnCheckedChangeListener genderListener=new RadioGroup.OnCheckedChangeListener()
    {
        @Override
        public void onCheckedChanged(RadioGroup Group, int Checkid) {
            // TODO Auto-generated method stub
            RadioButton radioButton = (RadioButton)findViewById(biz_gender.getCheckedRadioButtonId());
            gender = radioButton.getTag().toString();
        }
    };


    private View.OnClickListener mCancelClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            startMainActity();
        }
    };

    private View.OnClickListener mOkClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            register_no = biz_registerno.getText().toString().trim();
            if(TextUtils.isEmpty(register_no))
            {
                tip_info.setHint("请输入外部注册流水号！");
                return;
            }
            apply_type = getSpinnerSelVal(biz_applytype);
            id_type = getSpinnerSelVal(biz_idtype);


            id_no = biz_idno.getText().toString().trim();
            if(TextUtils.isEmpty(id_no))
            {
                tip_info.setHint("请输入证件号！");
                return;
            }

            name = biz_name.getText().toString().trim();
            if(TextUtils.isEmpty(name))
            {
                tip_info.setHint("请输入用户姓名！");
                return;
            }

            birthday = biz_birthday.getText().toString().trim();
            cellphone = biz_cellphone.getText().toString().trim();
            telephone = biz_telephone.getText().toString().trim();
            address = biz_address.getText().toString().trim();
            unit = biz_unit.getText().toString().trim();

            JSONObject bizJson = sdkHelper.getBizJson_register(register_no, register_time, apply_type, id_type, id_no, name, gender, birthday, cellphone, telephone, address,  unit);
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
            String digest  = summary(paraJson);
            sdkHelper.register(method, app_id, term_id, version, timestamp, digest_type, digest, enc_type,
                    register_no, register_time, apply_type, id_type, id_no,
                    name, gender, birthday, cellphone, telephone, address,  unit);
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
            jsonObj.put("register_no", register_no);//向接口请求参数对象里面添加值
            jsonObj.put("register_time", register_time);//向接口请求参数对象里面添加值
            jsonObj.put("apply_type", apply_type);//向接口请求参数对象里面添加值
            jsonObj.put("id_type", id_type);//向接口请求参数对象里面添加值jsonObjBiz.put("method", method);//向接口请求参数对象里面添加值
            jsonObj.put("id_no", id_no);//向接口请求参数对象里面添加值
            jsonObj.put("name", name);//向接口请求参数对象里面添加值
            jsonObj.put("gender", gender);//向接口请求参数对象里面添加值
            if(birthday.length() > 0)
                jsonObj.put("birthday", birthday);//向接口请求参数对象里面添加值
            if(cellphone.length() > 0)
                jsonObj.put("cellphone", cellphone);//向接口请求参数对象里面添加值
            if(telephone.length() > 0)
                jsonObj.put("telephone", telephone);//向接口请求参数对象里面添加值
            if(address.length() > 0)
                jsonObj.put("address", address);//向接口请求参数对象里面添加值
            if(unit.length() > 0)
                jsonObj.put("unit", unit);//向接口请求参数对象里面添加值
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

    private void startMainActity() {
        Intent intent = new Intent(RegisterActivity.this, MainActivity.class);
        startActivity(intent);
        this.finish();
    }

    private void parseJson(String resp)
    {
        String ss = "";
        JSONObject bizJson = null;
        JSONObject json = JsonHelper.getJsonObject(resp);
        JSONObject dataJson = JsonHelper.getJsonObj(json, "data");
        String enc_type = JsonHelper.getString(dataJson, Commons.CommonPara.ENC_TYPE);
        if(enc_type.equalsIgnoreCase("SM4"))
        {
            String bizStr = JsonHelper.getString(dataJson, Commons.CommonPara.BIZ_CONTENT);
            //解密
            String plainStr = sdkHelper.decryptData_ECB(bizStr);
            bizJson = JsonHelper.getJsonObject(plainStr);
        }
        else
            bizJson = JsonHelper.getJsonObj(dataJson, Commons.CommonPara.BIZ_CONTENT);

//        if(bizJson != null) {
//            Global.current_ehealth_card_id = JsonHelper.getString(bizJson, "ehealth_card_id");
//        }
        Log.d("main","ss==" + ss );
    }

}
