package com.yarin.android.Examples_05_01;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.MotionEvent;

public class Activity01 extends Activity {
	private static final int REFRESH = 0x000001;

	/* 声明GameView类对象 */
	private GameView mGameView = null;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		/* 实例化GameView对象 */
		this.mGameView = new GameView(this);

		// 设置显示为我们自定义的View(GameView)
		setContentView(mGameView);

		// 开启线程
		new Thread(new GameThread()).start();
	}
    // the message handler, by Tom Xue
	Handler myHandler = new Handler() {
		// 接收到消息后处理
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case Activity01.REFRESH:
                // will be called at some point in the future, by Tom Xue
				mGameView.invalidate();
				break;
			}
			super.handleMessage(msg);
		}
	};

	class GameThread implements Runnable {
		public void run() {
			while (!Thread.currentThread().isInterrupted()) {
				Message message = new Message();
				// User-defined message code so that the recipient can identify
				// what this message is about. Each Handler has its own
				// name-space for message codes, so you do not need to worry
				// about yours conflicting with other handlers.
				message.what = Activity01.REFRESH;
				// 发送消息
				Activity01.this.myHandler.sendMessage(message);
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	/**
	 * 当然可以将GameThread类这样写 同样可以更新界面，并且不在需要 Handler在接受消息 class GameThread
	 * implements Runnable { public void run() { while
	 * (!Thread.currentThread().isInterrupted()) { try { Thread.sleep(100); }
	 * catch (InterruptedException e) { Thread.currentThread().interrupt(); }
	 * //使用postInvalidate可以直接在线程中更新界面 mGameView.postInvalidate(); } } }
	 */

	// 详细事件处理见第三章
	// 当然这些事件也可以写在GameView中
	// 触笔事件
	public boolean onTouchEvent(MotionEvent event) {
		mGameView.y += 5;
		return true;
	}

	// 按键按下事件
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return true;
	}

	// 按键弹起事件
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		switch (keyCode) {
		// 上方向键
		case KeyEvent.KEYCODE_DPAD_UP:
			mGameView.y -= 3;
			break;
		// 下方向键
		case KeyEvent.KEYCODE_DPAD_DOWN:
			mGameView.y += 3;
			break;
		}
		return false;
	}

	public boolean onKeyMultiple(int keyCode, int repeatCount, KeyEvent event) {
		return true;
	}
}
