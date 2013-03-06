/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/benjholla/Desktop/Android-Applications/Android Applications/Gibberbot/Gibberbot/src/info/guardianproject/otr/app/im/IContactListListener.aidl
 */
package info.guardianproject.otr.app.im;
public interface IContactListListener extends android.os.IInterface
{
/** Local-side IPC implementation stub class. */
public static abstract class Stub extends android.os.Binder implements info.guardianproject.otr.app.im.IContactListListener
{
private static final java.lang.String DESCRIPTOR = "info.guardianproject.otr.app.im.IContactListListener";
/** Construct the stub at attach it to the interface. */
public Stub()
{
this.attachInterface(this, DESCRIPTOR);
}
/**
 * Cast an IBinder object into an info.guardianproject.otr.app.im.IContactListListener interface,
 * generating a proxy if needed.
 */
public static info.guardianproject.otr.app.im.IContactListListener asInterface(android.os.IBinder obj)
{
if ((obj==null)) {
return null;
}
android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
if (((iin!=null)&&(iin instanceof info.guardianproject.otr.app.im.IContactListListener))) {
return ((info.guardianproject.otr.app.im.IContactListListener)iin);
}
return new info.guardianproject.otr.app.im.IContactListListener.Stub.Proxy(obj);
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
case TRANSACTION_onContactChange:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
info.guardianproject.otr.app.im.IContactList _arg1;
_arg1 = info.guardianproject.otr.app.im.IContactList.Stub.asInterface(data.readStrongBinder());
info.guardianproject.otr.app.im.engine.Contact _arg2;
if ((0!=data.readInt())) {
_arg2 = info.guardianproject.otr.app.im.engine.Contact.CREATOR.createFromParcel(data);
}
else {
_arg2 = null;
}
this.onContactChange(_arg0, _arg1, _arg2);
return true;
}
case TRANSACTION_onAllContactListsLoaded:
{
data.enforceInterface(DESCRIPTOR);
this.onAllContactListsLoaded();
return true;
}
case TRANSACTION_onContactsPresenceUpdate:
{
data.enforceInterface(DESCRIPTOR);
info.guardianproject.otr.app.im.engine.Contact[] _arg0;
_arg0 = data.createTypedArray(info.guardianproject.otr.app.im.engine.Contact.CREATOR);
this.onContactsPresenceUpdate(_arg0);
return true;
}
case TRANSACTION_onContactError:
{
data.enforceInterface(DESCRIPTOR);
int _arg0;
_arg0 = data.readInt();
info.guardianproject.otr.app.im.engine.ImErrorInfo _arg1;
if ((0!=data.readInt())) {
_arg1 = info.guardianproject.otr.app.im.engine.ImErrorInfo.CREATOR.createFromParcel(data);
}
else {
_arg1 = null;
}
java.lang.String _arg2;
_arg2 = data.readString();
info.guardianproject.otr.app.im.engine.Contact _arg3;
if ((0!=data.readInt())) {
_arg3 = info.guardianproject.otr.app.im.engine.Contact.CREATOR.createFromParcel(data);
}
else {
_arg3 = null;
}
this.onContactError(_arg0, _arg1, _arg2, _arg3);
return true;
}
}
return super.onTransact(code, data, reply, flags);
}
private static class Proxy implements info.guardianproject.otr.app.im.IContactListListener
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
     * Called when:
     *  <ul>
     *  <li> a contact list has been created, deleted, renamed or loaded, or
     *  <li> a contact has been added to or removed from a list, or
     *  <li> a contact has been blocked or unblocked
     *  </ul>
     *
     * @see info.guardianproject.otr.app.im.engine.ContactListListener#onContactChange(int, ContactList, Contact)
     */
@Override public void onContactChange(int type, info.guardianproject.otr.app.im.IContactList list, info.guardianproject.otr.app.im.engine.Contact contact) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(type);
_data.writeStrongBinder((((list!=null))?(list.asBinder()):(null)));
if ((contact!=null)) {
_data.writeInt(1);
contact.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onContactChange, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
/**
     * Called when all the contact lists have been loaded from server.
     *
     * @see info.guardianproject.otr.app.im.engine.ContactListListener#onAllContactListsLoaded()
     */
@Override public void onAllContactListsLoaded() throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
mRemote.transact(Stub.TRANSACTION_onAllContactListsLoaded, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
/**
     * Called when one or more contacts' presence information has updated.
     *
     * @see info.guardianproject.otr.app.im.engine.ContactListListener#onContactsPresenceUpdate(Contact[])
     */
@Override public void onContactsPresenceUpdate(info.guardianproject.otr.app.im.engine.Contact[] contacts) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeTypedArray(contacts, 0);
mRemote.transact(Stub.TRANSACTION_onContactsPresenceUpdate, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
/**
     * Called when a previous contact related request has failed.
     *
     * @see info.guardianproject.otr.app.im.engine.ContactListListener#onContactError(int, ImErrorInfo, String, Contact)
     */
@Override public void onContactError(int errorType, info.guardianproject.otr.app.im.engine.ImErrorInfo error, java.lang.String listName, info.guardianproject.otr.app.im.engine.Contact contact) throws android.os.RemoteException
{
android.os.Parcel _data = android.os.Parcel.obtain();
try {
_data.writeInterfaceToken(DESCRIPTOR);
_data.writeInt(errorType);
if ((error!=null)) {
_data.writeInt(1);
error.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
_data.writeString(listName);
if ((contact!=null)) {
_data.writeInt(1);
contact.writeToParcel(_data, 0);
}
else {
_data.writeInt(0);
}
mRemote.transact(Stub.TRANSACTION_onContactError, _data, null, android.os.IBinder.FLAG_ONEWAY);
}
finally {
_data.recycle();
}
}
}
static final int TRANSACTION_onContactChange = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
static final int TRANSACTION_onAllContactListsLoaded = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
static final int TRANSACTION_onContactsPresenceUpdate = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
static final int TRANSACTION_onContactError = (android.os.IBinder.FIRST_CALL_TRANSACTION + 3);
}
/**
     * Called when:
     *  <ul>
     *  <li> a contact list has been created, deleted, renamed or loaded, or
     *  <li> a contact has been added to or removed from a list, or
     *  <li> a contact has been blocked or unblocked
     *  </ul>
     *
     * @see info.guardianproject.otr.app.im.engine.ContactListListener#onContactChange(int, ContactList, Contact)
     */
public void onContactChange(int type, info.guardianproject.otr.app.im.IContactList list, info.guardianproject.otr.app.im.engine.Contact contact) throws android.os.RemoteException;
/**
     * Called when all the contact lists have been loaded from server.
     *
     * @see info.guardianproject.otr.app.im.engine.ContactListListener#onAllContactListsLoaded()
     */
public void onAllContactListsLoaded() throws android.os.RemoteException;
/**
     * Called when one or more contacts' presence information has updated.
     *
     * @see info.guardianproject.otr.app.im.engine.ContactListListener#onContactsPresenceUpdate(Contact[])
     */
public void onContactsPresenceUpdate(info.guardianproject.otr.app.im.engine.Contact[] contacts) throws android.os.RemoteException;
/**
     * Called when a previous contact related request has failed.
     *
     * @see info.guardianproject.otr.app.im.engine.ContactListListener#onContactError(int, ImErrorInfo, String, Contact)
     */
public void onContactError(int errorType, info.guardianproject.otr.app.im.engine.ImErrorInfo error, java.lang.String listName, info.guardianproject.otr.app.im.engine.Contact contact) throws android.os.RemoteException;
}
