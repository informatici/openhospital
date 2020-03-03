package org.isf.utils.jobjects;

import java.awt.*;


public class WaitCursorEventQueue extends EventQueue implements DelayTimerCallback {
	private final CursorManager cursorManager;
	private final DelayTimer waitTimer;

	public WaitCursorEventQueue(int delay) {
		this.waitTimer = new DelayTimer(this, delay);
		this.cursorManager = new CursorManager(waitTimer);
	}

	public void close() {
		waitTimer.quit();
		pop();
	}

	protected void dispatchEvent(AWTEvent event) {
		cursorManager.push(event.getSource());
		waitTimer.startTimer();
		try {
			super.dispatchEvent(event);
		} finally {
			waitTimer.stopTimer();
			cursorManager.pop();
		}
	}

	public AWTEvent getNextEvent() throws InterruptedException {
		waitTimer.stopTimer();
		return super.getNextEvent();
	}

	public void trigger() {
		cursorManager.setCursor();
	}
}