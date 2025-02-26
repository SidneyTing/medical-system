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
import InputHandler.*;
import Model.GeneralObject;
import Model.Patient;
import Model.Request;
import SelectionStrategy.PatientSelectionContext;
import SelectionStrategy.PatientSelectionStrategy;
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
    public static Scanner scanner = new Scanner(System.in);

    private static AbstractInputHandler textHandler = new TextInputHandler();
    private static AbstractInputHandler yesNoHandler = new YesNoInputHandler();
    private static AbstractInputHandler longHandler = new LongInputHandler();
    private static AbstractInputHandler maleFemaleHandler = new MaleFemaleInputHandler();




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
                String birthday = "";  // YYYYMMDD
                String gender = "";
                String address = "";
                String phoneNo = "";
                String nationalIdNo = "";
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
                            birthday = GeneralObject.convertCharToString(temp);

                        // Gender
                        } else if (semiCtr == 5) {
                            gender = GeneralObject.convertCharToString(temp);
                        
                        // Address
                        } else if (semiCtr == 6) {
                            address = GeneralObject.convertCharToString(temp);
                        
                        // Phone Number
                        } else if (semiCtr == 7) {
                            phoneNo = GeneralObject.convertCharToString(temp);

                        // National ID Number
                        } else if (semiCtr == 8) {
                            nationalIdNo = GeneralObject.convertCharToString(temp);

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
        String birthday;  // YYYYMMDD
        String gender;
        String address;
        String phoneNo;
        String nationalIdNo;
        char delIndicator = '\0';
        String delReason = "";

        String choice;

        do {
            patUID = generatePUID(patients);  // generates Patient's UID

            // First Name
            System.out.println("\nFirst Name");
            firstName = textHandler.requestInput();

            // Last Name
            System.out.println("\nLast Name");
            lastName = textHandler.requestInput();

            // Middle Name
            System.out.println("\nMiddle Name");
            middleName = textHandler.requestInput();

            // Birthday
            System.out.println("\nBirthday (YYYYMMDD)");
            birthday = longHandler.requestInput();
            
            // Gender
            System.out.println("\nGender");
            gender = maleFemaleHandler.requestInput().substring(0, 1).toUpperCase();  // auto capitalization

            // Address
            System.out.println("\nAddress");
            address = textHandler.requestInput();

            // Phone No.
            System.out.println("\nPhone No.");
            phoneNo = longHandler.requestInput();

            // National ID No.
            System.out.println("\nNational ID No.");
            nationalIdNo = longHandler.requestInput();

            // Save record confirmation
            System.out.println("\nSave Patient Record? [Y/N]");
            choice = yesNoHandler.requestInput();

            if (choice.equalsIgnoreCase("Y")) {
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
            System.out.println("\nDo you want to add another patient? [Y/N]");
            choice = yesNoHandler.requestInput();
        } while (choice.equalsIgnoreCase("Y"));
    }

    // Select Patient
    public static int selectPatient(ArrayList<Patient> patients) {
        String choice;
        String patUID;
        String firstName;
        String lastName;
        String birthday;  // YYYYMMDD
        String nationalIdNo;

        PatientSelectionContext context = new PatientSelectionContext();

        do {
            System.out.print("\nSelect a Patient:\n[1] Use Patient's UID\n[2] Use Last Name, First Name, Birthday\n[3] Use National ID No.\n[X] Return to Main Menu\n\nSelect an option: ");
            choice = scanner.next().substring(0, 1);

            while (!choice.equals("1") && !choice.equals("2") && !choice.equals("3") && !choice.equalsIgnoreCase("X")) {
                System.out.print("\nInvalid input.\nPlease select an option: ");
                choice = scanner.next().substring(0, 1);
            }

            if (choice.equalsIgnoreCase("X")) {
                return -1;
            }

            context.setStrategy(choice);
            int result = context.execute(patients);
            if (result != -1) {
                return result;
            }

            // No record found
            System.out.println("\nNo record found.");

            // Repeat prompt
            System.out.println("\nSearch again? [Y/N] ");
            choice = yesNoHandler.requestInput();
        } while (choice.equalsIgnoreCase("Y"));

        return -1;
    }

    // Edit Patient
    public static int editPatient(ArrayList<Patient> patients, int index) {
        String choice;
        String yn;
        String address = "";
        String phoneNo = "";

        System.out.print("\nPatient Update Options:\n[1] Patient's Address\n[2] Patient's Phone No.\n[X] Return to Main Menu\n\nSelect an update option: ");
        choice = scanner.next().substring(0, 1);

        while (!choice.equals("1") && !choice.equals("2") && !choice.equalsIgnoreCase("X")) {
            System.out.print("\nInvalid input.\nPlease select an update option: ");
            choice = scanner.next().substring(0, 1);
        }
        
        // Patient's Address
        if (choice.equals("1")) {
            System.out.println("\nNew Patient's Address: ");
            address = textHandler.requestInput();
        // Phone No.
        } else if (choice.equals("2")) {
            System.out.println("\nNew Patient's Phone No.");
            phoneNo = longHandler.requestInput();
        } else {
            return -1;
        }

        // Update record confirmation
        System.out.println("\nUpdate Patient Record? [Y/N] ");
        yn = yesNoHandler.requestInput();

        if (yn.equalsIgnoreCase("Y")) {
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
                String birthday = "";  // YYYYMMDD
                String gender = "";
                String temp_address = "";
                String temp_phoneNo = "";
                String nationalIdNo = "";
    
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
                                    birthday = GeneralObject.convertCharToString(temp);
        
                                // Gender
                                } else if (semiCtr == 5) {
                                    gender = GeneralObject.convertCharToString(temp);
                                
                                // Address
                                } else if (semiCtr == 6) {
                                    temp_address = GeneralObject.convertCharToString(temp);
                                
                                // Phone Number
                                } else if (semiCtr == 7) {
                                    temp_phoneNo = GeneralObject.convertCharToString(temp);
        
                                // National ID Number
                                } else if (semiCtr == 8) {
                                    nationalIdNo = GeneralObject.convertCharToString(temp);
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
                        if (choice.equals("1")) {
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

            if (choice.equals("1")) {
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
        String choice;

        // Reason for Deletion
        System.out.println("\nReason for Deletion: ");
        delReason = textHandler.requestInput();

        // Delete record confirmation
        System.out.println("\nDelete Patient Record? This action cannot be undone. [Y/N] ");
        choice = yesNoHandler.requestInput();

        if (choice.equalsIgnoreCase("Y")) {
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
                String birthday = "";  // YYYYMMDD
                String gender = "";
                String address = "";
                String phoneNo = "";
                String nationalIdNo = "";
    
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
                                    birthday = GeneralObject.convertCharToString(temp);
        
                                // Gender
                                } else if (semiCtr == 5) {
                                    gender = GeneralObject.convertCharToString(temp);
                                
                                // Address
                                } else if (semiCtr == 6) {
                                    address = GeneralObject.convertCharToString(temp);
                                
                                // Phone Number
                                } else if (semiCtr == 7) {
                                    phoneNo = GeneralObject.convertCharToString(temp);
        
                                // National ID Number
                                } else if (semiCtr == 8) {
                                    nationalIdNo = GeneralObject.convertCharToString(temp);
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
        String choice;
        int ctr = 0;
        String reqUID;
        ArrayList<String> tempArr = new ArrayList<>();

        System.out.println("\nPatient's UID:\t\t" + patients.get(index).getPatUID());
        System.out.println("Name:\t\t\t" + patients.get(index).getLastName() + ", " + patients.get(index).getFirstName() + " " + patients.get(index).getMiddleName());
        System.out.println("Birthday:\t\t" + patients.get(index).getBirthday());
        System.out.println("Address:\t\t" + patients.get(index).getAddress());
        System.out.println("Phone Number:\t\t" + patients.get(index).getPhoneNo());
        System.out.println("National Id No.:\t" + patients.get(index).getNationalIdNo());
        
        for (int i = 0; i < requests.size(); i++) {
            if (patients.get(index).getPatUID().equals(requests.get(i).getPatUID()) && requests.get(i).getDelIndicator() != 'D'){
                if (ctr < 1) {
                    System.out.println("\nRequest's UID\t\tLab Test Type\t\t\tRequest Date\t\tResult");
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
            System.out.println("\nNo laboratory records found.");
            
        } else {
            System.out.println("\nDo you want to print a laboratory test result? [Y/N] ");
            choice = yesNoHandler.requestInput();
    
            if (choice.equalsIgnoreCase("Y")) {
                do {
                    int indexReq = -1;
    
                    // Enter Request's UID
                    System.out.println("\nEnter Request's UID: ");
                    reqUID = textHandler.requestInput();
                    
                    for (int l = 0; l < requests.size(); l++) {
                        if (reqUID.equals(requests.get(l).getReqUID())) {
                            indexReq = l;
                        }
                    }
    
                    System.out.print("\n");
    
                    if (indexReq != -1) {
                        try {
                            // Determines age of patient
                            int age;
        
                            LocalDateTime date = java.time.LocalDateTime.now();
                            String dateString = date.toString();
        
                            int birthday = Integer.parseInt(patients.get(index).getBirthday());
        
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
                        
                    } else {
                        System.out.println("No record found.");
                    }
    
                    // Repeat prompt
                    System.out.println("\nDo you want to print another laboratory test result? [Y/N] ");
                    choice = yesNoHandler.requestInput();
                } while (choice.equalsIgnoreCase("Y"));
            }  // Else return to main menu
        }
    }
}
