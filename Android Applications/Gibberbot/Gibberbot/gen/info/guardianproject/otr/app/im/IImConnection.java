/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/benjholla/Desktop/Android-Applications/Android Applications/Gibberbot/Gibberbot/src/info/guardianproject/otr/app/im/IImConnection.aidl
 */
package info.guardianproject.otr.app.im;
public interface IImConnection extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements info.guardianproject.otr.app.im.IImConnection
{
private static final java.lang.String DESCRIPTOR = "info.guardianproject.otr.app.im.IImConnection";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an info.guardianproject.otr.app.im.IImConnection interface,
 * generating a proxy if needed.
 */
public static info.guardianproject.otr.app.im.IImConnection asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof info.guardianproject.otr.app.im.IImConnection))) {
return ((info.guardianproject.otr.app.im.IImConnection)iin);
}
return new info.guardianproject.otr.app.im.IImConnection.Stub.Proxy(obj);
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
case TRANSACTION_registerConnectionListener:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IConnectionListener _arg0;
_arg0 = info.guardianproject.otr.app.im.IConnectionListener.Stub.asInterface(data.readStrongBinder());
this.registerConnectionListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unregisterConnectionListener:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IConnectionListener _arg0;
_arg0 = info.guardianproject.otr.app.im.IConnectionListener.Stub.asInterface(data.readStrongBinder());
this.unregisterConnectionListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_setInvitationListener:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IInvitationListener _arg0;
_arg0 = info.guardianproject.otr.app.im.IInvitationListener.Stub.asInterface(data.readStrongBinder());
this.setInvitationListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_getContactListManager:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IContactListManager _result = this.getContactListManager();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_getChatSessionManager:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IChatSessionManager _result = this.getChatSessionManager();
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_login:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _arg1;
_arg1 = (0!=data.readInt());
boolean _arg2;
_arg2 = (0!=data.readInt());
this.login(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
case TRANSACTION_logout:
{
data.enforceInterface(DESCRIPTOR);
this.logout();
reply.writeNoException();
return true;
}
case TRANSACTION_cancelLogin:
{
data.enforceInterface(DESCRIPTOR);
this.cancelLogin();
reply.writeNoException();
return true;
}
case TRANSACTION_getUserPresence:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.engine.Presence _result = this.getUserPresence();
reply.writeNoException();
if ((_result!=null)) {
reply.writeInt(1);
_result.writeToParcel(reply, android.os.Parcelable.PARCELABLE_WRITE_RETURN_VALUE);
}
else {
reply.writeInt(0);
}
return true;
}
case TRANSACTION_updateUserPresence:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.engine.Presence _arg0;
if ((0!=data.readInt())) {
_arg0 = info.guardianproject.otr.app.im.engine.Presence.CREATOR.createFromParcel(data);
}
else {
_arg0 = null;
}
int _result = this.updateUserPresence(_arg0);
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getSupportedPresenceStatus:
{
data.enforceInterface(DESCRIPTOR);
int[] _result = this.getSupportedPresenceStatus();
reply.writeNoException();
reply.writeIntArray(_result);
return true;
}
case TRANSACTION_getState:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getState();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getChatSessionCount:
{
data.enforceInterface(DESCRIPTOR);
int _result = this.getChatSessionCount();
reply.writeNoException();
reply.writeInt(_result);
return true;
}
case TRANSACTION_getProviderId:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getProviderId();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_getAccountId:
{
data.enforceInterface(DESCRIPTOR);
long _result = this.getAccountId();
reply.writeNoException();
reply.writeLong(_result);
return true;
}
case TRANSACTION_acceptInvitation:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
this.acceptInvitation(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_rejectInvitation:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
this.rejectInvitation(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_sendHeartbeat:
{
data.enforceInterface(DESCRIPTOR);
this.sendHeartbeat();
reply.writeNoException();
return true;
}
case TRANSACTION_setProxy:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
java.lang.String _arg1;
_arg1 = data.readString();
int _arg2;
_arg2 = data.readInt();
this.setProxy(_arg0, _arg1, _arg2);
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements info.guardianproject.otr.app.im.IImConnection
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
@Override public void registerConnectionListener(info.guardianproject.otr.app.im.IConnectionListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_registerConnectionListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void unregisterConnectionListener(info.guardianproject.otr.app.im.IConnectionListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_unregisterConnectionListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setInvitationListener(info.guardianproject.otr.app.im.IInvitationListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_setInvitationListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public info.guardianproject.otr.app.im.IContactListManager getContactListManager() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
info.guardianproject.otr.app.im.IContactListManager _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getContactListManager, _data, _reply, 0);
_reply.readException();
_result = info.guardianproject.otr.app.im.IContactListManager.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public info.guardianproject.otr.app.im.IChatSessionManager getChatSessionManager() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
info.guardianproject.otr.app.im.IChatSessionManager _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getChatSessionManager, _data, _reply, 0);
_reply.readException();
_result = info.guardianproject.otr.app.im.IChatSessionManager.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Login the IM server.
     *
     * @Param one time password not to be saved, for use if password is not persisted
     * @param autoLoadContacts if true, contacts will be loaded from the server
     *          automatically after the user successfully login; otherwise, the
     *          client must load contacts manually.
     */
@Override public void login(java.lang.String passwordTempt, boolean autoLoadContacts, boolean retry) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(passwordTempt);
_data.writeInt(((autoLoadContacts)?(1):(0)));
_data.writeInt(((retry)?(1):(0)));
mRemote.transact(Stub.TRANSACTION_login, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void logout() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_logout, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void cancelLogin() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_cancelLogin, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public info.guardianproject.otr.app.im.engine.Presence getUserPresence() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
info.guardianproject.otr.app.im.engine.Presence _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getUserPresence, _data, _reply, 0);
_reply.readException();
if ((0!=_reply.readInt())) {
_result = info.guardianproject.otr.app.im.engine.Presence.CREATOR.createFromParcel(_reply);
}
else {
_result = null;
}
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int updateUserPresence(info.guardianproject.otr.app.im.engine.Presence newPresence) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
if ((newPresence!=null)) {
_data.writeInt(1);
newPresence.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_updateUserPresence, _data, _reply, 0);
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
     * Gets an array of presence status which are supported by the IM provider.
     */
@Override public int[] getSupportedPresenceStatus() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int[] _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getSupportedPresenceStatus, _data, _reply, 0);
_reply.readException();
_result = _reply.createIntArray();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public int getState() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getState, _data, _reply, 0);
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
     * Gets the count of active ChatSessions of this connection.
     */
@Override public int getChatSessionCount() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
int _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getChatSessionCount, _data, _reply, 0);
_reply.readException();
_result = _reply.readInt();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public long getProviderId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getProviderId, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public long getAccountId() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
long _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAccountId, _data, _reply, 0);
_reply.readException();
_result = _reply.readLong();
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
@Override public void acceptInvitation(long id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(id);
mRemote.transact(Stub.TRANSACTION_acceptInvitation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void rejectInvitation(long id) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(id);
mRemote.transact(Stub.TRANSACTION_rejectInvitation, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void sendHeartbeat() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_sendHeartbeat, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
@Override public void setProxy(java.lang.String type, java.lang.String host, int port) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(type);
_data.writeString(host);
_data.writeInt(port);
mRemote.transact(Stub.TRANSACTION_setProxy, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_registerConnectionListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unregisterConnectionListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_setInvitationListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getContactListManager = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getChatSessionManager = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_login = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_logout = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_cancelLogin = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
static final int TRANSACTION_getUserPresence = (android.os.IBinder.FIRST_CALL_TRANSACTION + 8);
static final int TRANSACTION_updateUserPresence = (android.os.IBinder.FIRST_CALL_TRANSACTION + 9);
static final int TRANSACTION_getSupportedPresenceStatus = (android.os.IBinder.FIRST_CALL_TRANSACTION + 10);
static final int TRANSACTION_getState = (android.os.IBinder.FIRST_CALL_TRANSACTION + 11);
static final int TRANSACTION_getChatSessionCount = (android.os.IBinder.FIRST_CALL_TRANSACTION + 12);
static final int TRANSACTION_getProviderId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 13);
static final int TRANSACTION_getAccountId = (android.os.IBinder.FIRST_CALL_TRANSACTION + 14);
static final int TRANSACTION_acceptInvitation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 15);
static final int TRANSACTION_rejectInvitation = (android.os.IBinder.FIRST_CALL_TRANSACTION + 16);
static final int TRANSACTION_sendHeartbeat = (android.os.IBinder.FIRST_CALL_TRANSACTION + 17);
static final int TRANSACTION_setProxy = (android.os.IBinder.FIRST_CALL_TRANSACTION + 18);
}
public void registerConnectionListener(info.guardianproject.otr.app.im.IConnectionListener listener) throws android.os.RemoteException;
public void unregisterConnectionListener(info.guardianproject.otr.app.im.IConnectionListener listener) throws android.os.RemoteException;
public void setInvitationListener(info.guardianproject.otr.app.im.IInvitationListener listener) throws android.os.RemoteException;
public info.guardianproject.otr.app.im.IContactListManager getContactListManager() throws android.os.RemoteException;
public info.guardianproject.otr.app.im.IChatSessionManager getChatSessionManager() throws android.os.RemoteException;
/**
     * Login the IM server.
     *
     * @Param one time password not to be saved, for use if password is not persisted
     * @param autoLoadContacts if true, contacts will be loaded from the server
     *          automatically after the user successfully login; otherwise, the
     *          client must load contacts manually.
     */
public void login(java.lang.String passwordTempt, boolean autoLoadContacts, boolean retry) throws android.os.RemoteException;
public void logout() throws android.os.RemoteException;
public void cancelLogin() throws android.os.RemoteException;
public info.guardianproject.otr.app.im.engine.Presence getUserPresence() throws android.os.RemoteException;
public int updateUserPresence(info.guardianproject.otr.app.im.engine.Presence newPresence) throws android.os.RemoteException;
/**
     * Gets an array of presence status which are supported by the IM provider.
     */
public int[] getSupportedPresenceStatus() throws android.os.RemoteException;
public int getState() throws android.os.RemoteException;
/**
     * Gets the count of active ChatSessions of this connection.
     */
public int getChatSessionCount() throws android.os.RemoteException;
public long getProviderId() throws android.os.RemoteException;
public long getAccountId() throws android.os.RemoteException;
public void acceptInvitation(long id) throws android.os.RemoteException;
public void rejectInvitation(long id) throws android.os.RemoteException;
public void sendHeartbeat() throws android.os.RemoteException;
public void setProxy(java.lang.String type, java.lang.String host, int port) throws android.os.RemoteException;
}
