package SelectionStrategy;

import Model.Patient;

import java.util.ArrayList;

public interface PatientSelectionStrategy {
    int selectPatient(ArrayList<Patient> patients);
}
