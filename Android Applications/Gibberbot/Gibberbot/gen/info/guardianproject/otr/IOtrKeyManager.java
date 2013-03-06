/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/benjholla/Desktop/Android-Applications/Android Applications/Gibberbot/Gibberbot/src/info/guardianproject/otr/IOtrKeyManager.aidl
 */
package info.guardianproject.otr;
public interface IOtrKeyManager extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements info.guardianproject.otr.IOtrKeyManager
{
private static final java.lang.String DESCRIPTOR = "info.guardianproject.otr.IOtrKeyManager";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an info.guardianproject.otr.IOtrKeyManager interface,
 * generating a proxy if needed.
 */
public static info.guardianproject.otr.IOtrKeyManager asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof info.guardianproject.otr.IOtrKeyManager))) {
return ((info.guardianproject.otr.IOtrKeyManager)iin);
}
return new info.guardianproject.otr.IOtrKeyManager.Stub.Proxy(obj);
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
case TRANSACTION_verifyKey:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.verifyKey(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_unverifyKey:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
this.unverifyKey(_arg0);
reply.writeNoException();
return true;
}
case TRANSACTION_isKeyVerified:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _arg0;
_arg0 = data.readString();
boolean _result = this.isKeyVerified(_arg0);
reply.writeNoException();
reply.writeInt(((_result)?(1):(0)));
return true;
}
case TRANSACTION_getLocalFingerprint:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getLocalFingerprint();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_getRemoteFingerprint:
{
data.enforceInterface(DESCRIPTOR);
java.lang.String _result = this.getRemoteFingerprint();
reply.writeNoException();
reply.writeString(_result);
return true;
}
case TRANSACTION_generateLocalKeyPair:
{
data.enforceInterface(DESCRIPTOR);
this.generateLocalKeyPair();
reply.writeNoException();
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements info.guardianproject.otr.IOtrKeyManager
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
     * Verify the key for a given address.
     */
@Override public void verifyKey(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_verifyKey, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Revoke the verification for the key for a given address.
     */
@Override public void unverifyKey(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_unverifyKey, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
/**
     * Tells if the fingerprint of the remote user address has been verified.
     */
@Override public boolean isKeyVerified(java.lang.String address) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
boolean _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeString(address);
mRemote.transact(Stub.TRANSACTION_isKeyVerified, _data, _reply, 0);
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
     * Returns the fingerprint for the local user's key for a given account address.
     */
@Override public java.lang.String getLocalFingerprint() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getLocalFingerprint, _data, _reply, 0);
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
     * Returns the fingerprint for a remote user's key for a given account address.
     */
@Override public java.lang.String getRemoteFingerprint() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
java.lang.String _result;
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_getRemoteFingerprint, _data, _reply, 0);
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
     * generate a new local private/public key pair.
     */
@Override public void generateLocalKeyPair() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
android.os.Parcel _reply = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_generateLocalKeyPair, _data, _reply, 0);
_reply.readException();
}
finally {
_reply.recycle();
_data.recycle();
}
}
}
static final int TRANSACTION_verifyKey = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_unverifyKey = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_isKeyVerified = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_getLocalFingerprint = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
static final int TRANSACTION_getRemoteFingerprint = (android.os.IBinder.FIRST_CALL_TRANSACTION + 4);
static final int TRANSACTION_generateLocalKeyPair = (android.os.IBinder.FIRST_CALL_TRANSACTION + 5);
}
/**
     * Verify the key for a given address.
     */
public void verifyKey(java.lang.String address) throws android.os.RemoteException;
/**
     * Revoke the verification for the key for a given address.
     */
public void unverifyKey(java.lang.String address) throws android.os.RemoteException;
/**
     * Tells if the fingerprint of the remote user address has been verified.
     */
public boolean isKeyVerified(java.lang.String address) throws android.os.RemoteException;
/**
     * Returns the fingerprint for the local user's key for a given account address.
     */
public java.lang.String getLocalFingerprint() throws android.os.RemoteException;
/**
     * Returns the fingerprint for a remote user's key for a given account address.
     */
public java.lang.String getRemoteFingerprint() throws android.os.RemoteException;
/**
     * generate a new local private/public key pair.
     */
public void generateLocalKeyPair() throws android.os.RemoteException;
}
