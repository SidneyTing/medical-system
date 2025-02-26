package SelectionStrategy;

import InputHandler.AbstractInputHandler;
import InputHandler.LongInputHandler;
import InputHandler.TextInputHandler;
import Model.Patient;

import java.util.ArrayList;

public class NameBirthdaySelectionStrategy implements PatientSelectionStrategy {
    private static final AbstractInputHandler textHandler = new TextInputHandler();
    private static final AbstractInputHandler longHandler = new LongInputHandler();
    @Override
    public int selectPatient(ArrayList<Patient> patients) {
        int index = 0;
        int found = 0;

        System.out.println("\nLast Name: ");
        String lastName = textHandler.requestInput();

        System.out.println("\nFirst Name: ");
        String firstName = textHandler.requestInput();

        System.out.println("\nBirthday (YYYYMMDD): ");
        String birthday = longHandler.requestInput();

        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getDelIndicator() != 'D' &&
                    patients.get(i).getLastName().equals(lastName) &&
                    patients.get(i).getFirstName().equals(firstName) &&
                    patients.get(i).getBirthday().equals(birthday)) {
                found++;
                index = i;
            }
        }

        if (found == 1) {
            return index;
        } else if (found > 1) {
            System.out.println("\nMultiple patient records found. Displaying them below...");
            System.out.print("\nPatient's UID\tLast Name\tFirst Name\tMiddle Name\tBirthday\tGender\tAddress\t\t\tPhone Number\tNational ID No.\n");
            for (int i = 0; i < patients.size(); i++) {
                if (patients.get(i).getDelIndicator() != 'D' &&
                        patients.get(i).getLastName().equals(lastName) &&
                        patients.get(i).getFirstName().equals(firstName) &&
                        patients.get(i).getBirthday().equals(birthday)) {
                    System.out.print(patients.get(i).getPatUID() + "\t" +
                            patients.get(i).getLastName() + "\t\t" +
                            patients.get(i).getFirstName() + "\t\t" +
                            patients.get(i).getMiddleName() + "\t\t" +
                            patients.get(i).getBirthday() + "\t" +
                            patients.get(i).getGender() + "\t" +
                            patients.get(i).getAddress() + "\t" +
                            patients.get(i).getPhoneNo() + "\t" +
                            patients.get(i).getNationalIdNo() + "\n");
                }
            }
            System.out.println("\nSelect the Patient's UID that you want to display: ");
            String patUID = textHandler.requestInput();
            for (int i = 0; i < patients.size(); i++) {
                if (patients.get(i).getPatUID().equals(patUID) && patients.get(i).getDelIndicator() != 'D') {
                    return i;
                }
            }
        }
        return -1;
    }
}
