/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/content/base/public/nsIXMLHttpRequest.idl
 */

package org.mozilla.interfaces;

/**
 * Mozilla's XMLHttpRequest is modelled after Microsoft's IXMLHttpRequest
 * object. The goal has been to make Mozilla's version match Microsoft's
 * version as closely as possible, but there are bound to be some differences.
 *
 * In general, Microsoft's documentation for IXMLHttpRequest can be used.
 * Mozilla's interface definitions provide some additional documentation. The
 * web page to look at is http://www.mozilla.org/xmlextras/
 *
 * Mozilla's XMLHttpRequest object can be created in JavaScript like this:
 *   new XMLHttpRequest()
 * compare to Internet Explorer:
 *   new ActiveXObject("Msxml2.XMLHTTP")
 *
 * From JavaScript, the methods and properties visible in the XMLHttpRequest
 * object are a combination of nsIXMLHttpRequest and nsIJSXMLHttpRequest;
 * there is no need to differentiate between those interfaces.
 *
 * From native code, the way to set up onload and onerror handlers is a bit
 * different. Here is a comment from Johnny Stenback <jst@netscape.com>:
 *
 *   The mozilla implementation of nsIXMLHttpRequest implements the interface
 *   nsIDOMEventTarget and that's how you're supported to add event listeners.
 *   Try something like this:
 *
 *   nsCOMPtr<nsIDOMEventTarget> target(do_QueryInterface(myxmlhttpreq));
 *
 *   target->AddEventListener(NS_LITERAL_STRING("load"), mylistener,
 *                            PR_FALSE)
 *
 *   where mylistener is your event listener object that implements the
 *   interface nsIDOMEventListener.
 *
 *   The 'onload', 'onerror', and 'onreadystatechange' attributes moved to
 *   nsIJSXMLHttpRequest, but if you're coding in C++ you should avoid using
 *   those.
 *
 * Conclusion: Do not use event listeners on XMLHttpRequest from C++, unless
 * you're aware of all the security implications.  And then think twice about
 * it.
 */
public interface nsIXMLHttpRequest extends nsISupports {

  String NS_IXMLHTTPREQUEST_IID =
    "{ad78bf21-2227-447e-8ed5-824a017c265f}";

  /**
   * The request uses a channel in order to perform the
   * request.  This attribute represents the channel used
   * for the request.  NULL if the channel has not yet been
   * created.
   *
   * In a multipart request case, this is the initial channel, not the
   * different parts in the multipart request.
   *
   * Mozilla only. Requires elevated privileges to access.
   */
  nsIChannel getChannel();

  /**
   * The response to the request is parsed as if it were a
   * text/xml stream. This attributes represents the response as
   * a DOM Document object. NULL if the request is unsuccessful or
   * has not yet been sent.
   */
  nsIDOMDocument getResponseXML();

  /**
   * The response to the request as text.
   * NULL if the request is unsuccessful or
   * has not yet been sent.
   */
  String getResponseText();

  /**
   * The status of the response to the request for HTTP requests.
   */
  long getStatus();

  /**
   * The string representing the status of the response for
   * HTTP requests.
   */
  String getStatusText();

  /**
   * If the request has been sent already, this method will
   * abort the request.
   */
  void abort();

  /**
   * Returns all of the response headers as a string for HTTP
   * requests.
   *
   * Note that this will return all the headers from the *current*
   * part of a multipart request, not from the original channel.
   *
   * @returns A string containing all of the response headers.
   *          NULL if the response has not yet been received.
   */
  String getAllResponseHeaders();

  /**
   * Returns the text of the header with the specified name for
   * HTTP requests.
   *
   * @param header The name of the header to retrieve
   * @returns A string containing the text of the header specified.
   *          NULL if the response has not yet been received or the
   *          header does not exist in the response.
   */
  String getResponseHeader(String header);

  /**
   * Meant to be a script-only method for initializing a request.
   * The parameters are similar to the ones detailed in the
   * description of <code>openRequest</code>, but the last
   * 3 are optional.
   *
   * If there is an "active" request (that is, if open() or openRequest() has
   * been called already), this is equivalent to calling abort().
   *
   * @param method The HTTP method - either "POST" or "GET". Ignored
   *               if the URL is not a HTTP URL.
   * @param url The URL to which to send the request.
   * @param async (optional) Whether the request is synchronous or
   *              asynchronous i.e. whether send returns only after
   *              the response is received or if it returns immediately after
   *              sending the request. In the latter case, notification
   *              of completion is sent through the event listeners.
   *              The default value is true.
   *              This argument must be true if the multipart
   *              attribute has been set to true, or an exception will
   *              be thrown.
   * @param user (optional) A username for authentication if necessary.
   *             The default value is the empty string
   * @param password (optional) A password for authentication if necessary.
   *                 The default value is the empty string
   */
  void open(String method, String url);

