/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/benjholla/Desktop/Android-Applications/Android Applications/Gibberbot/Gibberbot/src/info/guardianproject/otr/IOtrChatSession.aidl
 */
package info.guardianproject.otr;
public interface IOtrChatSession extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements info.guardianproject.otr.IOtrChatSession
{
private static final java.lang.String DESCRIPTOR = "info.guardianproject.otr.IOtrChatSession";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an info.guardianproject.otr.IOtrChatSession interface,
 * generating a proxy if needed.
 */
public static info.guardianproject.otr.IOtrChatSession asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof info.guardianproject.otr.IOtrChatSession))) {
return ((info.guardianproject.otr.IOtrChatSession)iin);
}
return new info.guardianproject.otr.IOtrChatSession.Stub.Proxy(obj);
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
case TRANSACTION_startChatEncryption:
{
data.enforceInterface(DESCRIPTOR);
this.startChatEncryption();
reply.writeNoException();
return true;
}
case TRANSACTION_stopChatEncryption:
{
data.enforceInterface(DESCRIPTOR);
this.stopChatEncryption();
reply.writeNoException();
return true;
}
case TRANSACTION_isChatEncrypted:
{
data.enforceInterface(DESCRIPTOR);
boolean _result = this.isChatEncrypted();
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getChatStatus:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getChatStatus();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_initSmpVerification:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
this.initSmpVerification(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_respondSmpVerification:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.respondSmpVerification(_arg0);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements info.guardianproject.otr.IOtrChatSession
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
     * Start the OTR encryption on this chat session.
     */
@Override public void startChatEncryption() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_startChatEncryption, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Stop the OTR encryption on this chat session.
     */
@Override public void stopChatEncryption() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_stopChatEncryption, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Tells if the chat session has OTR encryption running.
     */
@Override public boolean isChatEncrypted() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_isChatEncrypted, _data, _reply, 0);
_reply.readException();
_result = (0!=_reply.readInt());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/** OTR session status - ordinal of SessionStatus */
@Override public int getChatStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getChatStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * start the SMP verification process
     */
@Override public void initSmpVerification(java.lang.String question, java.lang.String answer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(question);
_data.writeString(answer);
mRemote.transact(Stub.TRANSACTION_initSmpVerification, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * respond to the SMP verification process
     */
@Override public void respondSmpVerification(java.lang.String answer) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(answer);
mRemote.transact(Stub.TRANSACTION_respondSmpVerification, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_startChatEncryption = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_stopChatEncryption = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isChatEncrypted = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getChatStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_initSmpVerification = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_respondSmpVerification = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
/**
     * Start the OTR encryption on this chat session.
     */
public void startChatEncryption() throws android.os.RemoteException;
/**
     * Stop the OTR encryption on this chat session.
     */
public void stopChatEncryption() throws android.os.RemoteException;
/**
     * Tells if the chat session has OTR encryption running.
     */
public boolean isChatEncrypted() throws android.os.RemoteException;
/** OTR session status - ordinal of SessionStatus */
public int getChatStatus() throws android.os.RemoteException;
/**
     * start the SMP verification process
     */
public void initSmpVerification(java.lang.String question, java.lang.String answer) throws android.os.RemoteException;
/**
     * respond to the SMP verification process
     */
public void respondSmpVerification(java.lang.String answer) throws android.os.RemoteException;
}
