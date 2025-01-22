import java.util.Scanner;
import java.util.ArrayList;
import java.util.Collections;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.nio.file.*;

public class ReqFunctions {
    // Request Scanners
    public static Scanner scanUID = new Scanner(System.in);
    public static Scanner scanPUID = new Scanner(System.in);
    public static Scanner scanRUID = new Scanner(System.in);
    public static Scanner scanServCode = new Scanner(System.in);
    public static Scanner scanResult = new Scanner(System.in);
    public static Scanner scanEdit = new Scanner(System.in);

    /* LAB REQUEST METHODS */
    // Read Lab Requests txt files
    public static void readReqFile(ArrayList<Service> services, ArrayList<Request> requests) {
        try {
            int servRepeat = 0;

            for (int k = 0; k < services.size(); k++) {
                File reqFile = new File(services.get(k).getServCode() + "_Requests.txt");

                for (int j = 0; j < k; j++) {
                    if (services.get(k).getServCode().equals(services.get(j).getServCode())) {
                        servRepeat = 1;
                    }
                }

                if (reqFile.exists() && servRepeat != 1) {

                    Scanner reqReader = new Scanner(reqFile);

                    char temp[] = new char[30];  // temporary char array
                    
                    int i = 0;
                    int j = 0;

                    while (reqReader.hasNextLine()) {
                        String data = reqReader.nextLine();

                        String reqUID = "";
                        String patUID = "";
                        long reqDate = 0;
                        long reqTime = 0;
                        String result = "";
                        char delIndicator = '\0';
                        String delReason = "";
                        String servCode = services.get(k).getServCode();
                        String testType = services.get(k).getDescription();

                        int semiCtr = 0;  // semi-colon counter

                        for (char c : data.toCharArray()) {
                            if (c == ';') {

                                // Request's UID
                                if (semiCtr == 0) {
                                    reqUID = GeneralObject.convertCharToString(temp);

                                // Patient's UID
                                } else if (semiCtr == 1) {
                                    patUID = GeneralObject.convertCharToString(temp);

                                // Request Date
                                } else if (semiCtr == 2) {
                                    reqDate = Long.parseLong(GeneralObject.convertCharToString(temp));

                                // Request Time
                                } else if (semiCtr == 3) {
                                    reqTime = Long.parseLong(GeneralObject.convertCharToString(temp));

                                // Result
                                } else if (semiCtr == 4) {
                                    result = GeneralObject.convertCharToString(temp);

                                // Delete Indicator
                                } else if (semiCtr == 5) {
                                    delIndicator = temp[0];
                                
                                // Delete Reason
                                } else if (semiCtr == 6) {
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
                        requests.add(new Request(reqUID, patUID, servCode, testType, reqDate, reqTime, result, delIndicator, delReason));
                    }
                    reqReader.close();
                }
            }
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }
    
    // Generate Request's UID
    private static String generateRUID(String servCode) throws IOException {
        String reqUID;

        LocalDateTime date = java.time.LocalDateTime.now();
        String dateString = date.toString();

        File fileUID = new File(servCode + "_Requests.txt");
        Path pathUID = Paths.get(servCode + "_Requests.txt");

        // Gets respective month, year and day
        String month = dateString.substring(5,7); 
        String year = dateString.substring(0,4);
        String day = dateString.substring(8, 10);

        String letters= "AA";  // letter code
        String numbers = "00";  // number code

        if (fileUID.length() != 0) {  // checks if file has contents

            int tempNumbers;

            int size = (int) Files.lines(pathUID).count();  // number of lines in the file
            
            String line = Files.readAllLines(Paths.get(servCode + "_Requests.txt")).get(size - 1);  // gets the last line
            
            String day1 = line.substring(9, 11);  // gets the day of the last data

            if (fileUID.length() == 1) {

                if (!(day.equals(day1))) {
                    letters= "AA";  // letter code
                    numbers = "00";  // number code
                } else {
                    numbers = "01";
                }
            }

            else {

                // Checks if it a new day and resets the UID
                if (!(day.equals(day1))) { 
                    letters= "AA";  // letter code
                    numbers = "00";  // number code

                // Adjusts letters and numbers portion of UID
                } else {
                    letters = line.substring(11, 13);
                    numbers = line.substring(13, 15);

                    char [] arrayLetters = letters.toCharArray();

                    // Checks if number is 99
                    if (numbers.equals("99")) {

                        numbers = "00";

                        if (arrayLetters[1] == 'Z') {
                            arrayLetters[1] = 'A';
                            arrayLetters[0] += 1;
                        } else {
                            arrayLetters[1] += 1;
                        }
                    }

                    // If number is not 99
                    else {
                        tempNumbers = Integer.parseInt(numbers);
                        tempNumbers++;
                        numbers = String.valueOf(tempNumbers);
                        
                        // Adds a 0 in front of numbers since initially it will only be 0, 1, 2 and not 00, 01, 02
                        if (tempNumbers < 10) {
                            numbers = "0" + numbers;  
                        }
                    }
                    letters = String.valueOf(arrayLetters);
                }
            }
        }

        reqUID = (servCode + year + month + day + letters + numbers);
        return reqUID;
    }

    // Add Lab Request
    public static int addRequest(ArrayList<Patient> patients, ArrayList<Service> services, ArrayList<Request> requests) throws IOException {
        char delIndicator;
        String delReason;
        String reqUID;
        String servCode;
        String testType = "";
        long reqDate;  // YYYYMMDD
        long reqTime;  // HHMM
        String result;
        String patUID;

        int found;
        char choice;

        delIndicator = ' ';  // empty
        delReason = "";  // empty

        do {
            found = 0;

            // Patient's UID
            do {
                System.out.print("\nPatient's UID: ");
                patUID = scanPUID.nextLine();

                if (patUID.isEmpty()) {
                    System.out.print("\nPlease enter an input.");
                }
            } while (patUID.isEmpty());

            // Service Code
            do {
                System.out.print("Service Code: ");
                servCode = scanServCode.nextLine();

                if (servCode.isEmpty()) {
                    System.out.print("\nPlease enter an input.\n");
                }
            } while (servCode.isEmpty());

            for ( int i = 0; i < patients.size(); i++) {
                if (patients.get(i).getPatUID().equals(patUID) && patients.get(i).getDelIndicator() != 'D') {
                    found += 1;
                }
            }

            for ( int i = 0; i < services.size(); i++) {
                if (services.get(i).getServCode().equals(servCode) && services.get(i).getDelIndicator() != 'D') {
                    testType = services.get(i).getDescription();
                    found += 1;
                }
            }

            if (found == 2) {
                choice = 'N';
            } else {
                System.out.print("\nNo record found.");

                // Repeat prompt
                do {
                    System.out.print("\nSearch again? [Y/N]: ");
                    choice = GeneralObject.scanChoice.next().charAt(0);

                    if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                        System.out.print("\nInvalid input.");
                    }
                } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');
            }
        } while (choice == 'Y' || choice == 'y');

        if (found == 2) {
            // Gets system date and time
            LocalDateTime myDate = java.time.LocalDateTime.now();
            DateTimeFormatter myFormatDate = DateTimeFormatter.ofPattern("yyyyMMdd");
            DateTimeFormatter myFormatTime = DateTimeFormatter.ofPattern("HHmm");

            String formattedDate = myDate.format(myFormatDate);
            String formattedTime = myDate.format(myFormatTime);

            reqUID = generateRUID(servCode);  // generates Request's UID
            reqDate = Long.parseLong(formattedDate);
            reqTime = Long.parseLong(formattedTime);
            
            do {
                System.out.print("Result: ");
                result = scanResult.nextLine();

                if (result.isEmpty()) {
                    System.out.print("\nPlease enter an input.\n");
                }
            } while (result.isEmpty());

            // Save record confirmation
            do {
                System.out.print("\nSave Laboratory Request? [Y/N]: ");
                choice = GeneralObject.scanChoice.next().charAt(0);   
                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                    System.out.print("\nInvalid input.");
                }
            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

            if (choice == 'Y' || choice == 'y') {
                try {
                    FileWriter myWriter = new FileWriter(servCode + "_Requests.txt", true);
                    myWriter.write(reqUID + ";" + patUID + ";" + reqDate + ";" + reqTime + ";" + result + ";\n");
                    myWriter.close();
                    requests.add(new Request(reqUID, patUID, servCode, testType, reqDate, reqTime, result, delIndicator, delReason));
                } catch (IOException e) {
                    System.out.print("An error occurred.");
                    e.printStackTrace();
                }
                
                System.out.print("\nLaboratory Request " + reqUID + " has been added to file " + servCode + "_Requests.txt.\n");
                
                return 1;
            }
        }
        
        return -1;
    }

    // Edit Lab Request
    public static void editRequest(ArrayList<Service> services, ArrayList<Request> requests, int index) {
        int l;
        int m;
        int choice = 0;

        String reqServCode="";
        String tempReqUID;
        
        String editedResult = "";
        String reqFilePath = "";

        tempReqUID = requests.get(index).getReqUID(); 

        for (l = 0; l < requests.size(); l++) {
            if (requests.get(l).getReqUID().equals(tempReqUID)) {  // checks if temp is matches any data
                reqServCode = requests.get(l).getServCode();

                // Result
                do {
                    System.out.print("\nNew Laboratory Result: ");
                    editedResult = scanEdit.nextLine();  // new result
                    if (editedResult.isEmpty()) {
                        System.out.print("\nPlease enter an input.");
                    }
                } while (editedResult.isEmpty());

                // Update record confirmation
                do {
                    System.out.print("\nUpdate Request Record? [Y/N]: ");
                    choice = GeneralObject.scanChoice.next().charAt(0);   
                    if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                        System.out.print("\nInvalid input.");
                    }
                } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

                if (choice == 'Y' || choice == 'y') {
                    requests.get(l).setResult(editedResult);  // changes result
                    reqFilePath = reqServCode + "_Requests.txt";
                }
            }
        }

        if (choice == 'Y' || choice == 'y') {
            // This portion deletes a txt file such as CAT_Requests.txt file and adds the exact same data but with the new result
            
            File File = new File(reqFilePath);
            File.delete();

            try {
                try (FileWriter myWriter = new FileWriter(reqServCode + "_Requests.txt", true)) {
                    for (m = 0; m < requests.size(); m++) {
                        if (reqServCode.equals(requests.get(m).getServCode())) {  // checks if servCode matches data
                            // Checks if it is deleted then adds appropriate data
                            if (requests.get(m).getDelIndicator() == 'D' || requests.get(m).getDelReason() != "") { 
                            myWriter.write(requests.get(m).getReqUID() + ";" + requests.get(m).getPatUID() + ";" + requests.get(m).getReqDate() + ";" 
                            + requests.get(m).getReqTime() + ";" + requests.get(m).getResult() + ";" + requests.get(m).getDelIndicator() + ";" + requests.get(m).getDelReason() + ";\n" );
                            }

                            // Checks if it is NOT deleted then adds appropriate data
                            else {
                                myWriter.write(requests.get(m).getReqUID() + ";" + requests.get(m).getPatUID() + ";" + requests.get(m).getReqDate() + ";" 
                            + requests.get(m).getReqTime() + ";" + requests.get(m).getResult() + ";\n");
                            }
                        }
                    }
                    myWriter.close();
                    System.out.println("\nThe Laboratory Request " + tempReqUID + " has been updated.");
                }
            } catch (IOException e) {
                System.out.print("An error occurred.");
                e.printStackTrace();
            }
        }
    }

    // Delete Lab Request
    public static void deleteRequest(ArrayList<Request> requests, int index) {
        String delReason;
        int choice;

        // Reason for Deletion
        do {
            System.out.print("\nReason for Deletion: ");
            delReason = GeneralObject.scanDelReason.nextLine();

            if (delReason.isEmpty()) {
                System.out.print("\nPlease enter an input.");
            }
        } while (delReason.isEmpty());

        // Delete record confirmation
        do {
            System.out.print("\nDelete Lab Request Record? This action cannot be undone. [Y/N]: ");
            choice = GeneralObject.scanChoice.next().charAt(0);
            if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                System.out.print("\nInvalid input.");
            }
        } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

        if (choice == 'Y' || choice == 'y') {
            try {
                File reqFile = new File(requests.get(index).getServCode() + "_Requests.txt");
                File tempFile = new File("temp.txt");
                Scanner reqReader = new Scanner(reqFile);
    
                char temp[] = new char[30];  // temporary char array
                
                int lineCtr = 0;  // line counter
                int i = 0;
                int j = 0;

                int k = index;
                int codeCtr = 0;  // counts the number of preceding records with the same service code as the record being deleted
                int found = 0;

                // Search for the index of the record in its corresponding Request txt file
                while (k > 0 && found == 0) {
                    k--;

                    if (requests.get(index).getServCode().equals(requests.get(k).getServCode())) {
                        codeCtr++;
                    } else {
                        found = 1;
                    }  
                }
    
                while (reqReader.hasNextLine()) {
                    String data = reqReader.nextLine();

                    String reqUID = "";
                    String patUID = "";
                    long reqDate = 0;
                    long reqTime = 0;
                    String result = "";

                    if (lineCtr == codeCtr) {          
                        int semiCtr = 0;  // semi-colon counter

                        for (char c : data.toCharArray()) {
                            if (c == ';') {

                                // Request's UID
                            if (semiCtr == 0) {
                                reqUID = GeneralObject.convertCharToString(temp);

                            // Patient's UID
                            } else if (semiCtr == 1) {
                                patUID = GeneralObject.convertCharToString(temp);

                            // Request Date
                            } else if (semiCtr == 2) {
                                reqDate = Long.parseLong(GeneralObject.convertCharToString(temp));

                            // Request Time
                            } else if (semiCtr == 3) {
                                reqTime = Long.parseLong(GeneralObject.convertCharToString(temp));

                            // Result
                            } else if (semiCtr == 4) {
                                result = GeneralObject.convertCharToString(temp);
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
                            myWriter.write(reqUID + ";" + patUID + ";" + reqDate + ";" + reqTime + ";" + result + ";D;" + delReason + ";\n");
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
                
                reqReader.close();
                
                // Replace original file with temp file
                reqFile.delete();
                tempFile.renameTo(reqFile);               
                
            } catch (FileNotFoundException e) {
                System.out.println("An error occurred.");
                e.printStackTrace();
            }
            
            requests.get(index).setDelIndicator('D');
            requests.get(index).setDelReason(delReason);
            
            System.out.print("\n" + requests.get(index).getReqUID() + " has been deleted.\n");
        }  // Else return to main menu
    }

    // Search Lab Request
    public static int searchRequest(ArrayList<Patient> patients, ArrayList<Service> services, ArrayList<Request> requests) {
        String UID;

        int i;
        int j;
        int k; 
        int ctr;
        int index = -1;

        int tempIndex;
        int tempUID;

        ctr = 0;

        // Enter Request's UID or Patient's UID
        do {
            System.out.print("\nEnter the Request's UID or Patient's UID: ");
            UID = scanUID.nextLine();

            if (UID.isEmpty()) {
                System.out.print("\nPlease enter an input.");
            }
        } while (UID.isEmpty());

        ArrayList<String> tempArr = new ArrayList<String>();
        ArrayList<Integer> indexArr = new ArrayList<Integer>();

        for (i = 0; i < requests.size(); i++) {

            // Checks if entered UID matches data
            if ((UID.equals(requests.get(i).getReqUID()) || UID.equals(requests.get(i).getPatUID())) && requests.get(i).getDelIndicator() != 'D') { 
                if (ctr < 1) {
                    System.out.print("\nRequest's UID\t\tLab Test Type\t\tRequest Date\t\tResult" + "\n");
                }

                indexArr.add(i);

                ctr++;  // checks number of matches
                index = i;
            }
        }
        
        // Sorts arraylist
        Collections.sort(tempArr, Collections.reverseOrder());

        for (j = 0; j < indexArr.size(); j++) {
            for (k = j + 1; k < indexArr.size(); k++) {
                if (requests.get(indexArr.get(j)).getReqDate() < requests.get(indexArr.get(k)).getReqDate()) {
                        tempIndex = indexArr.get(j);
                        indexArr.set(j, indexArr.get(k));
                        indexArr.set(k, tempIndex);
                }
            }
        }

        for (j = 0; j < indexArr.size(); j++) {
            for (k = j + 1; k < indexArr.size(); k++) {
                if (requests.get(indexArr.get(j)).getReqDate()  == requests.get(indexArr.get(k)).getReqDate()) {
                    if (requests.get(indexArr.get(j)).getReqUID().compareTo(requests.get(indexArr.get(k)).getReqUID()) < 0) {
                        tempUID = indexArr.get(j);
                        indexArr.set(j, indexArr.get(k));
                        indexArr.set(k, tempUID);
                    }
                }
            }
        }

        // Prints arraylist
        for (k = 0; k < ctr; k++) {
            int ind = indexArr.get(k);

            System.out.println(requests.get(ind).getReqUID() + "\t\t" + requests.get(ind).getTestType() + " \t" 
                              + requests.get(ind).getReqDate() + "\t\t" + requests.get(ind).getResult());
        }

        if (ctr < 1) {
            System.out.print("\nNo record found.");
            return index; // returns if no matches
        } else if (ctr == 1) {
            return index; // returns index of data in requests array if there is only 1 match
        } else {
            ctr = -ctr;
            return ctr; // Returns a negative number of matches (This will be used in selectRequest)
                        // Negative number of matches so that it does not overlap with any other index options
                        // Smallest value of ctr would be 2
        }
    }
    
    // Select Lab Request
    public static int selectRequest(ArrayList<Patient> patients, ArrayList<Service> services, ArrayList<Request> requests) {
        int count = searchRequest(patients, services, requests);  // gets value of searchRequest

        int i;
        int temp = count;

        String choice;

        // This portion asks user to input RUID if there was more than 1 match in searchRequest
        if (count < -1) {
            do {
                System.out.print("\nEnter the Request's UID: ");
                choice = scanResult.nextLine();
    
                if (choice.isEmpty()) {
                    System.out.print("\nPlease enter an input.");
                }
            } while (choice.isEmpty());

             for (i = 0; i < requests.size(); i++) {
                if (choice.equals(requests.get(i).getReqUID())) {
                    count = i;
                }
             }

             if (count == temp) {
                System.out.print("\nNo record found.");
                count = -1;
             }

        }

        return count; // returns index of wanted data
    }
}
