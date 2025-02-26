package SelectionStrategy;

import InputHandler.AbstractInputHandler;
import InputHandler.LongInputHandler;
import Model.Patient;

import java.util.ArrayList;

public class NationalIDSelectionStrategy implements PatientSelectionStrategy {
    private static final AbstractInputHandler longHandler = new LongInputHandler();
    @Override
    public int selectPatient(ArrayList<Patient> patients) {
        System.out.println("\nNational ID No.: ");
        String nationalIdNo = longHandler.requestInput();
        for (int i = 0; i < patients.size(); i++) {
            if (patients.get(i).getNationalIdNo().equals(nationalIdNo) && patients.get(i).getDelIndicator() != 'D') {
                return i;
            }
        }
        return -1;
    }
}
