package com.example.chatappdemo;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ListView;

public class PendingFriendActivity extends Activity {

    private ListView lvRequestList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pending_friend);

        lvRequestList=(ListView)findViewById(R.id.lvRequestList);


    }

}
