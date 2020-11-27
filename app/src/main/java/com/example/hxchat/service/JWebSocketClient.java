package com.example.hxchat.service;

import android.util.Log;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_6455;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.util.Map;

import timber.log.Timber;

public class JWebSocketClient extends WebSocketClient {
    public JWebSocketClient(URI serverUri, Map<String, String> httpHeaders) {
        super(serverUri, new Draft_6455(), httpHeaders);
    }

    @Override
    public void onOpen(ServerHandshake handshakedata) {
        Timber.e("onOpen()");
    }

    @Override
    public void onMessage(String message) {
        Timber.e("onMessage()");
    }

    @Override
    public void onClose(int code, String reason, boolean remote) {
        Log.e("JWebSocketClient", "onClose():" + reason + "code:" + code);
    }

    @Override
    public void onError(Exception ex) {
        Timber.e("onError()");
    }
}
