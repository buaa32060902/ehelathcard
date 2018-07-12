package com.fmsh.ehelathcard;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.fmsh.ehelathcard.AlterInfo.AlterInfoActivity;
import com.fmsh.ehelathcard.Close.CloseActivity;
import com.fmsh.ehelathcard.Generate.GenerateActivity;
import com.fmsh.ehelathcard.Query.QueryActivity;
import com.fmsh.ehelathcard.RegisterActivity.RegisterActivity;
import com.fmsh.ehelathcard.SetAppInfo.SetAppInfoActivity;
import com.fmsh.ehelathcard.SetUrl.SetUrlActivity;
import com.fmsh.ehelathcard.SetVerifyName.SetVerifyNameActivity;
import com.fmsh.ehelathcard.Verify.VerifyActivity;

public class MainActivity extends Activity {
    // app_id，由SDK授权管理系统生成
    public static String appId = "bmwt7kpc079gmmoj0e";
    // app_secret，由SDK授权管理系统生成
    public static String appSecret = "B37496C26E475D07F032F1657E80D7A69FD87E1CCC819A8D74B751293E6A6D76";
    // 加密的服务系统密钥，该密钥从SDK授权管理系统下载
    public static String systemKey = "DE85EF24E0560FBC00BD6E3FB9D19E4D5F9C3C518AECAD1E777346C55F3CE435";
    // 加密服务系统密钥的pin码，该pin码在下载服务系统密钥时输入
    public static String pin = "12345678";
    private Button btn_register, btn_alter, btn_close, btn_query, btn_generate, btn_verify;
    private Button  btn_appInfo, btn_seturl, btn_setsdk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        String appname = getAppName(this);
//        String packagename = getPackageName(this);
//        int ret = SDKVerifier.VerifySDK("复旦微电子健康卡");
//        System.out.println(ret);

        btn_appInfo = (Button)findViewById(R.id.appInfo);
        btn_seturl = (Button)findViewById(R.id.seturl);
        btn_setsdk = (Button)findViewById(R.id.setsdk);
        btn_register = (Button)findViewById(R.id.register);
        btn_alter = (Button)findViewById(R.id.alter);
        btn_close = (Button)findViewById(R.id.close);
        btn_query = (Button)findViewById(R.id.query);
        btn_generate = (Button)findViewById(R.id.generate);
        btn_verify = (Button)findViewById(R.id.verify);

