package com.pa.theshapeofvoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.pingan.paai.recorder.ResultListener;
import com.pingan.paai.recorder.SdkConst;
import com.pingan.paai.recorder.SpeechRecognizer;
import com.pingan.paai.recorder.StageListener;

import org.json.JSONException;
import org.json.JSONObject;

import pingan.speech.util.PALogUtil;

public class VoiceVerifyActivity extends AppCompatActivity {
    //创建声纹注册实例
    private SpeechRecognizer paspeechRecogizer = SpeechRecognizer.getInstance();
    private String speechKey = null;
    private String speechText = null;
    private String verifyString = null;
    private String id_resultVerify = null;

    private StageListener stageListener = new StageListener() {
        @Override
        public  void onRecordBase64String(String base64){
            verifyString = base64;
        }
    };

    private ResultListener resultListener = new ResultListener() {
        @Override
        public void onResult(String s) {
            PALogUtil.d("info", "result:" + s);
            try {
                //register the jsonobject
                JSONObject jsonObject = new JSONObject(s);
                JSONObject data = jsonObject.getJSONObject("data");
                JSONObject returnData = data.getJSONObject("returnData");
               speechText = returnData.getString("speechText");

                //when you send the registe requestion,you need  to send the key to servers
                speechKey = returnData.getString("speechKey");
                id_resultVerify = returnData.getString("code");


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

    private String currentUserName = null;


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
            PALogUtil.d( "please open the AUDIO.");
        }
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_verify);

        Button btn = (Button) findViewById(R.id.btn_voice_verify);
        TextView tview = (TextView) findViewById(R.id.tView_room);
        init_Speech();
        register_voice();




        Intent r_intent = getIntent();
        if (r_intent != null){
            String str_room_name = r_intent.getStringExtra("room");
            tview.setText(str_room_name);
        }

        btn.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                //声纹验证。。。
                paspeechRecogizer.start();
                paspeechRecogizer.getSpeechText(currentUserName, SdkConst.SpeechType.TYPE_VERIFY,
                        SdkConst.SceneType.SCENETYPE_RANDOM, resultListener);

                paspeechRecogizer.verifyCurrent(currentUserName,speechText,speechKey,
                        SdkConst.SceneType.SCENETYPE_RANDOM ,verifyString, resultListener);


                //验证通过，跳转下一个界面，进入直播间。。。

                if(id_resultVerify == "603") //验证成功
                {
                    Intent t_intent = new Intent(VoiceVerifyActivity.this, VoicePrintActivity.class);
                    paspeechRecogizer.stop();
                    startActivity(t_intent);
                }else
                {
                    System.out.println(id_resultVerify);
                }

                return true;
            }
        });


    }

}
