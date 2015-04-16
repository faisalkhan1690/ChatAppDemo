package com.example.chatappdemo.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.Toast;
import com.example.chatappdemo.ChatApp;
import com.example.chatappdemo.R;
import org.jivesoftware.smack.roster.RosterEntry;
import java.util.List;

public class FriendRequestAdapter extends BaseAdapter {

	private Context context;
	private List<RosterEntry> friendList;
	private LayoutInflater inflater;
	private ChatApp app;
	private String nickname;
	private String idExtension;

	public FriendRequestAdapter(Context context, List<RosterEntry> friendList) {
		this.context = context;
		this.friendList = friendList;
		inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

	}

	@Override
	public int getCount() {
		return friendList.size();
	}

	@Override
	public Object getItem(int position) {
		return friendList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	public class ViewHolderItem {
		private TextView tvFriendName;
		private TextView tvAccept;
		private TextView tvDenny;
	}

	@Override
	public View getView(final int position, View v, ViewGroup arg2) {

		ViewHolderItem holder;
		if (v == null) {
			holder = new ViewHolderItem();
			v = inflater.inflate(R.layout.list_row_friend_request_list, null);
			holder.tvFriendName = (TextView) v.findViewById(R.id.tvFriendName);
			holder.tvAccept = (TextView) v.findViewById(R.id.tvAccept);
			holder.tvDenny = (TextView) v.findViewById(R.id.tvDenny);
			v.setTag(holder);
		} else {
			holder = (ViewHolderItem) v.getTag();
		}

		app = ChatApp.getInstance();
		if (friendList.get(position) != null) {
			holder.tvFriendName.setText("" + friendList.get(position).getUser());
		}
		// accept friend request
		holder.tvAccept.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (AcceptFriendRequestSubscribed(friendList.get(position).getUser())) {
					Toast.makeText(context, "Accept Friend Request Successfully", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Accept Friend Request fail", Toast.LENGTH_SHORT).show();
				}

			}

			private boolean AcceptFriendRequestSubscribed(String mFriendName) {
                return app.acceptFriendRequest(mFriendName);
			}
		});

		// denny friend request
		holder.tvDenny.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				if (RejectFriendRequest(friendList.get(position).getUser())) {
					Toast.makeText(context, "Denny Friend Request Successfully", Toast.LENGTH_SHORT).show();
				} else {
					Toast.makeText(context, "Denny Friend Request fail", Toast.LENGTH_SHORT).show();
				}

			}

			private boolean RejectFriendRequest(String mFriendName) {
                return app.rejectFriendRequest(mFriendName);
			}
		});
		return v;
	}

}
