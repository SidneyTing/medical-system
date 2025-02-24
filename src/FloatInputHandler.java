public class FloatInputHandler extends AbstractInputHandler {
    @Override
    protected boolean validate(String input) {
        try {
            Float.parseFloat(input);
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

