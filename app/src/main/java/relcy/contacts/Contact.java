package relcy.contacts;

import android.graphics.Bitmap;

import java.util.ArrayList;

/**
 * Created by PG on 2/28/2015.
 */
public class Contact {

    public Bitmap getBmContactImage() {
        return bmpContactPhoto;
    }

    public void setBmContactImage(Bitmap bmpContactPhoto) {
        this.bmpContactPhoto = bmpContactPhoto;
    }

    public String getStrContactName() {
        return strContactName;
    }

    public void setStrContactName(String strContactName) {
        this.strContactName = strContactName;
    }

    public ArrayList<String> getListContactPhoneNumbers() {
        return listContactPhoneNumbers;
    }

    public void setListContactPhoneNumber(ArrayList<String> listContactPhoneNumbers) {
        this.listContactPhoneNumbers = listContactPhoneNumbers;
    }

    public ArrayList<String> getStrContactEmails() {
        return listContactEmails;
    }

    public void setStrContactEmails(ArrayList<String> listContactEmails) {
        this.listContactEmails = listContactEmails;
    }

    private Bitmap bmpContactPhoto;
    private String strContactName;
    private ArrayList<String> listContactPhoneNumbers;
    private ArrayList<String> listContactEmails;

}




