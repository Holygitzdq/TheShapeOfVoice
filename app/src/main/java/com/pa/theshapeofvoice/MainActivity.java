package com.pa.theshapeofvoice;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {

    private static final String[] commonFunList = new String[]{

            "龙临阁",
            "藏龙阁",
            "梦龙阁",
            "海龙阁",
    };


    //定义listView
    private ListView listView;

    private void toVoiceVerifyActivity(int position){
        Intent intent_main;
        switch (position){
            case 0:
                intent_main = new Intent(MainActivity.this,VoiceVerifyActivity.class);
                intent_main.putExtra("room",commonFunList[position]);
                break;
            case 1:
                intent_main = new Intent(MainActivity.this,VoiceVerifyActivity.class);
                intent_main.putExtra("room",commonFunList[position]);
                break;
            case 2:
                intent_main = new Intent(MainActivity.this,VoiceVerifyActivity.class);
                intent_main.putExtra("room",commonFunList[position]);
                break;
            case 3:
                intent_main = new Intent(MainActivity.this,VoiceVerifyActivity.class);
                intent_main.putExtra("room",commonFunList[position]);
                break;
            default:
                intent_main = new Intent(MainActivity.this,MainActivity.class);
                break;
        }
        startActivity(intent_main);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initProject(); //初始化，建立服务器和客户端的链接

    }

    private void initProject()  //初始化，建立服务器和客户端的链接
    {
        //new Thread(postThread).start();

        listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(new ArrayAdapter<String>(this,
                android.R.layout.simple_list_item_1,
                commonFunList));
        listView.setOnItemClickListener(
                new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        toVoiceVerifyActivity(position);

                    }
                }
        );
    }

//    private Thread postThread = new Thread() {
//        public void run() {
//            String url_path = new String("http://192.168.23.74:8080/TomcatTest/ServerTest2");
//            String post = new String("name=Hello");
//            ServerConnection url_connect = new ServerConnection(url_path);
//            String str_post = url_connect.postAndGetMessage(post);
//            System.out.println(str_post);
//
//        }
//    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

}
