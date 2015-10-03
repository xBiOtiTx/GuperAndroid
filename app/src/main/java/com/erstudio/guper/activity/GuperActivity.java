package com.erstudio.guper.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.erstudio.guper.R;
import com.erstudio.guper.adapter.MessageAdapter;
import com.erstudio.guper.model.Location;
import com.erstudio.guper.model.Message;
import com.erstudio.guper.model.WSMessage;
import com.erstudio.guper.model.WSMessageType;
import com.erstudio.guper.service.GuperWSService;
import com.erstudio.guper.service.GuperWSServiceListener;
import com.erstudio.guper.service.GuperWSServiceStatus;

import java.util.ArrayList;
import java.util.List;

// http://stackoverflow.com/questions/16135984/full-screen-background-image-in-an-activity
// Картинка - "мелкие детали"
// http://stackoverflow.com/questions/26575197/no-shadow-by-default-on-toolbar // тень для тулбара
public class GuperActivity extends AppCompatActivity {

    private GuperWSService guperWSService;

    private RecyclerView mRecyclerView;
    private MessageAdapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private List<String> mItems;

    private ImageButton mSendButton;
    private EditText mMessageEditText;

    private void initRecyclerView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.messages);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        //mLayoutManager.setReverseLayout(true);
        mLayoutManager.setStackFromEnd(true);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mItems = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            String s = "";
            int len = (int) (Math.random() * 100);
            for (int j = 0; j < len; j++) {
                s += Character.toString((char) (Math.random() * 255));
            }
            mItems.add(s);
        }

        // specify an adapter (see also next example)
        mAdapter = new MessageAdapter(mItems);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void sendMessage() {
        Editable editable = mMessageEditText.getText();
        String stringMessage = editable.toString().trim();
        editable.clear();

        if (!stringMessage.equals("")) {
            mAdapter.add(stringMessage);
            mRecyclerView.smoothScrollToPosition(mAdapter.getItemCount() - 1);

            Message message = new Message(new Location(0f, 0f), stringMessage);
            WSMessage wsMessage = new WSMessage(WSMessageType.SEND_MESSAGE, message);
            guperWSService.send(wsMessage);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        guperWSService = new GuperWSService(this);
        guperWSService.setGuperWSServiceListener(guperWSServiceListener);

        setContentView(R.layout.activity_guper);
        initRecyclerView();

        mSendButton = (ImageButton) findViewById(R.id.send_message);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        mMessageEditText = (EditText) findViewById(R.id.text_message);
    }

    @Override
    protected void onResume() {
        super.onResume();
        guperWSService.connect();
    }

    @Override
    protected void onPause() {
        super.onPause();
        guperWSService.disconnect();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_guper, menu);
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

    //----------------------------------------------------------------------------------------------
    // GuperWSServiceListener
    //----------------------------------------------------------------------------------------------

    private GuperWSServiceListener guperWSServiceListener = new GuperWSServiceListener() {
        @Override
        public void onConnect() {
            Log.i("GuperWSServiceListener", "onConnect");
        }

        @Override
        public void onDisconnect() {
            Log.i("GuperWSServiceListener", "onDisconnect");
        }

        @Override
        public void onError(Exception ex) {
            Log.i("GuperWSServiceListener", "onError: " + ex);
        }

        @Override
        public void onStatusChange(GuperWSServiceStatus status) {
            Log.i("GuperWSServiceListener", "onStatusChange: " + status);
        }

        @Override
        public void onConnecting() {
            Log.i("GuperWSServiceListener", "onConnecting");
        }

        @Override
        public void onDisconnecting() {
            Log.i("GuperWSServiceListener", "onDisconnecting");
        }

        @Override
        public void onPing(String s) {
            Log.i("GuperWSServiceListener", "onPing: " + s);
        }

        @Override
        public void onPong(String s) {
            Log.i("GuperWSServiceListener", "onPong: " + s);
        }

        @Override
        public void onSend(WSMessage wsMessage) {
            Log.i("GuperWSServiceListener", "onSend: " + wsMessage);
        }

        @Override
        public void onReceive(WSMessage wsMessage) {
            Log.i("GuperWSServiceListener", "onReceive: " + wsMessage);
        }

        @Override
        public void onConfirm(WSMessage wsMessage) {
            Log.i("GuperWSServiceListener", "onConfirm: " + wsMessage);
        }
    };
}
