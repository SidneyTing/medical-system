package InputHandler;

public class LongInputHandler extends AbstractInputHandler {
    @Override
    protected boolean validate(String input) {
        try {
            Long.parseLong(input);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    @Override
    protected void displayInvalidMessage() {
        System.out.println("Invalid input. Must contain only digits.");
    }
}

