package relcy.contacts;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v7.app.ActionBarActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

import java.io.InputStream;
import java.util.ArrayList;

/*
Problem Statement:
Emulate Contacts app...
Search would return and show the Photo, Name of the Person and the Phone-Number or email
Photo would be a clickable link. Clicking on the photo would take you the respective contact and open the contact page

Solution:
EditText to search for the contacts
ListView to update based on the search
    - Listview will be divided into three sections --> Photo (BitMap), Name, Phone Number)
    - OnTextChanged event listener will be implemented..
    - As and when

    - Create a grid layout...with ListView in each grid..
    - Preprocess the contacts information..
    - When the text changes load from the pre processed information
    -
    - Do I need a hashtable to save the
 */
public class ContactsActivity extends ActionBarActivity {
    private String TAG = "ContactsActivity";
    EditText etSearchBar;
    TextView tvContactDetails;
    //ImageButton ibContactInfo;
    TextView tvTest;

    Contact contact;
    ArrayList<Contact> listContacts;

    private void initialize( ){
        etSearchBar = (EditText)findViewById(R.id.editTextSearch);
        tvContactDetails = (TextView)findViewById(R.id.textViewContactDetails);
        tvTest = (TextView)findViewById(R.id.textViewContacts);
        listContacts = new ArrayList<Contact>();

        //create listeners
        //createImageButtonListener( );
        createSearchBarListener();
        createTextViewListener( );
    }

    //methods to create event listeners
    private void createTextViewListener() {

    }

    /*private void createImageButtonListener( ){
        //send the intent to the contacts provider to open the contact with the details..
        ibContactInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //get the
            }
        });
    }*/

    private void createSearchBarListener( ){
        //add listeners
        etSearchBar.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                Log.d(TAG, "before text changed");
                Log.d(TAG, "before text changed" + s.toString());
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                Log.d(TAG, "on text changed");
                Log.d(TAG, "on text changed" + s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d(TAG, "after text changed");
                Log.d(TAG, "after text changed" + s.toString());

                //update the view based on the search text in the edit text..
                //update the image button with the image from the contacts provider
                //update the other contact details (Name, PhoneNumber(s), Email(s))
                //tvContactDetails.setText(strContacts );
                //getWords starting with
                String contactsToDisplay = ContactsUtils.constructString(listContacts);
                tvTest.setText(contactsToDisplay);
            }

        });
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.contacts_activity);
        initialize();

        ContactInfo contactInfo = new ContactInfo();
        contactInfo.execute("start");



    }

    @Override
    protected  void onStart(){
        super.onStart();

    }

    @Override
    protected void onResume( ){
        super.onResume();
        if (listContacts != null){
            //display in the grid view (or text view)
            String contactsToDisplay = ContactsUtils.constructString(listContacts);
            tvTest.setText(contactsToDisplay);
        }

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contacts, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class ContactInfo extends AsyncTask<String, Void, String> {
        ArrayList<Contact> threadContacts = new ArrayList<Contact>( );

        @Override
        protected String doInBackground(String... params) {

            String response = params[0];

            //if (response.equls)
            extractContacts();

            if (threadContacts != null){
                response = "done";
            }
            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            if(result.equals("done")){
                String contactsToDisplay = ContactsUtils.constructString(threadContacts);
                tvTest.setText(contactsToDisplay);
            }
        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected void onProgressUpdate(Void... values) {

        }

        private void extractContacts( ){
            ContentResolver contentResolver = getContentResolver();

            Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI,
                    new String[]
                            {ContactsContract.Contacts._ID,
                                    ContactsContract.Contacts.DISPLAY_NAME,
                                    ContactsContract.Contacts.PHOTO_ID,
                                    ContactsContract.Contacts.HAS_PHONE_NUMBER
                            },
                    null, null, null);

            if(cursor.getCount( ) > 0){
                while(cursor.moveToNext()){
                    Contact contact = new Contact();
                    String contactID = cursor.getColumnName(cursor.getColumnIndex(ContactsContract.Contacts._ID));
                    String photoID = cursor.getColumnName(cursor.getColumnIndex(ContactsContract.Contacts.PHOTO_ID));

                    contact.setStrContactName(cursor.getColumnName(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME)));

                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER)));

                    //extract phone Numbers...there can be more than one phone number..
                    if (hasPhoneNumber > 0){

                        Cursor phoneCursor = contentResolver.query(
                                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                                null,
                                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                                new String[]{contactID},
                                null
                        );

                        ArrayList<String> phoneNumbers = new ArrayList<String>( );
                        while (phoneCursor.moveToNext()) {
                            String phoneNumber = phoneCursor.getString(
                                    phoneCursor.getColumnIndex(
                                            ContactsContract.CommonDataKinds.Phone.NUMBER
                                    ));

                            phoneNumbers.add(phoneNumber);
                        }

                        contact.setListContactPhoneNumber(phoneNumbers);
                        phoneCursor.close();

                    }

                    //extract emails
                    Cursor emailCursor = contentResolver.query(
                            ContactsContract.CommonDataKinds.Email.CONTENT_URI,
                            null,
                            ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = ?",
                            new String[]{contactID},
                            null);

                    ArrayList<String> emails = new ArrayList<String>( );
                    while (emailCursor.moveToNext()) {
                        String email = emailCursor.getString(
                                emailCursor.getColumnIndex(
                                        ContactsContract.CommonDataKinds.Email.DATA
                                ));

                        emails.add(email);
                    }
                    contact.setStrContactEmails(emails);
                    emailCursor.close();

                    //load contact Info
                    //contact.setBmContactImage(loadContactPhoto(contentResolver, contactID, photoID));

                    threadContacts.add(contact);

                }




            }

        }
        private Bitmap loadContactPhoto( ContentResolver contentResolver, String contactId, String photoID) {
            Uri uri = ContentUris.withAppendedId(ContactsContract.Contacts.CONTENT_URI, Long.parseLong(contactId));

            InputStream input = ContactsContract.Contacts.openContactPhotoInputStream(contentResolver, uri);

            if (input != null)
            {
                return BitmapFactory.decodeStream(input);
            }
            else
            {
                Log.d(TAG, "first try failed to load photo");

            }

            byte[] photoBytes = null;
            Uri photoUri = ContentUris.withAppendedId(ContactsContract.Data.CONTENT_URI, Long.parseLong(photoID));

            Cursor photoCursor = contentResolver.query(
                    photoUri,
                    new String[] {ContactsContract.CommonDataKinds.Photo.PHOTO},
                    null, null, null);

            try
            {
                if (photoCursor.moveToFirst())
                    photoBytes = photoCursor.getBlob(0);

            } catch (Exception e) {
                // TODO: handle exception
                Log.d(TAG, e.toString());

            } finally {
                photoCursor.close();
            }

            if (photoBytes != null)
                return BitmapFactory.decodeByteArray(photoBytes,0,photoBytes.length);
            else
                Log.d(TAG, "Second try also failed");

            return null;
        }

