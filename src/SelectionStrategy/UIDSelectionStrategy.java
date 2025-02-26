package SelectionStrategy;

import InputHandler.AbstractInputHandler;
import InputHandler.TextInputHandler;
import Model.Patient;

import java.util.ArrayList;

public class UIDSelectionStrategy implements PatientSelectionStrategy {
    private static final AbstractInputHandler textHandler = new TextInputHandler();
    @Override
    public int selectPatient(ArrayList<Patient> patients) {
        System.out.println("\nPatient's UID: ");
        String patUID = textHandler.requestInput();
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getPatUID().equals(patUID) && patients.get(i).getDelIndicator() != 'D') {
                return i;
            }
        }
        return -1;
    }
}