  /**
   * Sends the request. If the request is asynchronous, returns
   * immediately after sending the request. If it is synchronous
   * returns only after the response has been received.
   *
   * All event listeners must be set before calling send().
   *
   * After the initial response, all event listeners will be cleared.
   * // XXXbz what does that mean, exactly?   
   *
   * @param body Either an instance of nsIDOMDocument, nsIInputStream
   *             or a string (nsISupportsString in the native calling
   *             case). This is used to populate the body of the
   *             HTTP request if the HTTP request method is "POST".
   *             If the parameter is a nsIDOMDocument, it is serialized.
   *             If the parameter is a nsIInputStream, then it must be
   *             compatible with nsIUploadChannel.setUploadStream, and a
   *             Content-Length header will be added to the HTTP request
   *             with a value given by nsIInputStream.available.  Any
   *             headers included at the top of the stream will be
   *             treated as part of the message body.  The MIME type of
   *             the stream should be specified by setting the Content-
   *             Type header via the setRequestHeader method before
   *             calling send.
   */
  void send(nsIVariant body);

  /**
   * A variant of the send() method used to send binary data.
   *
   * @param body The request body as a DOM string.  The string data will be
   *             converted to a single-byte string by truncation (i.e., the
   *             high-order byte of each character will be discarded).
   */
  void sendAsBinary(String body);

  /**
   * Sets a HTTP request header for HTTP requests. You must call open
   * before setting the request headers.
   *
   * @param header The name of the header to set in the request.
   * @param value The body of the header.
   */
  void setRequestHeader(String header, String value);

  /**
   * The state of the request.
   *
   * Possible values:
   *   0 UNINITIALIZED open() has not been called yet.
   *   1 LOADING       send() has not been called yet.
   *   2 LOADED        send() has been called, headers and status are available.
   *   3 INTERACTIVE   Downloading, responseText holds the partial data.
   *   4 COMPLETED     Finished with all operations.
   */
  int getReadyState();

  /**
   * Override the mime type returned by the server (if any). This may
   * be used, for example, to force a stream to be treated and parsed
   * as text/xml, even if the server does not report it as such. This
   * must be done before the <code>send</code> method is invoked.
   *
   * @param mimetype The type used to override that returned by the server
   *                 (if any).
   */
  void overrideMimeType(String mimetype);

  /**
   * Set to true if the response is expected to be a stream of
   * possibly multiple (XML) documents. If set to true, the content
   * type of the initial response must be multipart/x-mixed-replace or
   * an error will be triggerd. All requests must be asynchronous.
   *
   * This enables server push. For each XML document that's written to
   * this request, a new XML DOM document is created and the onload
   * handler is called inbetween documents. Note that when this is
   * set, the onload handler and other event handlers are not reset
   * after the first XML document is loaded, and the onload handler
   * will be called as each part of the response is received.
   */
  boolean getMultipart();

  /**
   * Set to true if the response is expected to be a stream of
   * possibly multiple (XML) documents. If set to true, the content
   * type of the initial response must be multipart/x-mixed-replace or
   * an error will be triggerd. All requests must be asynchronous.
   *
   * This enables server push. For each XML document that's written to
   * this request, a new XML DOM document is created and the onload
   * handler is called inbetween documents. Note that when this is
   * set, the onload handler and other event handlers are not reset
   * after the first XML document is loaded, and the onload handler
   * will be called as each part of the response is received.
   */
  void setMultipart(boolean aMultipart);

  /**
   * Set to true if this is a background service request. This will
   * prevent a load group being associated with the request, and
   * suppress any security dialogs from being shown * to the user.
   * In the cases where one of those dialogs would be shown, the request
   * will simply fail instead.
   */
  boolean getMozBackgroundRequest();

  /**
   * Set to true if this is a background service request. This will
   * prevent a load group being associated with the request, and
   * suppress any security dialogs from being shown * to the user.
   * In the cases where one of those dialogs would be shown, the request
   * will simply fail instead.
   */
  void setMozBackgroundRequest(boolean aMozBackgroundRequest);

  /**
   * When set to true attempts to make cross-site Access-Control requests
   * with credentials such as cookies and authorization headers.
   *
   * Never affects same-site requests.
   *
   * Defaults to false.
   */
  boolean getWithCredentials();

  /**
   * When set to true attempts to make cross-site Access-Control requests
   * with credentials such as cookies and authorization headers.
   *
   * Never affects same-site requests.
   *
   * Defaults to false.
   */
  void setWithCredentials(boolean aWithCredentials);

  /**
   * Upload process can be tracked by adding event listener to |upload|.
   */
  nsIXMLHttpRequestUpload getUpload();

  /**
   * Meant to be a script-only mechanism for setting a callback function.
   * The attribute is expected to be JavaScript function object. When the
   * readyState changes, the callback function will be called.
   * This attribute should not be used from native code!!
   *
   * After the initial response, all event listeners will be cleared.
   * // XXXbz what does that mean, exactly?   
   *
   * Call open() before setting an onreadystatechange listener.
   */
  nsIDOMEventListener getOnreadystatechange();

  /**
   * Meant to be a script-only mechanism for setting a callback function.
   * The attribute is expected to be JavaScript function object. When the
   * readyState changes, the callback function will be called.
   * This attribute should not be used from native code!!
   *
   * After the initial response, all event listeners will be cleared.
   * // XXXbz what does that mean, exactly?   
   *
   * Call open() before setting an onreadystatechange listener.
   */
  void setOnreadystatechange(nsIDOMEventListener aOnreadystatechange);

}