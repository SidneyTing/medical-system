public class TextInputHandler extends AbstractInputHandler {
    @Override
    protected boolean validate(String input) {
        return !input.isEmpty();
    }

    @Override
    protected void displayInvalidMessage() {
        System.out.println("Please enter an input.");
    }
}
