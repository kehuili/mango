package com.example.client.utils;

import com.example.tools.ContextUtil;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class ExceptionHandler extends Handler {
	public static final int SHOW_MESSAGE = 0x0001;
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		if (msg.what == SHOW_MESSAGE) {
			Toast.makeText(ContextUtil.getInstance(), msg.obj.toString(),
					Toast.LENGTH_LONG).show();
		}
	}

	public void showMessage(String message) {
		Message msg = Message
				.obtain(this, SHOW_MESSAGE);
		msg.obj = message;
		msg.sendToTarget();
	}
}
