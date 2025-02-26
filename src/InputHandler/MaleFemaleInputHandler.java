package InputHandler;

public class MaleFemaleInputHandler extends AbstractInputHandler {
    @Override
    protected boolean validate(String input) {
        if (input.isEmpty()) {
            return false;
        }
        return input.substring(0, 1).equalsIgnoreCase("M") || input.substring(0, 1).equalsIgnoreCase("F");
    }

    @Override
    protected void displayInvalidMessage() {
        System.out.println("Invalid input. Please enter \"M\" or \"F\".");
    }
}
