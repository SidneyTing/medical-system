import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.time.LocalDateTime; 
import java.io.FileOutputStream;

// iText
import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.Font.FontFamily;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.itextpdf.text.pdf.draw.LineSeparator;

public class PatFunctions {
    // Patient Scanners
    public static Scanner scanPUID = new Scanner(System.in);
    public static Scanner scanFirstName = new Scanner(System.in);
    public static Scanner scanLastName = new Scanner(System.in);
    public static Scanner scanMiddletName = new Scanner(System.in);
    public static Scanner scanBirthday = new Scanner(System.in);
    public static Scanner scanGender = new Scanner(System.in);
    public static Scanner scanAddress = new Scanner(System.in);
    public static Scanner scanPhoneNo = new Scanner(System.in);
    public static Scanner scanNationalIdNo = new Scanner(System.in);
    public static Scanner scanRUID = new Scanner(System.in);

    /* PATIENT METHODS */
    // Read Patients txt file
    public static void readPatFile(ArrayList<Patient> patients) {
        try {
            File patFile = new File("Patients.txt");
            Scanner patReader = new Scanner(patFile);

            char temp[] = new char[30];  // temporary char array
            
            int i = 0;
            int j = 0;

            while (patReader.hasNextLine()) {
                String data = patReader.nextLine();

                String patUID = "";
                String firstName = "";
                String lastName = "";
                String middleName = "";
                long birthday = 0;  // YYYYMMDD
                char gender = '\0';
                String address = "";
                long phoneNo = 0;
                long nationalIdNo = 0;
                char delIndicator = '\0';
                String delReason = "";

                int semiCtr = 0;  // semi-colon counter

                for (char c : data.toCharArray()) {
                    if (c == ';') {

                        // Patient's UID
                        if (semiCtr == 0) {
                            patUID = GeneralObject.convertCharToString(temp);

                        // First Name
                        } else if (semiCtr == 1) {
                            firstName = GeneralObject.convertCharToString(temp);

                        // Last Name
                        } else if (semiCtr == 2) {
                            lastName = GeneralObject.convertCharToString(temp);

                        // Middle Name
                        } else if (semiCtr == 3) {
                            middleName = GeneralObject.convertCharToString(temp);

                        // Birthday
                        } else if (semiCtr == 4) {
                            birthday = Long.parseLong(GeneralObject.convertCharToString(temp));

                        // Gender
                        } else if (semiCtr == 5) {
                            gender = temp[0];
                        
                        // Address
                        } else if (semiCtr == 6) {
                            address = GeneralObject.convertCharToString(temp);
                        
                        // Phone Number
                        } else if (semiCtr == 7) {
                            phoneNo = Long.parseLong(GeneralObject.convertCharToString(temp));

                        // National ID Number
                        } else if (semiCtr == 8) {
                            nationalIdNo = Long.parseLong(GeneralObject.convertCharToString(temp));

                        // Delete Indicator
                        } else if (semiCtr == 9) {
                            delIndicator = temp[0];
                        
                        // Delete Reason
                        } else if (semiCtr == 10) {
                            delReason = GeneralObject.convertCharToString(temp);
                        }

                        // reinitialize temp
                        for (j = 0; j < temp.length; j++) {
                            temp[j] = ' ';
                        }

                        semiCtr += 1;  // increment semi-colon counter
                        i = 0;  // reset index

                    } else {
                        temp[i] = c;
                        i++;
                    }
                }

                patients.add(new Patient(patUID, firstName, lastName, middleName, birthday, gender, address, phoneNo, nationalIdNo, delIndicator, delReason));
                // System.out.println(patUID + "*" + firstName + "*" + lastName + "*" + middleName + "*" + birthday + "*" + gender + "*" + address + "*" + phoneNo + "*" + nationalIdNo + "*" + delIndicator + "*" +delReason + "*");
            }

            patReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Generate Patient's UID
    private static String generatePUID(ArrayList<Patient> patients) {
        String patientUID;
        File fileUID = new File("Patients.txt");

        // System date and gets month & year
        LocalDateTime date = java.time.LocalDateTime.now();
        String dateString = date.toString();
        
        String month = dateString.substring(5,7);
        String year = dateString.substring(0,4);
        
        String letters = "AAA";  // letter code
        String numbers = "00";  // number code

        // Checks if file is not blank
        if (fileUID.length() != 0) {

            int size = patients.size();  // to get array index
            
            String month1 = (patients.get(size - 1).getPatUID()).substring(5,7);  // checks month of previous PUID

            if (fileUID.length() == 1){
               
                if (!(month.equals(month1))) {  //compares the month of the record of previous patient and about to be added patient
                    letters= "AAA";  // resets to AAA and 00
                    numbers = "00";
                }
                else{
                    numbers = "01";
                }
            }

            else if (fileUID.length() >= 2) {  // this portion checks the month of each UID and checks if it is different
                
                if (!(month.equals(month1))) {  // compares the month of the record of previous patient and about to be added patient
                    letters= "AAA";  // resets to AAA and 00
                    numbers = "00";
                }

                else{
                    String tempUID;
                    int tempNumbers;

                    tempUID = patients.get(size - 1).getPatUID();

                    letters = tempUID.substring(7,10);
                    numbers = tempUID.substring(10,12);
                    
                    char[] arrayLetters = letters.toCharArray();  //turns letter code into char array

                    // Checks if number code is 99
                    if (numbers.equals("99")) {
                        numbers = "00";
                        
                        // Checks if letter code is form of AZZ, BZZ, CZZ and fixes it. AZZ -> BAA, BZZ -> CAA
                        if (arrayLetters[2] == 'Z' && arrayLetters[1] == 'Z') {

                            arrayLetters[2] = 'A';
                            arrayLetters[1] = 'A';
                            arrayLetters[0] += 1;
                        }
                        // Checks if letter code is form of AAZ, ABZ, CDZ and fixes it. AAZ -> ABA, CDZ -> CEA
                        else if (arrayLetters[2] == 'Z') {
                            arrayLetters[2] = 'A';
                            arrayLetters[1] += 1;
                        }
                        // Increments last letter of code by 1. AAA -> AAB, BCD -> BCE
                        else {
                            arrayLetters[2] += 1;
                        }
                    }

                    // Adds number by 1. 00 -> 01, 95 -> 96
                    else {
                        tempNumbers = Integer.parseInt(numbers);
                        tempNumbers++;
                        numbers = String.valueOf(tempNumbers);
                        
                        // Adds a 0 in front of numbers since initially it will only be 0, 1, 2 and not 00, 01, 02
                        if (tempNumbers < 10) {
                            numbers = "0" + numbers;  
                        }
                    }
                    
                    letters = String.valueOf(arrayLetters);  // turns char array back to letter code string
                }
            }
        }

        patientUID = ("P" + year + month + letters + numbers);  // adds up everything

        return patientUID;
    }

    // Add Patient
    public static void addPatient(ArrayList<Patient> patients) {
        String patUID;
        String firstName;
        String lastName;
        String middleName;
        long birthday = 0;  // YYYYMMDD
        char gender;
        String address;
        long phoneNo = 0;
        long nationalIdNo = 0;
        char delIndicator = '\0';
        String delReason = "";

        String temp;
        int error = 0;

        int choice;

        do {
            patUID = generatePUID(patients);  // generates Patient's UID

            // First Name
            do {
                System.out.print("\nFirst Name: ");
                firstName = scanFirstName.nextLine();

                if (firstName.isEmpty()) {
                    System.out.print("\nPlease enter an input.");
                }
            } while (firstName.isEmpty());
            
            // Last Name
            do {
                System.out.print("Last Name: ");
                lastName = scanLastName.nextLine();

                if (lastName.isEmpty()) {
                    System.out.print("\nPlease enter an input.\n");
                }
            } while (lastName.isEmpty());

            // Middle Name
            do {
                System.out.print("Middle Name: ");
                middleName = scanMiddletName.nextLine();

                if (middleName.isEmpty()) {
                    System.out.print("\nPlease enter an input.\n");
                }
            } while (middleName.isEmpty());

            // Birthday
            do {
                System.out.print("Birthday (YYYYMMDD): ");
                temp = scanBirthday.nextLine();

                try {
                    birthday = Long.parseLong(temp);
                    error = 0;
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input. Must contain only digits.");
                    error = 1;
                }
            } while (error == 1);
            
            // Gender
            System.out.print("Gender: ");
            gender = Character.toUpperCase(scanGender.next().charAt(0));  // auto capitalization

            // Address
            do {
                System.out.print("Address: ");
                address = scanAddress.nextLine();

                if (address.isEmpty()) {
                    System.out.print("\nPlease enter an input.\n");
                }
            } while (address.isEmpty());

            // Phone No.
            do {
                System.out.print("Phone No.: ");
                temp = scanPhoneNo.nextLine();

                try {
                    phoneNo = Long.parseLong(temp);
                    error = 0;
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input. Must contain only digits.");
                    error = 1;
                }
            } while (error == 1); 

            // National ID No.
            do {
                System.out.print("National ID No.: ");
                temp = scanNationalIdNo.nextLine();

                try {
                    nationalIdNo = Long.parseLong(temp);
                    error = 0;
                } catch (NumberFormatException e) {
                    System.out.println("\nInvalid input. Must contain only digits.");
                    error = 1;
                }
            } while (error == 1);

            // Save record confirmation
            do {
                System.out.print("\nSave Patient Record? [Y/N]: ");
                choice = GeneralObject.scanChoice.next().charAt(0);

                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                    System.out.print("\nInvalid input.");
                }
            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

            if (choice == 'Y' || choice == 'y') {
                try {
                    FileWriter myWriter = new FileWriter("Patients.txt", true);
                    myWriter.write(patUID + ";" + firstName + ";" + lastName + ";" + middleName + ";" + birthday + ";" + gender + ";" + address + ";" + phoneNo + ";" + nationalIdNo + ";\n");
                    myWriter.close();
                    patients.add(new Patient(patUID, firstName, lastName, middleName, birthday, gender, address, phoneNo, nationalIdNo, delIndicator, delReason)); 
                } catch (IOException e) {
                    System.out.print("An error occurred.");
                    e.printStackTrace();
                }

                System.out.print("\nPatient " + patUID + " has been added.\n");
            }

            // Repeat prompt
            do {
                System.out.print("\nDo you want to add another patient? [Y/N]: ");
                choice = GeneralObject.scanChoice.next().charAt(0);

                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                    System.out.print("\nInvalid input.");
                }
            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');
        } while (choice == 'Y' || choice == 'y');
    }

    // Select Patient
    public static int selectPatient(ArrayList<Patient> patients) {
        int i = 0;
        int index = 0;
        int found = 0;
        int choice;
        String patUID;
        String firstName;
        String lastName;
        long birthday = 0;  // YYYYMMDD
        long nationalIdNo = 0;

        String temp;
        int error = 0;
        
        do {
            System.out.print("\nSelect a Patient:\n[1] Use Patient's UID\n[2] Use Last Name, First Name, Birthday\n[3] Use National ID No.\n[X] Return to Main Menu\n\nSelect an option: ");
            choice = GeneralObject.scanChoice.next().charAt(0);

            while (choice != '1' && choice != '2' && choice != '3' && choice != 'X' && choice != 'x') {
                System.out.print("\nInvalid input.\nPlease select an option: ");
                choice = GeneralObject.scanChoice.next().charAt(0);
            }
            
            // Search using Patient's UID
            if (choice == '1') {
                do {
                    System.out.print("\nPatient's UID: ");
                    patUID = scanPUID.nextLine();

                    if (patUID.isEmpty()) {
                        System.out.print("\nPlease enter an input.");
                    }
                } while (patUID.isEmpty());

                for (i = 0; i < patients.size(); i++) {
                    if (patients.get(i).getPatUID().equals(patUID) && patients.get(i).getDelIndicator() != 'D') {
                        return i;
                    }
                }

            // Search using Last Name, First Name, Birthday
            } else if (choice == '2') {
                // Last Name
                do {
                    System.out.print("\nLast Name: ");
                    lastName = scanLastName.nextLine();

                    if (lastName.isEmpty()) {
                        System.out.print("\nPlease enter an input.");
                    }
                } while (lastName.isEmpty());

                // First Name
                do {
                    System.out.print("First Name: ");
                    firstName = scanFirstName.nextLine();

                    if (firstName.isEmpty()) {
                        System.out.print("\nPlease enter an input.\n");
                    }
                } while (firstName.isEmpty());

                // Birthday
                do {
                    System.out.print("Birthday (YYYYMMDD): ");
                    temp = scanBirthday.nextLine();

                    try {
                        birthday = Long.parseLong(temp);
                        error = 0;
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid input. Must contain only digits.");
                        error = 1;
                    }
                } while (error == 1);

                found = 0;

                for (i = 0; i < patients.size(); i++) {
                    if (patients.get(i).getDelIndicator() != 'D' && patients.get(i).getLastName().equals(lastName) && patients.get(i).getFirstName().equals(firstName) && patients.get(i).getBirthday() == birthday) {
                        found += 1;
                        index = i;
                        System.out.print("\n");
                    }
                }

                if (found == 1) {
                    return index;

                } else if (found > 1) {
                    i = 0;  // reinitialize index
                    System.out.print("Patient's UID\tLast Name\tFirst Name\tMiddle Name\tBirthday\tGender\tAddress\t\t\tPhone Number\tNational ID No.\n");
                    for (i = 0; i < patients.size(); i++) {
                        if (patients.get(i).getDelIndicator() != 'D' && patients.get(i).getLastName().equals(lastName) && patients.get(i).getFirstName().equals(firstName) && patients.get(i).getBirthday() == birthday) {
                            System.out.print(patients.get(i).getPatUID() + "\t" + patients.get(i).getLastName() + "\t\t" + patients.get(i).getFirstName() + "\t\t" + patients.get(i).getMiddleName() + "\t\t" + patients.get(i).getBirthday() + "\t" + patients.get(i).getGender() + "\t" + patients.get(i).getAddress() + "\t" + patients.get(i).getPhoneNo() + "\t" + patients.get(i).getNationalIdNo() + "\n");
                        }
                    }

                    // Enter Patient's UID
                    do {
                        System.out.print("\nEnter the Patient's UID that you want to display: ");
                        patUID = scanPUID.nextLine();

                        if (patUID.isEmpty()) {
                            System.out.print("\nPlease enter an input.");
                        }
                    } while (patUID.isEmpty());

                    for (i = 0; i < patients.size(); i++) {
                        if (patients.get(i).getPatUID().equals(patUID) && patients.get(i).getDelIndicator() != 'D') {
                            return i;
                        }
                    }
                }

            // Search using National ID No.
            } else if (choice == '3') {
                System.out.print("\n");
                do {
                    System.out.print("National ID No.: ");
                    temp = scanNationalIdNo.nextLine();

                    try {
                        nationalIdNo = Long.parseLong(temp);
                        error = 0;
                    } catch (NumberFormatException e) {
                        System.out.println("\nInvalid input. Must contain only digits.");
                        error = 1;
                    }
                } while (error == 1);
                
                for (i = 0; i < patients.size(); i++) {
                    if (patients.get(i).getNationalIdNo() == nationalIdNo && patients.get(i).getDelIndicator() != 'D') {
                        return i;
                    }
                }
            } else {
                return -1;
            }

            // No record found
            System.out.print("\nNo record found.");

            // Repeat prompt
            do {
                System.out.print("\nSearch again? [Y/N]: ");
                choice = GeneralObject.scanChoice.next().charAt(0);
                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                    System.out.print("\nInvalid input.");
                }
            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');
        } while (choice == 'Y' || choice == 'y');

        return -1;
    }

    // Edit Patient
    public static int editPatient(ArrayList<Patient> patients, int index) {
        int choice;
        int choice1;
        String address = "";
        long phoneNo = 0;

        System.out.print("\nPatient Update Options:\n[1] Patient's Address\n[2] Patient's Phone No.\n[X] Return to Main Menu\n\nSelect an update option: ");
        choice = GeneralObject.scanChoice.next().charAt(0);

        while (choice != '1' && choice != '2' && choice != 'X' && choice != 'x') {
            System.out.print("\nInvalid input.\nPlease select an update option: ");
            choice = GeneralObject.scanChoice.next().charAt(0);
        }
        
        // Patient's Address
        if (choice == '1') {
            do {
                System.out.print("\nNew Patient's Address: ");
                address = scanAddress.nextLine();

                if (address.isEmpty()) {
                    System.out.print("\nPlease enter an input.");
                }
            } while (address.isEmpty());

        // Phone No.
        } else if (choice == '2') {
            String temp;
            int error = 0;
            do {
                System.out.print("\nNew Patient's Phone No.: ");
                temp = scanPhoneNo.nextLine();

                try {
                    phoneNo = Long.parseLong(temp);
                    error = 0;
                } catch (NumberFormatException e) {
                    System.out.print("\nInvalid input. Must contain only digits.");
                    error = 1;
                }
            } while (error == 1); 
        } else {
            return -1;
        }

        // Update record confirmation
        do {
            System.out.print("\nUpdate Patient Record? [Y/N]: ");
            choice1 = GeneralObject.scanChoice.next().charAt(0);   
            if (choice1 != 'Y' && choice1 != 'y' && choice1 != 'N' && choice1 != 'n') {
                System.out.print("\nInvalid input.");
            }
        } while (choice1 != 'Y' && choice1 != 'y' && choice1 != 'N' && choice1 != 'n');

        if (choice1 == 'Y' || choice1 == 'y') {
            try {
                File patFile = new File("Patients.txt");
                File tempFile = new File("temp.txt");
                Scanner patReader = new Scanner(patFile);
    
                char temp[] = new char[30];  // temporary char array
                
                int lineCtr = 0;  // line counter
                int i = 0;
                int j = 0;

                String patUID = "";
                String firstName = "";
                String lastName = "";
                String middleName = "";
                long birthday = 0;  // YYYYMMDD
                char gender = '\0';
                String temp_address = "";
                long temp_phoneNo = 0;
                long nationalIdNo = 0;
    
                while (patReader.hasNextLine()) {
                    String data = patReader.nextLine();

                    if (lineCtr == index) {          
                        int semiCtr = 0;  // semi-colon counter

                        for (char c : data.toCharArray()) {
                            if (c == ';') {

                                // Patient's UID
                                if (semiCtr == 0) {
                                    patUID = GeneralObject.convertCharToString(temp);
        
                                // First Name
                                } else if (semiCtr == 1) {
                                    firstName = GeneralObject.convertCharToString(temp);
        
                                // Last Name
                                } else if (semiCtr == 2) {
                                    lastName = GeneralObject.convertCharToString(temp);
        
                                // Middle Name
                                } else if (semiCtr == 3) {
                                    middleName = GeneralObject.convertCharToString(temp);
        
                                // Birthday
                                } else if (semiCtr == 4) {
                                    birthday = Long.parseLong(GeneralObject.convertCharToString(temp));
        
                                // Gender
                                } else if (semiCtr == 5) {
                                    gender = temp[0];
                                
                                // Address
                                } else if (semiCtr == 6) {
                                    temp_address = GeneralObject.convertCharToString(temp);
                                
                                // Phone Number
                                } else if (semiCtr == 7) {
                                    temp_phoneNo = Long.parseLong(GeneralObject.convertCharToString(temp));
        
                                // National ID Number
                                } else if (semiCtr == 8) {
                                    nationalIdNo = Long.parseLong(GeneralObject.convertCharToString(temp));
                                }
        
                                // reinitialize temp
                                for (j = 0; j < temp.length; j++) {
                                    temp[j] = ' ';
                                }
        
                                semiCtr += 1;  // increment semi-colon counter
                                i = 0;  // reset index
        
                            } else {
                                temp[i] = c;
                                i++;
                            }
                        }

                        // Edit Address
                        if (choice == '1') {
                            try {
                                FileWriter myWriter = new FileWriter("temp.txt", true);
                                myWriter.write(patUID + ";" + firstName + ";" + lastName + ";" + middleName + ";" + birthday + ";" + gender + ";" + address + ";" + temp_phoneNo + ";" + nationalIdNo + ";\n");
                                myWriter.close();
                            } catch (IOException e) {
                                System.out.print("An error occurred.");
                                e.printStackTrace();
                            }
                        
                        // Edit Phone Number
                        } else {
                            try {
                                FileWriter myWriter = new FileWriter("temp.txt", true);
                                myWriter.write(patUID + ";" + firstName + ";" + lastName + ";" + middleName + ";" + birthday + ";" + gender + ";" + temp_address + ";" + phoneNo + ";" + nationalIdNo + ";\n");
                                myWriter.close();
                            } catch (IOException e) {
                                System.out.print("An error occurred.");
                                e.printStackTrace();
                            }
                        }
                        
                    } else {
                        try {
                            FileWriter myWriter = new FileWriter("temp.txt", true);
                            myWriter.write(data + "\n");
                            myWriter.close();
                        } catch (IOException e) {
                            System.out.print("An error occurred.");
                            e.printStackTrace();
                        }
                    }

                    lineCtr++;
                }
    
                patReader.close();
                
                // Replace original file with temp file
                patFile.delete();
                tempFile.renameTo(patFile);
                
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }

            if (choice == '1') {
                patients.get(index).setAddress(address);
                System.out.print("\nThe Address of Patient " + patients.get(index).getPatUID() + " has been updated.\n");
                return 1;
            } else {
                patients.get(index).setPhoneNo(phoneNo);
                System.out.print("\nThe Phone Number of Patient " + patients.get(index).getPatUID() + " has been updated.\n");
                return 1;
            }         
        }  // Else return to main menu

        return 1;
    }

    // Delete Patient
    public static void deletePatient(ArrayList<Patient> patients, int index) {
        String delReason;
        int choice;

        // Reason for Deletion
        do {
            System.out.print("Reason for Deletion: ");
            delReason = GeneralObject.scanDelReason.nextLine();

            if (delReason.isEmpty()) {
                System.out.print("\nPlease enter an input.\n");
            }
        } while (delReason.isEmpty());

        // Delete record confirmation
        do {
            System.out.print("\nDelete Patient Record? This action cannot be undone. [Y/N]: ");
            choice = GeneralObject.scanChoice.next().charAt(0);
            if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                System.out.print("\nInvalid input.");
            }
        } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

        if (choice == 'Y' || choice == 'y') {
            try {
                File patFile = new File("Patients.txt");
                File tempFile = new File("temp.txt");
                Scanner patReader = new Scanner(patFile);
    
                char temp[] = new char[30];  // temporary char array
                
                int lineCtr = 0;  // line counter
                int i = 0;
                int j = 0;

                String patUID = "";
                String firstName = "";
                String lastName = "";
                String middleName = "";
                long birthday = 0;  // YYYYMMDD
                char gender = '\0';
                String address = "";
                long phoneNo = 0;
                long nationalIdNo = 0;
    
                while (patReader.hasNextLine()) {
                    String data = patReader.nextLine();

                    if (lineCtr == index) {          
                        int semiCtr = 0;  // semi-colon counter

                        for (char c : data.toCharArray()) {
                            if (c == ';') {

                                // Patient's UID
                                if (semiCtr == 0) {
                                    patUID = GeneralObject.convertCharToString(temp);
        
                                // First Name
                                } else if (semiCtr == 1) {
                                    firstName = GeneralObject.convertCharToString(temp);
        
                                // Last Name
                                } else if (semiCtr == 2) {
                                    lastName = GeneralObject.convertCharToString(temp);
        
                                // Middle Name
                                } else if (semiCtr == 3) {
                                    middleName = GeneralObject.convertCharToString(temp);
        
                                // Birthday
                                } else if (semiCtr == 4) {
                                    birthday = Long.parseLong(GeneralObject.convertCharToString(temp));
        
                                // Gender
                                } else if (semiCtr == 5) {
                                    gender = temp[0];
                                
                                // Address
                                } else if (semiCtr == 6) {
                                    address = GeneralObject.convertCharToString(temp);
                                
                                // Phone Number
                                } else if (semiCtr == 7) {
                                    phoneNo = Long.parseLong(GeneralObject.convertCharToString(temp));
        
                                // National ID Number
                                } else if (semiCtr == 8) {
                                    nationalIdNo = Long.parseLong(GeneralObject.convertCharToString(temp));
                                }
        
                                // reinitialize temp
                                for (j = 0; j < temp.length; j++) {
                                    temp[j] = ' ';
                                }
        
                                semiCtr += 1;  // increment semi-colon counter
                                i = 0;  // reset index
        
                            } else {
                                temp[i] = c;
                                i++;
                            }
                        }
                        
                        try {
                            FileWriter myWriter = new FileWriter("temp.txt", true);
                            myWriter.write(patUID + ";" + firstName + ";" + lastName + ";" + middleName + ";" + birthday + ";" + gender + ";" + address + ";" + phoneNo + ";" + nationalIdNo + ";D;" + delReason + ";\n");
                            myWriter.close();
                        } catch (IOException e) {
                            System.out.print("An error occurred.");
                            e.printStackTrace();
                        }
                        
                    } else {
                        try {
                            FileWriter myWriter = new FileWriter("temp.txt", true);
                            myWriter.write(data + "\n");
                            myWriter.close();
                        } catch (IOException e) {
                            System.out.print("An error occurred.");
                            e.printStackTrace();
                        }
                    }

                    lineCtr++;
                }
    
                patReader.close();
                
                // Replace original file with temp file
                patFile.delete();
                tempFile.renameTo(patFile);
                
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            
            patients.get(index).setDelIndicator('D');
            patients.get(index).setDelReason(delReason);

            System.out.print("\nData of Patient " + patients.get(index).getPatUID() + " has been deleted.\n");
        }  // Else return to main menu
    }

    // Search Patient
    public static void searchPatient(ArrayList<Patient> patients, ArrayList<Request> requests, int index) {
        int choice;
        int ctr = 0;
        String reqUID;
        int indexReq = -1;
        ArrayList<String> tempArr = new ArrayList<String>();

        System.out.print("\nPatient's UID:\t\t" + patients.get(index).getPatUID() + "\n");
        System.out.print("Name:\t\t\t" + patients.get(index).getLastName() + ", " + patients.get(index).getFirstName() + " " + patients.get(index).getMiddleName() + "\n");
        System.out.print("Birthday:\t\t" + patients.get(index).getBirthday() + "\n");
        System.out.print("Address:\t\t" + patients.get(index).getAddress() + "\n");
        System.out.print("Phone Number:\t\t" + patients.get(index).getPhoneNo() + "\n");
        System.out.print("National Id No.:\t" + patients.get(index).getNationalIdNo() + "\n");
        
        for (int i = 0; i < requests.size(); i++) {
            if (patients.get(index).getPatUID().equals(requests.get(i).getPatUID()) && requests.get(i).getDelIndicator() != 'D'){
                if (ctr < 1) {
                    System.out.print("\nRequest's UID\t\tLab Test Type\t\t\tRequest Date\t\tResult\n");
                }
                tempArr.add(new String (requests.get(i).getReqUID() + "\t\t" + requests.get(i).getTestType()+ " \t\t" + requests.get(i).getReqDate() + "\t\t" + requests.get(i).getResult()));
                ctr++;
            }
        }
        Collections.sort(tempArr, Collections.reverseOrder());

        for (int k = 0; k < ctr; k++) {
            System.out.println(tempArr.get(k));
        }

        if (ctr < 1){
            System.out.println("\nNo record found");
        }

        do {
            System.out.print("\nDo you want to print a laboratory test result? [Y/N]: ");
            choice = GeneralObject.scanChoice.next().charAt(0);
            if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                System.out.print("\nInvalid input.");
            }
        } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

        if (choice == 'Y' || choice == 'y') {
            do {
                // Enter Request's UID
                do {
                    System.out.print("\nEnter Request's UID: ");
                    reqUID = scanRUID.nextLine();

                    if (reqUID.isEmpty()) {
                        System.out.print("\nPlease enter an input.");
                    }
                } while (reqUID.isEmpty());
                
                for (int l = 0; l < requests.size(); l++) {
                    if (reqUID.equals(requests.get(l).getReqUID())) {
                        indexReq = l;
                    }
                }

                System.out.print("\n");
                try {
                    
                    // Determines age of patient
                    int age;

                    LocalDateTime date = java.time.LocalDateTime.now();
                    String dateString = date.toString();

                    int birthday = (int) patients.get(index).getBirthday();

                    int year = birthday / 10000;
                    int month = (birthday / 100) % 100;
                    int day = birthday % 100;

                    int nowYear = Integer.valueOf(dateString.substring(0,4));
                    int nowMonth = Integer.valueOf(dateString.substring(5,7));
                    int nowDay = Integer.valueOf(dateString.substring(8, 10));

                    age = nowYear - year;

                    if (age > 0) {
                        if (nowMonth < month) {
                            age--;
                        }

                        else if (nowMonth == month) {
                            if (nowDay < day) {
                                age--;
                            }
                        }
                    }
                    
                    // Chooses location of printing of pdf
                    String fileName = patients.get(index).getLastName() + "_" + requests.get(indexReq).getReqUID() + "_" + requests.get(indexReq).getReqDate() + ".pdf";
                    
                    Document pdf = new Document(PageSize.A4);  // creation of document(pdf). A4 size is 595 x 842
                    PdfWriter.getInstance(pdf, new FileOutputStream(fileName));  // writes to document

                    pdf.open(); 
                    
                    // Creating of a line object
                    LineSeparator line = new LineSeparator();
 
                    // Inserting image to pdf
                    Image pic = Image.getInstance("logo.png");  // gets image
                    pic.scaleToFit(100f, 100f);  // changes size
                    pic.setAbsolutePosition((float) 247.5, 801);  // changes position
                    pdf.add(pic); // adds image

                    // Creating text
                    Paragraph address = new Paragraph("\n279 E Rodriguez Sr. Ave. Quezon City, 1112\n");
                    Paragraph number = new Paragraph("+63-2-8723-0101\n\n\n");

                    // Aligns text to center
                    number.setAlignment(Element.ALIGN_CENTER);
                    address.setAlignment(Element.ALIGN_CENTER);

                    // Adds text
                    pdf.add(address);
                    pdf.add(number);

                    // Creates line divison
                    pdf.add(line);

                    // Creation of second table
                    PdfPTable tableInfo = new PdfPTable(2);
                    PdfPCell cellInfo = new PdfPCell(new Phrase("Name: " + "\t" +  patients.get(index).getLastName() + ", " + patients.get(index).getFirstName() 
                                                    +  " " + patients.get(index).getMiddleName()) );  // creating of cell with phrase

                    tableInfo.setWidthPercentage(100);  // changes size of table
                    cellInfo.setBorder(Rectangle.NO_BORDER);  // creates table without borders
                    tableInfo.addCell(cellInfo);  // adds cellInfo into first cell of the table

                    // Sets cell Info into respective data then adds it
                    cellInfo.setPhrase(new Phrase("Specimen ID: " + requests.get(indexReq).getReqUID()));
                    tableInfo.addCell(cellInfo);
                  
                    cellInfo.setPhrase(new Phrase("Patient ID: " + requests.get(indexReq).getPatUID()));
                    tableInfo.addCell(cellInfo);

                    cellInfo.setPhrase(new Phrase("Collection Date: " + requests.get(indexReq).getReqDate()));
                    tableInfo.addCell(cellInfo);
                    
                    cellInfo.setPhrase(new Phrase("Age: " + age));
                    tableInfo.addCell(cellInfo);
                    
                    cellInfo.setPhrase(new Phrase("Birthday: " + patients.get(index).getBirthday()));
                    tableInfo.addCell(cellInfo);
                    
                    cellInfo.setPhrase(new Phrase("Gender: " + patients.get(index).getGender()));
                    tableInfo.addCell(cellInfo);
                    
                    cellInfo.setPhrase(new Phrase("Phone number: " + patients.get(index).getPhoneNo()));
                    tableInfo.addCell(cellInfo);

                    pdf.add(tableInfo);  // adds table
                    
                    pdf.add(line);  // adds line divison

                    pdf.add(Chunk.NEWLINE);  // adds space

                    // Creation of second table
                    PdfPTable tableTest = new PdfPTable(2);
                    Font boldText = new Font(FontFamily.HELVETICA, 12, Font.BOLD);  // creates a bold font
                    PdfPCell cellTest = new PdfPCell(new Phrase("Test", boldText));

                    tableTest.setWidthPercentage(100);
                    tableTest.addCell(cellTest);

                    cellTest.setPhrase(new Phrase("Result", boldText));
                    tableTest.addCell(cellTest);

                    cellTest.setPhrase(new Phrase(requests.get(indexReq).getTestType()));
                    tableTest.addCell(cellTest);

                    cellTest.setPhrase(new Phrase(requests.get(indexReq).getResult()));
                    tableTest.addCell(cellTest);

                    pdf.add(tableTest);
                    pdf.add(Chunk.NEWLINE);
                    pdf.add(Chunk.NEWLINE);
                    pdf.add(Chunk.NEWLINE);

                    pdf.add(line);

                    pdf.add(Chunk.NEWLINE);
                    pdf.add(Chunk.NEWLINE);

                    // Creation of third table
                    PdfPTable tableStaff = new PdfPTable(2);
                    PdfPCell cellStaff = new PdfPCell(new Phrase("Jane Doe"));

                    tableStaff.setWidthPercentage(100);
                    cellStaff.setBorder(Rectangle.NO_BORDER);
                    tableStaff.addCell(cellStaff);

                    cellStaff.setPhrase(new Phrase("John Roe"));
                    tableStaff.addCell(cellStaff);

                    cellStaff.setPhrase(new Phrase("Medical Technologist"));
                    tableStaff.addCell(cellStaff);

                    cellStaff.setPhrase(new Phrase("Pathologist"));
                    tableStaff.addCell(cellStaff);

                    cellStaff.setPhrase(new Phrase("Lic. #123456789"));
                    tableStaff.addCell(cellStaff);

                    cellStaff.setPhrase(new Phrase("Lic. # 987654321"));
                    tableStaff.addCell(cellStaff);
                    
                    pdf.add(tableStaff);

                    pdf.close();  // closes pdf

                    File file = new File(fileName);
                    String path = file.getAbsoluteFile().getParent();

                    System.out.println(fileName + " has been saved to " + path + ".");
                } catch (Exception e) {
                    System.out.println("No record found.");
                }

                // Repeat prompt
                do {
                    System.out.print("Do you want to print another laboratory test result? [Y/N]: ");
                    choice = GeneralObject.scanChoice.next().charAt(0);
                    if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                        System.out.print("\nInvalid input.");
                    }
                } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');
                
            } while (choice == 'Y' || choice == 'y'); 
        }  // Else return to main menu
    }
}
