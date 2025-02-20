import java.util.Scanner;

public abstract class AbstractInputHandler {
    private final Scanner scanner = new Scanner(System.in);

    public final String requestInput() {
        String input;
        boolean isValid;

        do {
            displayPrompt();
            input = getInput();
            isValid = validate(input);

            if (!isValid) {
                displayInvalidMessage();
            }
        } while (!isValid);
        
        return input;
    }

    protected void displayPrompt() {
        System.out.print("Enter: ");
    }

    protected String getInput() {
        return scanner.nextLine().trim();
    }

    // Hooks
    protected abstract boolean validate(String input);
    protected abstract void displayInvalidMessage();
}
