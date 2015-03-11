package relcy.contacts;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by PG on 3/2/2015.
 */
public class ContactsUtils {
    public ContactsUtils(){

    }


    public static ArrayList<Contact> getContactsForName( String searchName, ArrayList<Contact> contacts){

        if (searchName == null || contacts == null){
            return null;
        }

        ArrayList<Contact> output = new ArrayList<Contact>();

        Iterator<Contact> itContacts = contacts.iterator();
        while(itContacts.hasNext())
        {
            Contact contact = itContacts.next();

            if (contact.getStrContactName().toLowerCase().contains(searchName.toLowerCase())){
                output.add(contact);
            }
            //Do something with obj
        }

        return output;
    }

    public static ArrayList<Contact> getContactsForPhoneNumber (String phoneNumber, ArrayList<Contact> contacts){
        if (phoneNumber == null || contacts == null){
            return null;
        }

        ArrayList<Contact> output = new ArrayList<Contact>();

        Iterator<Contact> itContacts = contacts.iterator();
        while(itContacts.hasNext())
        {
            Contact contact = itContacts.next();

            ArrayList<String> phoneNumbers = contact.getListContactPhoneNumbers();

            for (int i = 0; i < phoneNumbers.size(); i++){
                if(phoneNumbers.get(i).contains(phoneNumber)){
                    output.add(contact);
                }
            }
        }

        return output;
    }

    public static String constructString(ArrayList<Contact> contacts){

        if (contacts == null){
            return null;
        }

        StringBuffer output = new StringBuffer();

        for (int i = 0; i< contacts.size(); i++){
            Contact contact = contacts.get(i);

            output.append("\n First Name: " + contact.getStrContactName());

            //get phone numbers
            ArrayList<String> phoneNumbers = contact.getListContactPhoneNumbers();
            if (phoneNumbers != null){
                for (int j = 0; j < phoneNumbers.size(); j++){
                    output.append("\n Phone Number: " + phoneNumbers.get(j));
                }
            }


            //get emails
            ArrayList<String> emails = contact.getStrContactEmails();
            if (emails != null){
                for (int k = 0; k < emails.size(); k++) {
                    output.append("\n Email: " + emails.get(k));
                }
            }



        }

        return output.toString();
    }
}
