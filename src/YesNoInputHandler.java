public class YesNoInputHandler extends AbstractInputHandler {
    @Override
    protected boolean validate(String input) {
        return input.equalsIgnoreCase("Y") || input.equalsIgnoreCase("N");
    }

    @Override
    protected void displayInvalidMessage() {
        System.out.println("Invalid input. Please enter \"Y\" or \"N\".");
    }
}
