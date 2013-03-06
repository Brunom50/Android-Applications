/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/benjholla/Desktop/Android-Applications/Android Applications/Gibberbot/Gibberbot/src/info/guardianproject/otr/app/im/IRemoteImService.aidl
 */
package info.guardianproject.otr.app.im;
public interface IRemoteImService extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements info.guardianproject.otr.app.im.IRemoteImService
{
private static final java.lang.String DESCRIPTOR = "info.guardianproject.otr.app.im.IRemoteImService";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an info.guardianproject.otr.app.im.IRemoteImService interface,
 * generating a proxy if needed.
 */
public static info.guardianproject.otr.app.im.IRemoteImService asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof info.guardianproject.otr.app.im.IRemoteImService))) {
return ((info.guardianproject.otr.app.im.IRemoteImService)iin);
}
return new info.guardianproject.otr.app.im.IRemoteImService.Stub.Proxy(obj);
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
case TRANSACTION_getAllPlugins:
{
data.enforceInterface(DESCRIPTOR);
java.util.List _result = this.getAllPlugins();
reply.writeNoException();
reply.writeList(_result);
return true;
}
case TRANSACTION_addConnectionCreatedListener:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IConnectionCreationListener _arg0;
_arg0 = info.guardianproject.otr.app.im.IConnectionCreationListener.Stub.asInterface(data.readStrongBinder());
this.addConnectionCreatedListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_removeConnectionCreatedListener:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.IConnectionCreationListener _arg0;
_arg0 = info.guardianproject.otr.app.im.IConnectionCreationListener.Stub.asInterface(data.readStrongBinder());
this.removeConnectionCreatedListener(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_createConnection:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
long _arg1;
_arg1 = data.readLong();
info.guardianproject.otr.app.im.IImConnection _result = this.createConnection(_arg0, _arg1);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
case TRANSACTION_getActiveConnections:
{
data.enforceInterface(DESCRIPTOR);
java.util.List _result = this.getActiveConnections();
reply.writeNoException();
reply.writeList(_result);
return true;
}
case TRANSACTION_dismissNotifications:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
this.dismissNotifications(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_dismissChatNotification:
{
data.enforceInterface(DESCRIPTOR);
long _arg0;
_arg0 = data.readLong();
java.lang.String _arg1;
_arg1 = data.readString();
this.dismissChatNotification(_arg0, _arg1);
reply.writeNoException();
return true;
}
case TRANSACTION_getOtrKeyManager:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
info.guardianproject.otr.IOtrKeyManager _result = this.getOtrKeyManager(_arg0);
reply.writeNoException();
reply.writeStrongBinder((((_result!=null))?(_result.asBinder()):(null)));
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements info.guardianproject.otr.app.im.IRemoteImService
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
     * Gets a list of all installed plug-ins. Each item is an ImPluginInfo.
     */
@Override public java.util.List getAllPlugins() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getAllPlugins, _data, _reply, 0);
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
/**
     * Register a listener on the service so that the client can be notified when
     * there is a connection be created.
     */
@Override public void addConnectionCreatedListener(info.guardianproject.otr.app.im.IConnectionCreationListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_addConnectionCreatedListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Unregister the listener on the service so that the client doesn't ware of
     * the connection creation anymore.
     */
@Override public void removeConnectionCreatedListener(info.guardianproject.otr.app.im.IConnectionCreationListener listener) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeStrongBinder((((listener!=null))?(listener.asBinder()):(null)));
mRemote.transact(Stub.TRANSACTION_removeConnectionCreatedListener, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Create a connection for the given provider.
     */
@Override public info.guardianproject.otr.app.im.IImConnection createConnection(long providerId, long accountId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
info.guardianproject.otr.app.im.IImConnection _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(providerId);
_data.writeLong(accountId);
mRemote.transact(Stub.TRANSACTION_createConnection, _data, _reply, 0);
_reply.readException();
_result = info.guardianproject.otr.app.im.IImConnection.Stub.asInterface(_reply.readStrongBinder());
}
finally {
_reply.recycle();
_data.recycle();
}
return _result;
}
/**
     * Get all the active connections.
     */
@Override public java.util.List getActiveConnections() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.util.List _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getActiveConnections, _data, _reply, 0);
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
/**
     * Dismiss all notifications for an IM provider.
     */
@Override public void dismissNotifications(long providerId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(providerId);
mRemote.transact(Stub.TRANSACTION_dismissNotifications, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Dismiss notification for the specified chat.
     */
@Override public void dismissChatNotification(long providerId, java.lang.String username) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeLong(providerId);
_data.writeString(username);
mRemote.transact(Stub.TRANSACTION_dismissChatNotification, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
    * Get OTR Key Manager
    */
@Override public info.guardianproject.otr.IOtrKeyManager getOtrKeyManager(java.lang.String accountId) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
info.guardianproject.otr.IOtrKeyManager _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(accountId);
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
static final int TRANSACTION_getAllPlugins = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_addConnectionCreatedListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_removeConnectionCreatedListener = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_createConnection = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getActiveConnections = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_dismissNotifications = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
static final int TRANSACTION_dismissChatNotification = (android.os.IBinder.FIRST_CALL_TRANSACTION + 6);
static final int TRANSACTION_getOtrKeyManager = (android.os.IBinder.FIRST_CALL_TRANSACTION + 7);
}
/**
     * Gets a list of all installed plug-ins. Each item is an ImPluginInfo.
     */
public java.util.List getAllPlugins() throws android.os.RemoteException;
/**
     * Register a listener on the service so that the client can be notified when
     * there is a connection be created.
     */
public void addConnectionCreatedListener(info.guardianproject.otr.app.im.IConnectionCreationListener listener) throws android.os.RemoteException;
/**
     * Unregister the listener on the service so that the client doesn't ware of
     * the connection creation anymore.
     */
public void removeConnectionCreatedListener(info.guardianproject.otr.app.im.IConnectionCreationListener listener) throws android.os.RemoteException;
/**
     * Create a connection for the given provider.
     */
public info.guardianproject.otr.app.im.IImConnection createConnection(long providerId, long accountId) throws android.os.RemoteException;
/**
     * Get all the active connections.
     */
public java.util.List getActiveConnections() throws android.os.RemoteException;
/**
     * Dismiss all notifications for an IM provider.
     */
public void dismissNotifications(long providerId) throws android.os.RemoteException;
/**
     * Dismiss notification for the specified chat.
     */
public void dismissChatNotification(long providerId, java.lang.String username) throws android.os.RemoteException;
/**
    * Get OTR Key Manager
    */
public info.guardianproject.otr.IOtrKeyManager getOtrKeyManager(java.lang.String accountId) throws android.os.RemoteException;
}
