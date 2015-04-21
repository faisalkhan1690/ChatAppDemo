package com.example.chatappdemo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import com.example.chatappdemo.Adapter.FriendListAdapter;

import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;
import java.util.ArrayList;


public class FriendListActivity extends Activity implements View.OnClickListener {
    private ListView lvFriendList;
    private ChatApp chatApp;
    private ArrayList<RosterEntry> friendList;
    private FriendListAdapter friendListAdapter;
    private TextView tvAddFriend;
    private TextView tvFriendRequest;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        tvAddFriend = (TextView) findViewById(R.id.tvAddFriend);
        tvFriendRequest = (TextView) findViewById(R.id.tvFriendsRequest);

        chatApp = ChatApp.getInstance();
        Roster roster = Roster.getInstanceFor(chatApp.connection);

        friendList=chatApp.getAllFriendList();

        // faisal

        lvFriendList = (ListView) findViewById(R.id.lvFriendList);
        friendListAdapter = new FriendListAdapter(FriendListActivity.this, friendList, roster);
        lvFriendList.setAdapter(friendListAdapter);

        lvFriendList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(FriendListActivity.this, XMPPChatDemoActivity.class);
                intent.putExtra("email", friendList.get(position).getUser().toString());
                startActivity(intent);
            }
        });


        /**
         * click on tab button
         */

        tvAddFriend.setOnClickListener(this);
        tvFriendRequest.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tvAddFriend: {
                Intent intent = new Intent(FriendListActivity.this, SendFriendRequestActivity.class);
                startActivity(intent);
            }

            break;
            case R.id.tvFriendsRequest: {

                Intent intent = new Intent(FriendListActivity.this, ReceiveFriendRequestActivity.class);
                startActivity(intent);
            }

            default:
                break;
        }
    }
}