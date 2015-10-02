package com.erstudio.guper.service;

import android.content.Context;
import android.os.Handler;

import com.erstudio.guper.model.WSMessage;
import com.google.gson.Gson;
import com.google.inject.Inject;
import com.koushikdutta.async.callback.CompletedCallback;
import com.koushikdutta.async.http.AsyncHttpClient;
import com.koushikdutta.async.http.WebSocket;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Евгений on 20.09.2015.
 */
public class GuperWSService {
    private static final String url = "ws://192.168.1.2:8080/Guper/guperendpoint";

    private Context mContext;
    private GuperWSServiceStatus mStatus;
    private GuperWSServiceListener mListener;
    private WebSocket mWebSocket;
    private List<WSMessage> queue;

    @Inject
    public GuperWSService(Context context) {
        mContext = context.getApplicationContext();
        mStatus = GuperWSServiceStatus.DISCONNECTED;
        mListener = null;
        mWebSocket = null;
        queue = new ArrayList<>();
    }

    public GuperWSServiceListener getmGuperWSServiceListener() {
        return mListener;
    }

    public void setGuperWSServiceListener(GuperWSServiceListener mListener) {
        this.mListener = mListener;
    }

    public GuperWSServiceStatus getStatus() {
        return mStatus;
    }


    public void send(WSMessage wsMessage) {
        if (isConnected()) {
            mWebSocket.send(new Gson().toJson(wsMessage));
        } else {
            addWSMessage(wsMessage);
            connect();
        }
    }

    public void connect() {
        setStatus(GuperWSServiceStatus.CONNECTING);
        AsyncHttpClient.getDefaultInstance().websocket(url, null, webSocketConnectCallback);
        runOnContextThread(new Runnable() {
            @Override
            public void run() {
                mListener.onConnecting();
            }
        });
    }

    public void disconnect() {
        setStatus(GuperWSServiceStatus.DISCONNECTING);
        close();
        runOnContextThread(new Runnable() {
            @Override
            public void run() {
                mListener.onDisconnecting();
            }
        });
    }

    public void ping() {
        if (isConnected()) {
            String pingMessage = "ping";
            mWebSocket.ping(pingMessage);
            onPing(pingMessage);
        }
    }

    // ---------------------------------------------------------------------------------------------
    // private methods
    // ---------------------------------------------------------------------------------------------

    private boolean isConnected() {
        boolean result = true;

        if (mWebSocket == null || !mWebSocket.isOpen()) {
            result = false;
        }

        if (!mStatus.equals(GuperWSServiceStatus.CONNECTED)) {
            result = false;
        }

        return result;
    }

    private void addWSMessage(WSMessage message) {
        queue.add(message);
    }

    private void removeWSMessage(WSMessage message) {
        queue.remove(message);
    }

    private void clearWSMessageQueue() {
        queue.clear();
    }

    private void setStatus(GuperWSServiceStatus status) {
        if (!mStatus.equals(status)) {
            mListener.onStatusChange(status);
        }
        mStatus = status;
    }

    private void close() {
        if (mWebSocket != null) {
            mWebSocket.setClosedCallback(null);
            mWebSocket.setEndCallback(null);
            mWebSocket.setPongCallback(null);
            mWebSocket.setStringCallback(null);
            mWebSocket.close();
            mWebSocket = null;
        }
    }

    private void runOnContextThread(Runnable runnable) {
        Handler mHandler = new Handler(mContext.getMainLooper());
        mHandler.post(runnable);
    }

    // ---------------------------------------------------------------------------------------------
    // Events
    // ---------------------------------------------------------------------------------------------

    private void onError(final Exception ex) {
        setStatus(GuperWSServiceStatus.ERROR);


        runOnContextThread(new Runnable() {
            @Override
            public void run() {
                mListener.onError(ex);
            }
        });
    }

    private void onConnect(WebSocket webSocket) {
        setStatus(GuperWSServiceStatus.CONNECTED);

        mWebSocket = webSocket;

        mWebSocket.setClosedCallback(onClosedCallback);
        mWebSocket.setEndCallback(onEndCallback);

        mWebSocket.setPongCallback(onPongCallback);
        mWebSocket.setStringCallback(stringCallback);

        runOnContextThread(new Runnable() {
            @Override
            public void run() {
                mListener.onConnect();
            }
        });
    }

    private void onDisconnect() {
        setStatus(GuperWSServiceStatus.DISCONNECTED);
        runOnContextThread(new Runnable() {
            @Override
            public void run() {
                mListener.onDisconnect();
            }
        });
    }

    private void checkReceivedWSMessage() {

    }

    private void onReceive(String s) {
        final WSMessage message = new Gson().fromJson(s, WSMessage.class);
        checkReceivedWSMessage();

        runOnContextThread(new Runnable() {
            @Override
            public void run() {
                mListener.onReceive(message);
            }
        });
    }

    private void onPing(final String s) {

        runOnContextThread(new Runnable() {
            @Override
            public void run() {
                mListener.onPing(s);
            }
        });
    }

    private void onPong(final String s) {

        runOnContextThread(new Runnable() {
            @Override
            public void run() {
                mListener.onPong(s);
            }
        });
    }

    // ---------------------------------------------------------------------------------------------
    // callbacks
    // ---------------------------------------------------------------------------------------------

    private AsyncHttpClient.WebSocketConnectCallback webSocketConnectCallback = new AsyncHttpClient.WebSocketConnectCallback() {
        @Override
        public void onCompleted(Exception ex, WebSocket webSocket) {
            if (ex != null) {
                onError(ex);
            } else {
                onConnect(webSocket);
            }
        }
    };

    private CompletedCallback onClosedCallback = new CompletedCallback() {
        @Override
        public void onCompleted(Exception ex) {
            if (ex == null) {
                onDisconnect();
            } else {
                onError(ex);
            }
        }
    };

    private CompletedCallback onEndCallback = new CompletedCallback() {
        @Override
        public void onCompleted(Exception ex) {
            if (ex == null) {
                onDisconnect();
            } else {
                onError(ex);
            }
        }
    };

    private WebSocket.PongCallback onPongCallback = new WebSocket.PongCallback() {
        @Override
        public void onPongReceived(String s) {
            onPong(s);
        }
    };

    private WebSocket.StringCallback stringCallback = new WebSocket.StringCallback() {
        @Override
        public void onStringAvailable(String s) {
            onReceive(s);
        }
    };
}
