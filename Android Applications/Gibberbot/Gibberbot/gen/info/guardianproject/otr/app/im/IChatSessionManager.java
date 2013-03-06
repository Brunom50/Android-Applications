/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/benjholla/Desktop/Android-Applications/Android Applications/Gibberbot/Gibberbot/src/info/guardianproject/otr/app/im/IChatSessionManager.aidl
 */
package info.guardianproject.otr.app.im;
public interface IChatSessionManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements info.guardianproject.otr.app.im.IChatSessionManager
{
private static final java.lang.String DESCRIPTOR = "info.guardianproject.otr.app.im.IChatSessionManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an info.guardianproject.otr.app.im.IChatSessionManager interface,
 * generating a proxy if needed.
 */
public static info.guardianproject.otr.app.im.IChatSessionManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof info.guardianproject.otr.app.im.IChatSessionManager))) {
return ((info.guardianproject.otr.app.im.IChatSessionManager)iin);
}
return new info.guardianproject.otr.app.im.IChatSessionManager.Stub.Proxy(obj);
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
case TRANSACTION_registerChatSessionListener:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IChatSessionListener _arg0;
_arg0 = info.guardianproject.otr.app.im.IChatSessionListener.Stub.asInterface(data.readStrongBinder());
this.registerChatSessionListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterChatSessionListener:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IChatSessionListener _arg0;
_arg0 = info.guardianproject.otr.app.im.IChatSessionListener.Stub.asInterface(data.readStrongBinder());
this.unregisterChatSessionListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_createChatSession:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
info.guardianproject.otr.app.im.IChatSession _result = this.createChatSession(_arg0);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_getChatSession:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
info.guardianproject.otr.app.im.IChatSession _result = this.getChatSession(_arg0);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_getActiveChatSessions:
{
data.enforceInterface(DESCRIPTOR);
java.util.List _result = this.getActiveChatSessions();
reply.writeNoException();
reply.writeList(_result);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements info.guardianproject.otr.app.im.IChatSessionManager
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
@Override public void registerChatSessionListener(info.guardianproject.otr.app.im.IChatSessionListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerChatSessionListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterChatSessionListener(info.guardianproject.otr.app.im.IChatSessionListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterChatSessionListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Create a ChatSession with the specified contact. If the contact does not exist in any
     * of the user's contact lists, it will be added to the temporary list.
     *
     * @param contactAddress the address of the contact.
     */
@Override public info.guardianproject.otr.app.im.IChatSession createChatSession(java.lang.String contactAddress) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
info.guardianproject.otr.app.im.IChatSession _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(contactAddress);
mRemote.transact(Stub.TRANSACTION_createChatSession, _data, _reply, 0);
_reply.readException();
_result = info.guardianproject.otr.app.im.IChatSession.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Get the ChatSession that is associated with the specified contact or group.
     *
     * @param the address of the contact or group.
     * @return the ChatSession with the contact or group or <code>null</code> if
     *       there isn't any active ChatSession with the contact or group.
     */
@Override public info.guardianproject.otr.app.im.IChatSession getChatSession(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
info.guardianproject.otr.app.im.IChatSession _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_getChatSession, _data, _reply, 0);
_reply.readException();
_result = info.guardianproject.otr.app.im.IChatSession.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Get a list of all active ChatSessions.
     *
     * @return a list of IBinders of all active ChatSessions.
     */
@Override public java.util.List getActiveChatSessions() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getActiveChatSessions, _data, _reply, 0);
_reply.readException();
java.lang.ClassLoader cl = (java.lang.ClassLoader)this.getClass().getClassLoader();
_result = _reply.readArrayList(cl);
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
}
static final int TRANSACTION_registerChatSessionListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterChatSessionListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_createChatSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getChatSession = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getActiveChatSessions = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
}
public void registerChatSessionListener(info.guardianproject.otr.app.im.IChatSessionListener listener) throws android.os.RemoteException;
public void unregisterChatSessionListener(info.guardianproject.otr.app.im.IChatSessionListener listener) throws android.os.RemoteException;
/**
     * Create a ChatSession with the specified contact. If the contact does not exist in any
     * of the user's contact lists, it will be added to the temporary list.
     *
     * @param contactAddress the address of the contact.
     */
public info.guardianproject.otr.app.im.IChatSession createChatSession(java.lang.String contactAddress) throws android.os.RemoteException;
/**
     * Get the ChatSession that is associated with the specified contact or group.
     *
     * @param the address of the contact or group.
     * @return the ChatSession with the contact or group or <code>null</code> if
     *       there isn't any active ChatSession with the contact or group.
     */
public info.guardianproject.otr.app.im.IChatSession getChatSession(java.lang.String address) throws android.os.RemoteException;
/**
     * Get a list of all active ChatSessions.
     *
     * @return a list of IBinders of all active ChatSessions.
     */
public java.util.List getActiveChatSessions() throws android.os.RemoteException;
}
