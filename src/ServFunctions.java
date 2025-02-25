import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class ServFunctions {
    public static Scanner scanner = new Scanner(System.in);

    private static AbstractInputHandler textHandler = new TextInputHandler();
    private static AbstractInputHandler yesNoHandler = new YesNoInputHandler();
    private static AbstractInputHandler servCodeHandler = new ServCodeInputHandler();
    private static AbstractInputHandler floatHandler = new FloatInputHandler();
    
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
                String price = "";
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
                            price = GeneralObject.convertCharToString(temp);

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
        String price;
        char delIndicator = '\0';
        String delReason = "";
        
        String choice;
        int i;
        int count;

        do {  // checks if service code already exists by checking each servCode in the file
            count = 0;

            // Service Code
            System.out.println("\nService Code: ");
            servCode = servCodeHandler.requestInput().toUpperCase();  // auto capitalization

            for (i = 0; i < services.size(); i++) {
                if (servCode.equals(services.get(i).getServCode())) {
                    System.out.println("Service Code already exists. Please enter a different Service Code.");
                    count = 1;
                }
            }
        } while (count == 1);

        // Description
        System.out.println("\nDescription: ");
        description = textHandler.requestInput();

        // Price
        System.out.println("\nPrice: ");
        price = floatHandler.requestInput();

        // Save record confirmation
        System.out.println("\nSave Laboratory Service? [Y/N] ");
        choice = yesNoHandler.requestInput();
        
        if (choice.equalsIgnoreCase("Y")) {
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
        String choice;

        System.out.println("\nServices cannot be edited.\nIf you would like to edit an existing service, this service will first be deleted, and a new service will be created.\nWould you like to proceed? [Y/N] ");
        choice = yesNoHandler.requestInput();

        if (choice.equalsIgnoreCase("Y")) {
            System.out.print("\nDelete Service");
            int success = deleteService(services);

            if (success == -1) {
                return 1;
            }

            // Add service confirmation
            System.out.println("\nThe program will now proceed to add a service. Continue? [Y/N] ");
            choice = yesNoHandler.requestInput();

            if (choice.equalsIgnoreCase("Y")) {
                System.out.print("\nAdd Service");
                addService(services);
            }  
            return 1;

        } else {
            return -1;
        }
    }

    // Delete Service
    public static int deleteService(ArrayList<Service> services) {
        String searchCode;
        String deleteCode;

        // Enter Service Code or Keyword
        System.out.println("\nEnter the Service Code or Keyword: ");
        searchCode = textHandler.requestInput();

        int ctrDel = 0;
        int index = 0;
        String choice;
        int found = 0;

        for (int i = 0; i < services.size(); i++) {
            if (services.get(i).getDelIndicator() != 'D' && (searchCode.equals(services.get(i).getServCode()) || services.get(i).getDescription().toLowerCase().contains(searchCode.toLowerCase()))) {
                ctrDel++;
                index = i;
            }
        }
        
        if (ctrDel == 0) {
            System.out.println("\nNo record found.");
            return -1;
            
        } else if (ctrDel == 1) {
            found = 1;

        } else if (ctrDel > 1) {
            System.out.print("\nCode\tDescription\t\tPrice\n");
            for (int i = 0; i < services.size(); i++) {
                if ((services.get(i).getDelIndicator() != 'D' && (searchCode.equals(services.get(i).getServCode()) || services.get(i).getDescription().toLowerCase().contains(searchCode.toLowerCase())))) {
                    System.out.println(services.get(i).getServCode() + "\t" + services.get(i).getDescription() + " \t" + services.get(i).getPrice());
                }
            }

            System.out.println("\nMultiple service records found. Displaying them below...");

            // Enter specific Service Code
            System.out.println("\nEnter the specific Service Code: ");
            deleteCode = servCodeHandler.requestInput();

            for (int j = 0; j < services.size(); j++) {
                if (services.get(j).getDelIndicator() != 'D' && deleteCode.equals(services.get(j).getServCode())) {
                    found = 1;
                    index = j;
                }
            }

            if (found == 0) {
                System.out.println("\nNo record found.");
                return -1;
            }

        }

        if (found == 1) {
            String delReason;

            // Reason for Deletion
            System.out.println("\nReason for Deletion: ");
            delReason = textHandler.requestInput();

            // Delete record confirmation
            System.out.println("\nDelete Service Record? This action cannot be undone. [Y/N] ");
            choice = yesNoHandler.requestInput();

            if (choice.equalsIgnoreCase("Y")) {
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

        return 1;
    }

    // Search Service
    public static void searchService(ArrayList<Service> services) {
        String searchCode;
        int ctrSearch = 0;

        System.out.print("\nSearch Services");

        // Enter Service Code or Keyword
        System.out.println("\nEnter the Service Code or Keyword: ");
        searchCode = textHandler.requestInput();
        
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
            System.out.println("\nNo record found.");
    }
}
