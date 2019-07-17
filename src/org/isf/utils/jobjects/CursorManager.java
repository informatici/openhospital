package org.isf.utils.jobjects;

import java.awt.*;
import java.awt.event.InputEvent;
import java.util.*;

class CursorManager {
  private final DelayTimer waitTimer;
  private final Stack<DispatchedEvent> dispatchedEvents;
  private boolean needsCleanup;

  public CursorManager(DelayTimer waitTimer) {
    this.dispatchedEvents = new Stack<DispatchedEvent>();
    this.waitTimer = waitTimer;
  }
  private void cleanUp() {
    if (((DispatchedEvent) dispatchedEvents.peek()).resetCursor()) {
      clearQueueOfInputEvents();
    }
  }
  private void clearQueueOfInputEvents() {
    EventQueue q = Toolkit.getDefaultToolkit().getSystemEventQueue();
    synchronized (q) {
      ArrayList<AWTEvent> nonInputEvents = gatherNonInputEvents(q);
      for (Iterator<AWTEvent> it = nonInputEvents.iterator(); it.hasNext();)
        q.postEvent((AWTEvent)it.next());
    }
  }
  private ArrayList<AWTEvent> gatherNonInputEvents(EventQueue systemQueue) {
    ArrayList<AWTEvent> events = new ArrayList<AWTEvent>();
    while (systemQueue.peekEvent() != null) {
      try {
        AWTEvent nextEvent = systemQueue.getNextEvent();
        if (!(nextEvent instanceof InputEvent)) {
          events.add(nextEvent);
        }
      } catch (InterruptedException ie) {
        Thread.currentThread().interrupt();
      }
    }
    return events;
  }
  public void push(Object source) {
    if (needsCleanup) {
      waitTimer.stopTimer();
      cleanUp(); 
      //this corrects the state when a modal dialog 
      //opened last time round
    }
    dispatchedEvents.push(new DispatchedEvent(source));
    needsCleanup = true;
  }
  public void pop() {
    cleanUp();
    dispatchedEvents.pop();
    if (!dispatchedEvents.isEmpty()) {
      //this will be stopped if getNextEvent() is called - 
      //used to watch for modal dialogs closing
      waitTimer.startTimer(); 
    } else {
      needsCleanup = false;
    }
  }
  public void setCursor() {
    ((DispatchedEvent) dispatchedEvents.peek()).setCursor();
  }
}
