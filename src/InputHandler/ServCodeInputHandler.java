package InputHandler;

public class ServCodeInputHandler extends AbstractInputHandler {
    @Override
    protected boolean validate(String input) {
        if (input.length() != 3) {
            return false;
        }

        return input.matches("[a-zA-Z]+"); 
    }

    @Override
    protected void displayInvalidMessage() {
        System.out.println("Invalid input. Service Code must contain exactly three letters.");
    }
}
