package Model;

import java.util.Scanner;

public abstract class GeneralObject {
    // General Scanners
    public static Scanner scanChoice = new Scanner(System.in);
    public static Scanner scanDelReason = new Scanner(System.in);

    // General Object Variables
    private char delIndicator;
    private String delReason;

    public GeneralObject(
        char delIndicator,
        String delReason
    ) {
        this.delIndicator = delIndicator;
        this.delReason = delReason;
    }

    public char getDelIndicator() {
        return delIndicator;
    }

    public void setDelIndicator(char delIndicator) {
        this.delIndicator = delIndicator;
    }

    public String getDelReason() {
        return delReason;
    }

    public void setDelReason(String delReason) {
        this.delReason = delReason;
    }
    
    // Convert char[] to String
    public static String convertCharToString(char[] charArray) {
        return String.valueOf(charArray).trim();
    }
}
