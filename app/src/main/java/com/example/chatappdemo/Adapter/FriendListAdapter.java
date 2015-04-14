package com.example.chatappdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chatappdemo.ChatApp;
import com.example.chatappdemo.R;

import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.roster.Roster;
import org.jivesoftware.smack.roster.RosterEntry;

import java.util.ArrayList;

public class FriendListAdapter extends BaseAdapter {
	private Context context;
	private LayoutInflater inflater;
    private ChatApp app;
    private ArrayList<RosterEntry> friendList;
    private Roster roster;


	public FriendListAdapter(Context context, ArrayList<RosterEntry> friendList, Roster roster) {
		this.context = context;
		this.friendList = friendList;
        this.roster = roster;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return friendList.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return friendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	public class ViewHolderItem {
		private TextView tvFriendName;
		private TextView tvDenny;
	}

	@Override
	public View getView(final int position, View v, ViewGroup arg2) {

		ViewHolderItem holder;
		if (v == null) {
			holder = new ViewHolderItem();
			v = inflater.inflate(R.layout.list_row_myfriend_list, null);
			holder.tvFriendName = (TextView) v.findViewById(R.id.tvFriendName);
			holder.tvDenny = (TextView) v.findViewById(R.id.tvDenny);
			v.setTag(holder);
		} else {
			holder = (ViewHolderItem) v.getTag();
		}
		app = ChatApp.getInstance();
		if (friendList.get(position) != null) {
			holder.tvFriendName.setText("" + friendList.get(position).getName());
		}
       // roster.getPresence(friendList.get(position).getUser());
        Presence entryPresence = roster.getPresence(friendList.get(position).getUser());
        Presence.Type type = entryPresence.getType();
        if("unavailable".equals(type.name())){
            holder.tvDenny.setText("Offline");
        }else{
            holder.tvDenny.setText("Online");
        }
		return v;
	}

}
