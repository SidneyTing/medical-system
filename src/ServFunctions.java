import java.util.Scanner;
import java.util.ArrayList;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.FileNotFoundException;

public class ServFunctions {
    // Service Scanners
    public static Scanner scanCode = new Scanner(System.in);
    public static Scanner scanDescription = new Scanner(System.in);
    public static Scanner scanPrice = new Scanner(System.in);

    /* SERVICE METHODS */
    // Read Services txt file
    public static void readServFile(ArrayList<Service> services) {
        try {
            File servFile = new File("Services.txt");
            Scanner servReader = new Scanner(servFile);

            char temp[] = new char[30];  // temporary char array
            
            int i = 0;
            int j = 0;

            while (servReader.hasNextLine()) {
                String data = servReader.nextLine();

                String servCode = "";
                String description = "";
                float price = 0;
                char delIndicator = '\0';
                String delReason = "";

                int semiCtr = 0;  // semi-colon counter

                for (char c : data.toCharArray()) {
                    if (c == ';') {

                        // Service Code
                        if (semiCtr == 0) {
                            servCode = GeneralObject.convertCharToString(temp);

                        // First Name
                        } else if (semiCtr == 1) {
                            description = GeneralObject.convertCharToString(temp);

                        // Price
                        } else if (semiCtr == 2) {
                            price = Float.parseFloat(GeneralObject.convertCharToString(temp));

                        // Delete Indicator
                        } else if (semiCtr == 3) {
                            delIndicator = temp[0];
                        
                        // Delete Reason
                        } else if (semiCtr == 4) {
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

                services.add(new Service(servCode, description, price, delIndicator, delReason));
            }

            servReader.close();

        } catch (FileNotFoundException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    // Add Service
    public static void addService(ArrayList<Service> services) {
        String servCode;
        String description;
        float price = 0;
        char delIndicator = '\0';
        String delReason = "";
        
        int choice;
        int i;
        int count;

        String temp;
        int error = 0;

        do {  // checks if service code already exists by checking each servCode in the file
            count = 0;

            // Service Code
            System.out.print("\nService Code: ");
            servCode = scanCode.nextLine().toUpperCase();  // auto capitalization

            if (servCode.length() != 3) {
                System.out.print("\nInvalid input. Service Code must contain exactly three letters.");
                count = 1;
            }

            for (i = 0; i < services.size(); i++) {
                if (servCode.equals(services.get(i).getServCode())) {
                    System.out.println("Service Code already exists. Please enter a different Service Code.");
                    count = 1;
                }
            }
        } while (count == 1);

        // Description
        do {
            System.out.print("Description: ");
            description = scanDescription.nextLine();

            if (description.isEmpty()) {
                System.out.print("\nPlease enter an input.\n");
            }
        } while (description.isEmpty());

        // Price
        do {
            System.out.print("Price: ");
            temp = scanPrice.nextLine();

            try {
                price = Float.parseFloat(temp);
                error = 0;
            } catch (NumberFormatException e) {
                System.out.println("\nInvalid input. Must contain only digits.");
                error = 1;
            }
        } while (error == 1); 

        // Save record confirmation
        do {
            System.out.print("\nSave Laboratory Service? [Y/N]: ");
            choice = GeneralObject.scanChoice.next().charAt(0);
            if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                System.out.print("\nInvalid input.");
            }
        } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');
        
        if (choice == 'Y' || choice == 'y') {
            try {
                // Add Service record
                FileWriter myServWriter = new FileWriter("Services.txt", true);
                myServWriter.write(servCode + ";" + description + ";" + price + ";\n");
                myServWriter.close();
                services.add(new Service(servCode, description, price, delIndicator, delReason));
                
                // Create a Request file
                FileWriter myReqWriter = new FileWriter(servCode + "_Requests.txt", true);
                myReqWriter.close();
            } catch (IOException e) {
                System.out.print("An error occurred.");
                e.printStackTrace();
            }

            System.out.print("\n" + servCode + " " + description + " has been added.\n");
        }
    }

    // Edit Service
    public static int editService(ArrayList<Service> services) {
        int choice;

        System.out.print("\nServices cannot be edited.\nIf you would like to edit an existing service, this service will first be deleted, and a new service will be created.\nWould you like to proceed? [Y/N]: ");
        choice = GeneralObject.scanChoice.next().charAt(0);

        while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
            System.out.print("\nInvalid input.\nProceed to edit service? [Y/N]: ");
            choice = GeneralObject.scanChoice.next().charAt(0);
        }

        if (choice == 'Y' || choice == 'y') {
            System.out.print("\nDelete Service");

            deleteService(services);

            // Add service confirmation
            do {
                System.out.print("\nThe program will now proceed to add a service. Continue? [Y/N]: ");
                choice = GeneralObject.scanChoice.next().charAt(0);
                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                    System.out.print("\nInvalid input.");
                }
            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

            if (choice == 'Y' || choice == 'y') {
                System.out.print("\nAdd Service");
                addService(services);
            }  
            return 1;

        } else {
            return -1;
        }
    }

    // Delete Service
    public static void deleteService(ArrayList<Service> services) {
        String searchCode;
        String deleteCode;

        // Enter Service Code or Keyword
        do {
            System.out.print("\nEnter the Service Code or Keyword: ");
            searchCode = scanCode.nextLine();

            if (searchCode.isEmpty()) {
                System.out.print("\nPlease enter an input.");
            }
        } while (searchCode.isEmpty());

        int ctrDel = 0;
        int index = 0;
        int choice;
        int found = 0;

        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getDelIndicator() != 'D' && (searchCode.equals(services.get(i).getServCode()) || services.get(i).getDescription().toLowerCase().contains(searchCode.toLowerCase()))) {
                ctrDel++;
                index = i;
            }
        }
        
        if (ctrDel == 0) {
            System.out.print("\nNo record found.");
            found = 0;
            
        } else if (ctrDel == 1) {
            found = 1;

        } else if (ctrDel > 1) {
            System.out.print("\nCode\tDescription\t\tPrice\n");
            for (int i = 0; i < services.size(); i++) {
                if ((services.get(i).getDelIndicator() != 'D' && (searchCode.equals(services.get(i).getServCode()) || services.get(i).getDescription().toLowerCase().contains(searchCode.toLowerCase())))) {
                    System.out.println(services.get(i).getServCode() + "\t" + services.get(i).getDescription() + " \t" + services.get(i).getPrice());
                }
            }

            // Enter specific Service Code
            do {
                System.out.print("\nEnter the specific Service Code: ");
                deleteCode = scanCode.nextLine();

                if (deleteCode.isEmpty()) {
                    System.out.print("\nPlease enter an input.");
                }
            } while (deleteCode.isEmpty());

            for (int j = 0; j < services.size(); j++) {
                if (services.get(j).getDelIndicator() != 'D' && deleteCode.equals(services.get(j).getServCode())) {
                    found = 1;
                    index = j;
                }
            }

            if (found == 0) {
                System.out.print("\nNo record found.");
            }

        }

        if (found == 1) {
            String delReason;

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
                System.out.print("\nDelete Service Record? This action cannot be undone. [Y/N]: ");
                choice = GeneralObject.scanChoice.next().charAt(0);   
                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                    System.out.print("\nInvalid input.");
                }
            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

            if (choice == 'Y' || choice == 'y') {
                try {
                    File servFile = new File("Services.txt");
                    File tempFile = new File("temp.txt");
                    Scanner servReader = new Scanner(servFile);
        
                    char temp[] = new char[30];  // temporary char array
                    
                    int lineCtr = 0;  // line counter
                    int i = 0;
                    int k = 0;

                    String servCode = "";
                    String description = "";
                    float price = 0;
        
                    while (servReader.hasNextLine()) {
                        String data = servReader.nextLine();

                        if (lineCtr == index) {          
                            int semiCtr = 0;  // semi-colon counter

                            for (char c : data.toCharArray()) {
                                if (c == ';') {

                                    // Service Code
                                    if (semiCtr == 0) {
                                        servCode = GeneralObject.convertCharToString(temp);

                                    // First Name
                                    } else if (semiCtr == 1) {
                                        description = GeneralObject.convertCharToString(temp);

                                    // Price
                                    } else if (semiCtr == 2) {
                                        price = Float.parseFloat(GeneralObject.convertCharToString(temp));
                                    }

                                    // reinitialize temp
                                    for (k = 0; k < temp.length; k++) {
                                        temp[k] = ' ';
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
                                myWriter.write(servCode + ";" + description + ";" + price + ";D;" + delReason + ";\n");
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
        
                    servReader.close();
                    
                    // Replace original file with temp file
                    servFile.delete();
                    tempFile.renameTo(servFile);
                    
                } catch (FileNotFoundException e) {
                    System.out.println("An error occurred.");
                    e.printStackTrace();
                }

                services.get(index).setDelIndicator('D');
                services.get(index).setDelReason(delReason);
                
                System.out.print("\n" + services.get(index).getServCode() + " " + services.get(index).getDescription() + " has been deleted.\n");
            }
        }
    }

    // Search Service
    public static void searchService(ArrayList<Service> services) {
        String searchCode;
        int ctrSearch = 0;

        System.out.print("\nSearch Services");
        // Enter Service Code or Keyword
        do {
            System.out.print("\nEnter the Service Code or Keyword: ");
            searchCode = scanCode.nextLine();

            if (searchCode.isEmpty()) {
                System.out.print("\nPlease enter an input.");
            }
        } while (searchCode.isEmpty());

        for (int i = 0; i < services.size(); i++) {
            if ((services.get(i).getDelIndicator() != 'D' && (searchCode.equals(services.get(i).getServCode()) || services.get(i).getDescription().toLowerCase().contains(searchCode.toLowerCase())))) {
                ctrSearch++;

                if (ctrSearch == 1) {
                    System.out.print("\nCode\tDescription\t\tPrice\n");
                }
                System.out.println(services.get(i).getServCode() + "\t" + services.get(i).getDescription() + " \t" + services.get(i).getPrice());
                
            }
        }

        if (ctrSearch == 0)
            System.out.print("\nNo record found.");
    }
}
