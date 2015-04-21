package com.example.chatappdemo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


public class XMPPChatDemoActivity extends Activity {


	private EditText recipient;
	private EditText textMessage;
	private ListView listview;
	private Button btn_registration;
	private ChatApp app;

	private ImageView ivStatus;
	private ArrayList<String> messages = new ArrayList<String>();
	private TextView txtStatus;
	private String user;
	ImageView viewImage;
	private Object progressDialog;
	private String memberChat;
    ChatApp.MessageRcd rcd;


	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		ivStatus = (ImageView) findViewById(R.id.ivStatus);
		txtStatus = (TextView) findViewById(R.id.txtStatus);
		viewImage = (ImageView) findViewById(R.id.viewImage);

		app = ChatApp.getInstance();
        app.initializeListener(rcd);

        recipient = (EditText) this.findViewById(R.id.toET);
		textMessage = (EditText) this.findViewById(R.id.chatET);
		listview = (ListView) this.findViewById(R.id.listMessages);
		setListAdapter();

		if (getIntent().getExtras() != null) {
			user = getIntent().getExtras().getString("email");
			recipient.setText(user);
			//updateUserStatus();
			if("unavailable".equals(app.getStatusOfUser(user).name())){
                txtStatus.setText("Offline");
                ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_red_offline));
            }else{
                txtStatus.setText("Online");
                ivStatus.setImageDrawable(getResources().getDrawable(R.drawable.ic_green_online));
            }
			memberChat = getIntent().getExtras().getString("memberChat");
		}

		// Set a listener to send a chat text message
		Button send = (Button) this.findViewById(R.id.sendBtn);
		send.setOnClickListener(new OnClickListener() {
			public void onClick(View view) {
				if (TextUtils.isEmpty(textMessage.getText().toString())) {
					textMessage.setError("Can't send empty message");
					return;
				}
				app.sendMessage(getText(recipient), getText(textMessage),memberChat);
				messages.add(app.getUsername() + ":" + getText(textMessage));
				textMessage.setText("");
			}
		});



	}


	private String getText(EditText et) {
		return et.getText().toString().trim();
	}

	private void setListAdapter() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listitem, messages);
		listview.setAdapter(adapter);
	}
}