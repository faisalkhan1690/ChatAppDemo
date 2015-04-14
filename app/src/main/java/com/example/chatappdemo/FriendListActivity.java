package com.example.chatappdemo;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import com.example.chatappdemo.Adapter.FriendListAdapter;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;
import java.util.Collection;


public class FriendListActivity extends Activity {
    private ListView lvFriendList;
    private ChatApp chatApp;
    private ArrayList<RosterEntry> friendList = new ArrayList<RosterEntry>();
    private FriendListAdapter friendListAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friend_list);

        chatApp = ChatApp.getInstance();
        Roster roster = Roster.getInstanceFor(chatApp.connection);

        Collection<RosterEntry> entries = roster.getEntries();
        for (RosterEntry entry : entries) {
            //System.out.println("Here: " + entry);
          /*  System.out.println("Here: " +  roster.getPresence(entry.getUser()));
            Log.i("userPresence", ""+roster.getPresence(entry.getUser()));
            Presence entryPresence = roster.getPresence(entry.getUser());
            Presence.Type type = entryPresence.getType();
            System.out.println("type: " +  type);
            Log.i("userPresence", ""+roster.getPresence(entry.getUser()));*/
            //roster.getPresence(entry.getUser());
            friendList.add(entry);

        }

        lvFriendList = (ListView)findViewById(R.id.lvFriendList);
        friendListAdapter = new FriendListAdapter(FriendListActivity.this,	friendList, roster);
        lvFriendList.setAdapter(friendListAdapter);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_friend_list, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
