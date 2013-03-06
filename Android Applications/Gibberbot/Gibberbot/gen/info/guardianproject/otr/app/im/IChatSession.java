/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/benjholla/Desktop/Android-Applications/Android Applications/Gibberbot/Gibberbot/src/info/guardianproject/otr/app/im/IChatSession.aidl
 */
package info.guardianproject.otr.app.im;
public interface IChatSession extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements info.guardianproject.otr.app.im.IChatSession
{
private static final java.lang.String DESCRIPTOR = "info.guardianproject.otr.app.im.IChatSession";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an info.guardianproject.otr.app.im.IChatSession interface,
 * generating a proxy if needed.
 */
public static info.guardianproject.otr.app.im.IChatSession asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof info.guardianproject.otr.app.im.IChatSession))) {
return ((info.guardianproject.otr.app.im.IChatSession)iin);
}
return new info.guardianproject.otr.app.im.IChatSession.Stub.Proxy(obj);
}
@Override public android.os.IBinder asBinder()
{
return this;
}
@Override public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException
{
switch (code)
{
case INTERFACE_TRANSACTION:
{
reply.writeString(DESCRIPTOR);
return true;
}
case TRANSACTION_registerChatListener:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IChatListener _arg0;
_arg0 = info.guardianproject.otr.app.im.IChatListener.Stub.asInterface(data.readStrongBinder());
this.registerChatListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterChatListener:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IChatListener _arg0;
_arg0 = info.guardianproject.otr.app.im.IChatListener.Stub.asInterface(data.readStrongBinder());
this.unregisterChatListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isGroupChatSession:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isGroupChatSession();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getName:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getName();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getId:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getId();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_getPariticipants:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String[] _result = this.getPariticipants();
reply.writeNoException();
reply.writeStringArray(_result);
return true;
}
case TRANSACTION_convertToGroupChat:
{
data.enforceInterface(DESCRIPTOR);
this.convertToGroupChat();
reply.writeNoException();
return true;
}
case TRANSACTION_inviteContact:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.inviteContact(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_leave:
{
data.enforceInterface(DESCRIPTOR);
this.leave();
reply.writeNoException();
return true;
}
case TRANSACTION_leaveIfInactive:
{
data.enforceInterface(DESCRIPTOR);
this.leaveIfInactive();
reply.writeNoException();
return true;
}
case TRANSACTION_sendMessage:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.sendMessage(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_markAsRead:
{
data.enforceInterface(DESCRIPTOR);
this.markAsRead();
reply.writeNoException();
return true;
}
case TRANSACTION_getOtrChatSession:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.IOtrChatSession _result = this.getOtrChatSession();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_getOtrKeyManager:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.IOtrKeyManager _result = this.getOtrKeyManager();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements info.guardianproject.otr.app.im.IChatSession
{
private android.os.IBinder mRemote;
Proxy(android.os.IBinder remote)
{
mRemote = remote;
}
@Override public android.os.IBinder asBinder()
{
return mRemote;
}
public java.lang.String getInterfaceDescriptor()
{
return DESCRIPTOR;
}
/**
     * Registers a ChatListener with this ChatSession to listen to incoming
     * message and participant change events.
     */
@Override public void registerChatListener(info.guardianproject.otr.app.im.IChatListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerChatListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Unregisters the ChatListener so that it won't be notified again.
     */
@Override public void unregisterChatListener(info.guardianproject.otr.app.im.IChatListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterChatListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Tells if this ChatSession is a group session.
     */
@Override public boolean isGroupChatSession() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isGroupChatSession, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Gets the name of ChatSession.
     */
@Override public java.lang.String getName() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getName, _data, _reply, 0);
_reply.readException();
_result = _reply.readString();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Gets the id of the ChatSession in content provider.
     */
@Override public long getId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getId, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Gets the participants of this ChatSession.
     */
@Override public java.lang.String[] getPariticipants() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getPariticipants, _data, _reply, 0);
_reply.readException();
_result = _reply.createStringArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Convert a single chat to a group chat. If the chat session is already a
     * group chat or it's converting to group chat.
     */
@Override public void convertToGroupChat() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_convertToGroupChat, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Invites a contact to join this ChatSession. The user can only invite
     * contacts to join this ChatSession if it's a group session. Nothing will
     * happen if this is a simple one-to-one ChatSession.
     */
@Override public void inviteContact(java.lang.String contact) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(contact);
mRemote.transact(Stub.TRANSACTION_inviteContact, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Leaves this ChatSession.
     */
@Override public void leave() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_leave, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Leaves this ChatSession if there isn't any message sent or received in it.
     */
@Override public void leaveIfInactive() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_leaveIfInactive, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Sends a message to all participants in this ChatSession.
     */
@Override public void sendMessage(java.lang.String text) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(text);
mRemote.transact(Stub.TRANSACTION_sendMessage, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Mark this chat session as read.
     */
@Override public void markAsRead() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_markAsRead, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
    * Get OTR Session Manager
    */
@Override public info.guardianproject.otr.IOtrChatSession getOtrChatSession() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
info.guardianproject.otr.IOtrChatSession _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getOtrChatSession, _data, _reply, 0);
_reply.readException();
_result = info.guardianproject.otr.IOtrChatSession.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
    * Get OTR Key Manager
    */
@Override public info.guardianproject.otr.IOtrKeyManager getOtrKeyManager() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
info.guardianproject.otr.IOtrKeyManager _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getOtrKeyManager, _data, _reply, 0);
_reply.readException();
_result = info.guardianproject.otr.IOtrKeyManager.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_registerChatListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterChatListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isGroupChatSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getName = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_getPariticipants = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_convertToGroupChat = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_inviteContact = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_leave = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_leaveIfInactive = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_sendMessage = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_markAsRead = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getOtrChatSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getOtrKeyManager = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
}
/**
     * Registers a ChatListener with this ChatSession to listen to incoming
     * message and participant change events.
     */
public void registerChatListener(info.guardianproject.otr.app.im.IChatListener listener) throws android.os.RemoteException;
/**
     * Unregisters the ChatListener so that it won't be notified again.
     */
public void unregisterChatListener(info.guardianproject.otr.app.im.IChatListener listener) throws android.os.RemoteException;
/**
     * Tells if this ChatSession is a group session.
     */
public boolean isGroupChatSession() throws android.os.RemoteException;
/**
     * Gets the name of ChatSession.
     */
public java.lang.String getName() throws android.os.RemoteException;
/**
     * Gets the id of the ChatSession in content provider.
     */
public long getId() throws android.os.RemoteException;
/**
     * Gets the participants of this ChatSession.
     */
public java.lang.String[] getPariticipants() throws android.os.RemoteException;
/**
     * Convert a single chat to a group chat. If the chat session is already a
     * group chat or it's converting to group chat.
     */
public void convertToGroupChat() throws android.os.RemoteException;
/**
     * Invites a contact to join this ChatSession. The user can only invite
     * contacts to join this ChatSession if it's a group session. Nothing will
     * happen if this is a simple one-to-one ChatSession.
     */
public void inviteContact(java.lang.String contact) throws android.os.RemoteException;
/**
     * Leaves this ChatSession.
     */
public void leave() throws android.os.RemoteException;
/**
     * Leaves this ChatSession if there isn't any message sent or received in it.
     */
public void leaveIfInactive() throws android.os.RemoteException;
/**
     * Sends a message to all participants in this ChatSession.
     */
public void sendMessage(java.lang.String text) throws android.os.RemoteException;
/**
     * Mark this chat session as read.
     */
public void markAsRead() throws android.os.RemoteException;
/**
    * Get OTR Session Manager
    */
public info.guardianproject.otr.IOtrChatSession getOtrChatSession() throws android.os.RemoteException;
/**
    * Get OTR Key Manager
    */
public info.guardianproject.otr.IOtrKeyManager getOtrKeyManager() throws android.os.RemoteException;
}
