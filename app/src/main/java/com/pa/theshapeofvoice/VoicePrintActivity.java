package com.pa.theshapeofvoice;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.provider.Settings;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.KeyEvent;
import android.widget.TextView;

import org.json.JSONObject;
import org.json.JSONTokener;

import pingan.speech.asr.PARecognizer;
import pingan.speech.asr.PARecognizerListener;
import pingan.speech.login.LoginSDK;
import pingan.speech.util.PALogUtil;


public class VoicePrintActivity extends AppCompatActivity {

    private String mTextTemp = "";
    private TextView textView1;
    private PARecognizer asr = new PARecognizer();           // 语音识别对象

    void initSDK()
    {
        // 初始化对象
        String url = getResources().getString(R.string.url_cloud);

        /**使用此APPID需在平安语音云平台注册UserId，并填入下方userId*/
        String app_id = getResources().getString(R.string.app_id);

        /**userId：为app当前使用用户的id，可按此维度查看用户的语音转写数据或用户校验*/
        String user_id = getResources().getString(R.string.user_id);

        LoginSDK.initialSDK(VoicePrintActivity.this, url, app_id, user_id);     //初始化sdk，校验
        asr = asr.createRecognizer(this, url, app_id);

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_print);
        textView1 = (TextView)findViewById(R.id.print_voice);
        textView1.setMovementMethod(ScrollingMovementMethod.getInstance());
        initSDK();
        asr.startListening(paRecognizerListener);

    }

    /**
     * 听写监听器。结果及状态回调
     */
    private PARecognizerListener paRecognizerListener = new PARecognizerListener() {

        @Override
        public void onBeginOfSpeech() {
            PALogUtil.d("info", "开始说话");
        }

        @Override
        public void onError(int errorCode, String errorDescription) {
            PALogUtil.d("error", "错误码:" + errorCode + " " + errorDescription);
        }

        @Override
        public void onEndOfSpeech() {
            PALogUtil.d("info", "结束说话");
        }

        @Override
        public void onResult(String result) {
            String texttemp = "";
            String islast = "";
            PALogUtil.d("info", "result:" + result);
            try {
                JSONTokener tokener = new JSONTokener(result);
                JSONObject joResult = new JSONObject(tokener);
                texttemp = joResult.getString("result");
                islast = joResult.getString("pgs");

            } catch (Exception e) {
                e.printStackTrace();
            }
            PALogUtil.d("ASR", "text:" + texttemp + "---islast:" + islast);

            textView1.setText(mTextTemp + texttemp);


        //        mTextTemp = "";

            if (islast.equals("1")) {
                mTextTemp = textView1.getText().toString();
            }
            PALogUtil.d("shape", "mtext:" + mTextTemp);
        }

        @Override
        public void onVolumeChanged(int volume) {
            PALogUtil.v("verbose", "当前正在说话，音量大小：" + volume);
            //   textView2.setText(volume + "");
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        asr.cancel();
        // 退出时释放连接
        asr.destroy();
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK )
        {
            // 创建退出对话框
            AlertDialog isExit = new AlertDialog.Builder(this).create();
            // 设置对话框标题
            isExit.setTitle("系统提示");
            // 设置对话框消息
            isExit.setMessage("确定要退出吗");
            // 添加选择按钮并注册监听

            isExit.setButton("确认", listener);
            isExit.setButton2("取消", listener);
            // 显示对话框
            isExit.show();

        }

        return false;

    }
    /**监听对话框里面的button点击事件*/
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    onDestroy(); //销毁Activity这个实例
                    finish(); //跳转到前一个界面
                    Intent t_intent = new Intent(VoicePrintActivity.this, MainActivity.class);

                    startActivity(t_intent); //进入下一个页面MainActivity
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };


}
