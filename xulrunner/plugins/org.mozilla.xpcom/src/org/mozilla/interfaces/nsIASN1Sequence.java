/*
 * DO NOT EDIT.  THIS FILE IS GENERATED FROM
 * /builds/slave/mozilla-1.9.2-linux-xulrunner/build/security/manager/ssl/public/nsIASN1Sequence.idl
 */

package org.mozilla.interfaces;

/**
 * This represents a sequence of ASN.1 objects,
 * where ASN.1 is "Abstract Syntax Notation number One".
 *
 * Overview of how this ASN1 interface is intended to
 * work.
 *
 * First off, the nsIASN1Sequence is any type in ASN1
 * that consists of sub-elements (ie SEQUENCE, SET)
 * nsIASN1Printable Items are all the other types that
 * can be viewed by themselves without interpreting further.
 * Examples would include INTEGER, UTF-8 STRING, OID.
 * These are not intended to directly reflect the numberous
 * types that exist in ASN1, but merely an interface to ease
 * producing a tree display the ASN1 structure of any DER
 * object.
 *
 * The additional state information carried in this interface
 * makes it fit for being used as the data structure
 * when working with visual reprenstation of ASN.1 objects
 * in a human user interface, like in a tree widget
 * where open/close state of nodes must be remembered.
 *
 * @status FROZEN
 */
public interface nsIASN1Sequence extends nsIASN1Object {

  String NS_IASN1SEQUENCE_IID =
    "{b6b957e6-1dd1-11b2-89d7-e30624f50b00}";

  /**
   *  The array of objects stored in the sequence.
   */
  nsIMutableArray getASN1Objects();

  /**
   *  The array of objects stored in the sequence.
   */
  void setASN1Objects(nsIMutableArray aASN1Objects);

  /**
   *  Whether the node at this position in the ASN.1 data structure
   *  sequence contains sub elements understood by the
   *  application.
   */
  boolean getIsValidContainer();

  /**
   *  Whether the node at this position in the ASN.1 data structure
   *  sequence contains sub elements understood by the
   *  application.
   */
  void setIsValidContainer(boolean aIsValidContainer);

  /**
   *  Whether the contained objects should be shown or hidden.
   *  A UI implementation can use this flag to store the current
   *  expansion state when shown in a tree widget.
   */
  boolean getIsExpanded();

  /**
   *  Whether the contained objects should be shown or hidden.
   *  A UI implementation can use this flag to store the current
   *  expansion state when shown in a tree widget.
   */
  void setIsExpanded(boolean aIsExpanded);

}