        btn_appInfo.setOnClickListener(SetAppInfoClickListener);
        btn_seturl.setOnClickListener(SetUrlClickListener);
        btn_setsdk.setOnClickListener(SetSDKClickListener);
        btn_register.setOnClickListener(RegisterClickListener);
        btn_alter.setOnClickListener(AlterClickListener);
        btn_close.setOnClickListener(CloseClickListener);
        btn_query.setOnClickListener(QueryClickListener);
        btn_generate.setOnClickListener(GenerateClickListener);
        btn_verify.setOnClickListener(VerifyClickListener);
    }


    public static String getAppName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

    public static String getPackageName(Context context)
    {
        try
        {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.packageName;
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }


    private View.OnClickListener SetAppInfoClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SetAppInfoActivity.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener SetUrlClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SetUrlActivity.class);
            startActivity(intent);
        }
    };
    private View.OnClickListener SetSDKClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Intent intent = new Intent(MainActivity.this, SetVerifyNameActivity.class);
            startActivity(intent);
        }
    };

    private View.OnClickListener RegisterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!Global.configOk)
            {
                Toast.makeText(MainActivity.this, "请先配置APP信息", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.urlOk)
            {
                Toast.makeText(MainActivity.this, "请先配置卡管平台地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.sdkOk)
            {
                Toast.makeText(MainActivity.this, "请先配置SDK校验名", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("appId",Global.appId_ok);
            bundle.putString("appSecret",Global.appSecret_ok);
            bundle.putString("systemKey",Global.systemKey_ok);
            bundle.putString("pin",Global.pin_ok);
            bundle.putString("apptype",Global.apptype_ok);
            bundle.putString("url",Global.url_ok);
            bundle.putString("sdkname",Global.sdk_ok);
            intent.putExtra("bundle",bundle);
            startActivity(intent);

        }
    };
    private View.OnClickListener AlterClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!Global.configOk)
            {
                Toast.makeText(MainActivity.this, "请先配置APP信息", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.urlOk)
            {
                Toast.makeText(MainActivity.this, "请先配置卡管平台地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.sdkOk)
            {
                Toast.makeText(MainActivity.this, "请先配置SDK校验名", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, AlterInfoActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("appId",Global.appId_ok);
            bundle.putString("appSecret",Global.appSecret_ok);
            bundle.putString("systemKey",Global.systemKey_ok);
            bundle.putString("pin",Global.pin_ok);
            bundle.putString("apptype",Global.apptype_ok);
            bundle.putString("url",Global.url_ok);
            bundle.putString("sdkname",Global.sdk_ok);
            intent.putExtra("bundle",bundle);
            startActivity(intent);

        }
    };

    private View.OnClickListener CloseClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!Global.configOk)
            {
                Toast.makeText(MainActivity.this, "请先配置APP信息", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.urlOk)
            {
                Toast.makeText(MainActivity.this, "请先配置卡管平台地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.sdkOk)
            {
                Toast.makeText(MainActivity.this, "请先配置SDK校验名", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, CloseActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("appId",Global.appId_ok);
            bundle.putString("appSecret",Global.appSecret_ok);
            bundle.putString("systemKey",Global.systemKey_ok);
            bundle.putString("pin",Global.pin_ok);
            bundle.putString("apptype",Global.apptype_ok);
            bundle.putString("url",Global.url_ok);
            bundle.putString("sdkname",Global.sdk_ok);
            intent.putExtra("bundle",bundle);
            startActivity(intent);
        }
    };

    private View.OnClickListener QueryClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!Global.configOk)
            {
                Toast.makeText(MainActivity.this, "请先配置APP信息", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.urlOk)
            {
                Toast.makeText(MainActivity.this, "请先配置卡管平台地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.sdkOk)
            {
                Toast.makeText(MainActivity.this, "请先配置SDK校验名", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, QueryActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("appId",Global.appId_ok);
            bundle.putString("appSecret",Global.appSecret_ok);
            bundle.putString("systemKey",Global.systemKey_ok);
            bundle.putString("pin",Global.pin_ok);
            bundle.putString("apptype",Global.apptype_ok);
            bundle.putString("url",Global.url_ok);
            bundle.putString("sdkname",Global.sdk_ok);
            intent.putExtra("bundle",bundle);
            startActivity(intent);
        }
    };

    private View.OnClickListener GenerateClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!Global.configOk)
            {
                Toast.makeText(MainActivity.this, "请先配置APP信息", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.urlOk)
            {
                Toast.makeText(MainActivity.this, "请先配置卡管平台地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.sdkOk)
            {
                Toast.makeText(MainActivity.this, "请先配置SDK校验名", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, GenerateActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("appId",Global.appId_ok);
            bundle.putString("appSecret",Global.appSecret_ok);
            bundle.putString("systemKey",Global.systemKey_ok);
            bundle.putString("pin",Global.pin_ok);
            bundle.putString("apptype",Global.apptype_ok);
            bundle.putString("url",Global.url_ok);
            bundle.putString("sdkname",Global.sdk_ok);
            intent.putExtra("bundle",bundle);
            startActivity(intent);
        }
    };

    private View.OnClickListener VerifyClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(!Global.configOk)
            {
                Toast.makeText(MainActivity.this, "请先配置APP信息", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.urlOk)
            {
                Toast.makeText(MainActivity.this, "请先配置卡管平台地址", Toast.LENGTH_SHORT).show();
                return;
            }
            if(!Global.sdkOk)
            {
                Toast.makeText(MainActivity.this, "请先配置SDK校验名", Toast.LENGTH_SHORT).show();
                return;
            }
            Intent intent = new Intent(MainActivity.this, VerifyActivity.class);
            Bundle bundle=new Bundle();
            bundle.putString("appId",Global.appId_ok);
            bundle.putString("appSecret",Global.appSecret_ok);
            bundle.putString("systemKey",Global.systemKey_ok);
            bundle.putString("pin",Global.pin_ok);
            bundle.putString("apptype",Global.apptype_ok);
            bundle.putString("url",Global.url_ok);
            bundle.putString("sdkname",Global.sdk_ok);
            intent.putExtra("bundle",bundle);
            startActivity(intent);
        }
    };
}
