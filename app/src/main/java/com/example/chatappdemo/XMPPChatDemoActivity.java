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


public class XMPPChatDemoActivity extends Activity implements OnClickListener, ChatApp.MessageRcd {


	private EditText recipient;
	private EditText textMessage;
	private ListView listview;
	private Button btn_registration;
	private ChatApp app;

	private ImageView ivStatus;
	private ArrayList<String> messages = new ArrayList<String>();
	private TextView txtStatus;
	private String user;
	private TextView txtFileTransfer;
	private ImageView imgDocument;
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
        rcd=this;
        app.initializeListener(rcd);

       // app.recieveFile(app.connection);



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


		// Choose image from gallery
		imgDocument = (ImageView) findViewById(R.id.imgDocument);
		imgDocument.setOnClickListener(this);
	}


	private String getText(EditText et) {
		return et.getText().toString().trim();
	}

	private void setListAdapter() {
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.listitem, messages);
		listview.setAdapter(adapter);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		//app.sendMessage(getText(recipient), "Bye",memberChat);

	}
    @Override
    public void onMessageReceived(String message) {

        messages.add(message);
        setListAdapter();
    }

    @Override
    public void onClick(View v) {

        switch (v.getId())
        {
            case R.id.imgDocument:
                    selectImage();
                break;

            default:
                break;
        }

    }



    private void selectImage() {

        final CharSequence[] options = { "Take Photo", "Choose from Gallery", "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(XMPPChatDemoActivity.this);
        builder.setTitle("Add Photo!");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (options[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment.getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, 1);
                } else if (options[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    startActivityForResult(intent, 2);

                } else if (options[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == 1) {
                File f = new File(Environment.getExternalStorageDirectory().toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }

                try {
                    Bitmap bitmap;
                    BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();

                    bitmap = BitmapFactory.decodeFile(f.getAbsolutePath(), bitmapOptions);

                    viewImage.setImageBitmap(bitmap);

                    String path = android.os.Environment.getExternalStorageDirectory() + File.separator + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream outFile = null;
                    File file = new File(path, String.valueOf(System.currentTimeMillis()) + ".jpg");
                    try {
                        outFile = new FileOutputStream(file);
                        bitmap.compress(Bitmap.CompressFormat.JPEG, 85, outFile);
                        outFile.flush();
                        outFile.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == 2) {

                Uri selectedImage = data.getData();
                String[] filePath = { MediaStore.Images.Media.DATA };
                Cursor c = getContentResolver().query(selectedImage, filePath, null, null, null);
                c.moveToFirst();
                int columnIndex = c.getColumnIndex(filePath[0]);
                String picturePath = c.getString(columnIndex);
                c.close();
                Bitmap thumbnail = (BitmapFactory.decodeFile(picturePath));
            //    Log.i("path of image from gallery......******************.........", picturePath + "");
                viewImage.setImageBitmap(thumbnail);
                //sendData(user, picturePath);
                app.fileTransfer(picturePath,thumbnail, getText(recipient));
            }
        }

    }
}