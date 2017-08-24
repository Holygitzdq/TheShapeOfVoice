package com.pa.theshapeofvoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class VoiceVerifyActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_verify);

        Button btn = (Button) findViewById(R.id.btn_voice_verify);
        TextView tview = (TextView) findViewById(R.id.tView_room);

        Intent r_intent = getIntent();
        if (r_intent != null){
            String str_room_name = r_intent.getStringExtra("room");
            tview.setText(str_room_name);
        }

        btn.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                //声纹验证。。。


                //验证通过，跳转下一个界面，进入直播间。。。
                Intent t_intent = new Intent(VoiceVerifyActivity.this, VoicePrintActivity.class);
                startActivity(t_intent);
                return true;
            }
        });


    }

}