/*
        private void extractContacts( ) {
            String phoneNumber = null;
            String email = null;

            Uri Content_URI = ContactsContract.Contacts.CONTENT_URI;
            String ID = ContactsContract.Contacts._ID;
            String DISPLAY_NAME = ContactsContract.Contacts.DISPLAY_NAME;
            String HAS_PHONE_NUMBER = ContactsContract.Contacts.HAS_PHONE_NUMBER;

            Uri phoneContentUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;
            String PHONE_CONTACT_ID = ContactsContract.CommonDataKinds.Phone.CONTACT_ID;
            String NUMBER = ContactsContract.CommonDataKinds.Phone.NUMBER;

            Uri emailContentURI = ContactsContract.CommonDataKinds.Email.CONTENT_URI;
            String emailContactID = ContactsContract.CommonDataKinds.Email.CONTACT_ID;
            String EMAIL_DATA = ContactsContract.CommonDataKinds.Email.DATA;

            //String photoContentUri = ContactsContract.CommonDataKinds.Photo.C
            String PHOTO_CONTACT_ID = ContactsContract.CommonDataKinds.Photo.PHOTO_ID;
            String photo = ContactsContract.CommonDataKinds.Photo.PHOTO;

            ContentResolver contentResolver = getContentResolver();

            Cursor cursor = contentResolver.query(Content_URI, null, null, null, null);

            if (cursor.getCount() > 0) {
                while (cursor.moveToNext()) {
                    String contactID = cursor.getString(cursor.getColumnIndex(ID));
                    String name = cursor.getString(cursor.getColumnIndex(DISPLAY_NAME));

                    int hasPhoneNumber = Integer.parseInt(cursor.getString(cursor.getColumnIndex(HAS_PHONE_NUMBER)));

                    if (hasPhoneNumber > 0) {
                        //get the phone number and use it as a key for the hashtable of
                        // contacts..
                        Contact contact = new Contact( );
                        contact.setStrContactName(name);

                        Cursor phoneCursor = contentResolver.query(phoneContentUri, null, PHONE_CONTACT_ID
                                + " = ?", new String[]{contactID}, null);

                        ArrayList<String> phoneNumbers = new ArrayList<String>( );
                        while (phoneCursor.moveToNext()) {
                            phoneNumber = phoneCursor.getString(phoneCursor.getColumnIndex(NUMBER));
                            phoneNumbers.add(phoneNumber);
                        }

                        contact.setListContactPhoneNumber(phoneNumbers);
                        phoneCursor.close();

                        Cursor emailCursor = contentResolver.query(emailContentURI, null,
                                emailContactID + " = ?", new String[]{contactID}, null);

                        ArrayList<String> emails = new ArrayList<String>( );
                        while (emailCursor.moveToNext()) {
                            email = emailCursor.getString(emailCursor.getColumnIndex(EMAIL_DATA));
                            emails.add(email);
                        }
                        contact.setStrContactEmails(emails);
                        emailCursor.close();

                        //Cursor photoCursor = contentResolver.query()
                    }
                    threadContacts.add(contact);
                }

            }
        }*/
    }
}

