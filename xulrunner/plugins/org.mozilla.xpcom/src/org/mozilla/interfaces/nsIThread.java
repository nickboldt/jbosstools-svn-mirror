/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/xpcom/threads/nsIThread.idl
 */

package org.mozilla.interfaces;

/**
 * This interface provides a high-level abstraction for an operating system
 * thread.
 *
 * Threads have a built-in event queue, and a thread is an event target that
 * can receive nsIRunnable objects (events) to be processed on the thread.
 *
 * See nsIThreadManager for the API used to create and locate threads.
 */
public interface nsIThread extends nsIEventTarget {

  String NS_ITHREAD_IID =
    "{9c889946-a73a-4af3-ae9a-ea64f7d4e3ca}";

  /**
   * Shutdown the thread.  This method prevents further dispatch of events to
   * the thread, and it causes any pending events to run to completion before
   * the thread joins (see PR_JoinThread) with the current thread.  During this
   * method call, events for the current thread may be processed.
   *
   * This method MAY NOT be executed from the thread itself.  Instead, it is
   * meant to be executed from another thread (usually the thread that created
   * this thread or the main application thread).  When this function returns,
   * the thread will be shutdown, and it will no longer be possible to dispatch
   * events to the thread.
   *
   * @throws NS_ERROR_UNEXPECTED
   *   Indicates that this method was erroneously called when this thread was
   *   the current thread, that this thread was not created with a call to
   *   nsIThreadManager::NewThread, or if this method was called more than once
   *   on the thread object.
   */
  void shutdown();

  /**
   * This method may be called to determine if there are any events ready to be
   * processed.  It may only be called when this thread is the current thread.
   *
   * Because events may be added to this thread by another thread, a "false"
   * result does not mean that this thread has no pending events.  It only
   * means that there were no pending events when this method was called.
   *
   * @returns
   *   A boolean value that if "true" indicates that this thread has one or
   *   more pending events.
   *
   * @throws NS_ERROR_UNEXPECTED
   *   Indicates that this method was erroneously called when this thread was
   *   not the current thread.
   */
  boolean hasPendingEvents();

  /**
   * Process the next event.  If there are no pending events, then this method
   * may wait -- depending on the value of the mayWait parameter -- until an
   * event is dispatched to this thread.  This method is re-entrant but may
   * only be called if this thread is the current thread.
   *
   * @param mayWait
   *   A boolean parameter that if "true" indicates that the method may block
   *   the calling thread to wait for a pending event.
   *
   * @returns
   *   A boolean value that if "true" indicates that an event was processed.
   *
   * @throws NS_ERROR_UNEXPECTED
   *   Indicates that this method was erroneously called when this thread was
   *   not the current thread.
   */
  boolean processNextEvent(boolean mayWait);

}