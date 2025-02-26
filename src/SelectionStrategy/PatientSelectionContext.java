package SelectionStrategy;

import Model.Patient;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientSelectionContext {
    private PatientSelectionStrategy strategy;
    private final Map<String, PatientSelectionStrategy> strategies = new HashMap<>();

    public PatientSelectionContext() {
        strategies.put("1", new UIDSelectionStrategy());
        strategies.put("2", new NameBirthdaySelectionStrategy());
        strategies.put("3", new NationalIDSelectionStrategy());
    }

    public void setStrategy(String option) {
        this.strategy = strategies.get(option);
    }

    public int execute(ArrayList<Patient> patients) {
        if (strategy == null) {
            throw new IllegalStateException("Strategy has not been set.");
        }
        return strategy.selectPatient(patients);
    }
}
