package com.pa.theshapeofvoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class VoiceRegisterActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_register);

        Button btn_reg = (Button) findViewById(R.id.btn_register);
        final EditText etext_name = (EditText) findViewById(R.id.et_name);

        btn_reg.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                if(etext_name.getText().length()==0) {
                    Toast.makeText(getApplicationContext(), "姓名不能为空！", Toast.LENGTH_SHORT).show();
                }

                else {
                    //创建声纹注册实例
                    int flag = 0; // 声纹注册通过标志，默认0，表示不通过，1表示通过

                    //开始注册


                    //注册成功,并判断姓名是否为空
                    if (flag==0 ) {
                        //摧毁声纹注册实例
                        Intent t_intent = new Intent(VoiceRegisterActivity.this, MainActivity.class);

                        startActivity(t_intent); //进入下一个页面Main
                    }

                    else  //注册失败
                    {
                        //摧毁声纹注册实例
                        Toast.makeText(getApplicationContext(), "声纹注册失败，重新注册！", Toast.LENGTH_SHORT).show();
                    }

                }

                return true;
            }
        });

    }

}
