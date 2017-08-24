package com.pa.theshapeofvoice;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pingan.paai.recorder.ResultListener;
import com.pingan.paai.recorder.SdkConst;
import com.pingan.paai.recorder.SpeechRecognizer;
import com.pingan.paai.recorder.StageListener;

import org.json.JSONException;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import pingan.speech.util.PALogUtil;


public class VoiceRegisterActivity extends AppCompatActivity {

    //add the variable
    private String currentUserName = "zdq";
    private String[] speech_text = new String[3];
    private String [] speech_key = new String[3];
    List<String> voiceResult =new ArrayList<>(Arrays.asList("","",""));
    private String id_result = null;

    //zwj jh
    private Button btn_reg = null; //声纹注册按键
    private EditText edt_usname = null; //声纹注册姓名
    private int reg_time = 0; //声纹注册次数

    private TextView tv_key = null;

    //创建声纹注册实例
    private SpeechRecognizer paspeechRecogizer = SpeechRecognizer.getInstance();

    private StageListener stageListener = new StageListener() {
        @Override
        public  void onRecordBase64String(String base64){
            voiceResult.set(reg_time,base64);
        }
    };

    ResultListener resultListener = new ResultListener() {
        @Override
        public void onResult(String s) {
            PALogUtil.d("info", "result:" + s);
            try {
                //register the jsonobject
                JSONObject jsonObject = new JSONObject(s);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject returnData = data.getJSONObject("returnData");
                speech_text = returnData.getString("speechText").split(";");   //3个数字
                //when you send the registe requestion,you need  to send the key to servers
                speech_key = returnData.getString("speechKey").split(";");    //store the key

                id_result = returnData.getString("code");//store the result value ID;

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onNetworkUnavailable() {
            PALogUtil.d("network is unavailable!");
        }

        @Override
        public void onFailed(String s) {
            PALogUtil.d("onFailed\n\n\n\n\n\n");
        }
    };


    public void init_Speech(){
        SpeechRecognizer.init(this);
        SpeechRecognizer.getInstance().setAppId("90007");
        SpeechRecognizer.getInstance().setLogEnable(true);
    }
    public void register_voice()
    {
        if(paspeechRecogizer.isRecordAudioPermissionGranted())
        {
            paspeechRecogizer.setStageListener(stageListener);
            paspeechRecogizer.setMaxStallTime(1000);//set the quiet time when you record
            paspeechRecogizer.setMinRecordTime(2000);//set the min record time
            paspeechRecogizer.setMaxRecordTime(6000);//set the max record time
        } else

        {   ///audio is closing
            PALogUtil.d("please open the AUDIO.");
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_register);
        initActivity();
        tv_key.setText("hello");
    }


    private void initActivity()
    {
        reg_time = 0;
        btn_reg = (Button) findViewById(R.id.btn_register);
        edt_usname = (EditText) findViewById(R.id.edt_usname);
        tv_key = (TextView) findViewById(R.id.tv_reg_numb);
        Toast.makeText(getApplicationContext(), "输入姓名，长按话筒读出上述口令！", Toast.LENGTH_LONG).show();

        init_Speech();
        register_voice();
        paspeechRecogizer.getSpeechText(currentUserName, SdkConst.SpeechType.TYPE_REGISTER,
                SdkConst.SceneType.SCENETYPE_RANDOM, resultListener);



        try {
            btn_reg.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View view, MotionEvent motionEvent) {
                    if (edt_usname.getText().length() == 0) {
                        Toast.makeText(getApplicationContext(), "姓名不能为空！", Toast.LENGTH_SHORT).show();
                    }
                    //按下操作
                    if (motionEvent.getAction() == MotionEvent.ACTION_DOWN) {
                        if (paspeechRecogizer.start()) {
                            tv_key.setText(speech_text[reg_time]); //0,
                            paspeechRecogizer.isUserRegistered(currentUserName,resultListener);
                            if(id_result == "201")
                                //已经注册了
                                if(id_result == "203") {
                                    paspeechRecogizer.register(currentUserName, SdkConst.RegisterType.TYPE_REGISTER,
                                            speech_text[reg_time], speech_key[reg_time], SdkConst.SceneType.SCENETYPE_RANDOM,
                                            voiceResult, resultListener); //音频数据和监听结果
                                }
                        } else {
                            Toast.makeText(getApplicationContext(), "声纹注册失败，请重试！", Toast.LENGTH_SHORT).show();
                        }
                    }
                    //抬起操作
                    if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                        paspeechRecogizer.stop();
                        Toast.makeText(getApplicationContext(), "声纹注册成功" + reg_time + "次", Toast.LENGTH_SHORT).show();
                        reg_time++;
                        if (reg_time == 3) //判断声纹是否已经注册三次
                        {
                            Toast.makeText(getApplicationContext(), "声纹注册成功！", Toast.LENGTH_SHORT).show();
                            Intent t_intent = new Intent(VoiceRegisterActivity.this, MainActivity.class);
                            startActivity(t_intent); //进入下一个页面
                        }

                    }
                    return false;
                }
            });
        }
        catch (Exception e) {
        e.printStackTrace();
    }
    }

}
