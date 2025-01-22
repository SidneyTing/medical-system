import java.util.Scanner;
import java.util.ArrayList;

public class App {
    // General Scanners
    public static Scanner scanChoice = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        // Arrays
        ArrayList<Patient> patients = new ArrayList<Patient>();
        ArrayList<Request> requests = new ArrayList<Request>();
        ArrayList<Service> services = new ArrayList<Service>();

        int choice;  // for decisions
        
        PatFunctions.readPatFile(patients);
        ServFunctions.readServFile(services);
        ReqFunctions.readReqFile(services, requests);

        do {
            System.out.print("\nMedical Laboratory Information System\n[1] Manage Patient Records\n[2] Manage Services\n[3] Manage Laboratory Requests\n\nSelect a transaction: ");
            choice = scanChoice.next().charAt(0);

            while (choice != '1' && choice != '2' && choice != '3') {
                System.out.print("\nInvalid input.\nPlease select a transaction: ");
                choice = scanChoice.next().charAt(0);
            }

            // Manage Patient Records
            if (choice == '1') {
                System.out.print("\nManage Patient Records\n[1] Add New Patient\n[2] Edit Patient Record\n[3] Delete Patient Record\n[4] Search Patient Record\n[X] Return to Main Menu\n\nSelect a transaction: ");
                choice = scanChoice.next().charAt(0);

                while (choice != '1' && choice != '2' && choice != '3' && choice != '4' && choice != 'X' && choice != 'x') {
                    System.out.print("\nInvalid input.\nPlease select a transaction: ");
                    choice = scanChoice.next().charAt(0);
                }

                // Add Patient
                if (choice == '1') {
                    System.out.print("\nAdd Patient");
                    PatFunctions.addPatient(patients);
                
                // Edit Patient
                } else if (choice == '2') {
                    int repeat = 0;  // initialize loop
                    int index;

                    do {
                        System.out.print("\nEdit Patient");
                        index = PatFunctions.selectPatient(patients);

                        if (index != -1) {
                            int proceed = PatFunctions.editPatient(patients, index);
                            if (proceed != -1) {
                                do {
                                    System.out.print("\nDo you want to edit another patient? [Y/N] ");
                                    choice = scanChoice.next().charAt(0);

                                    if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                                        System.out.print("\nInvalid input.");
                                    }
                                } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

                                if (choice == 'Y' || choice == 'y') {
                                    repeat = 1;
                                } else {
                                    repeat = 0;
                                }
                            }
                        }    
                    } while (repeat != 0 && index != -1);

                // Delete Patient
                } else if (choice == '3') {
                    int repeat = 0;  // initialize loop
                    int index;

                    do {
                        System.out.print("\nDelete Patient");
                        index = PatFunctions.selectPatient(patients);

                        if (index != -1) {
                            PatFunctions.deletePatient(patients, index);
                            do {
                                System.out.print("\nDo you want to delete another patient? [Y/N] ");
                                choice = scanChoice.next().charAt(0);
                                
                                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                                    System.out.print("\nInvalid input.");
                                }
                            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

                            if (choice == 'Y' || choice == 'y') {
                                repeat = 1;
                            } else {
                                repeat = 0;
                            }
                        }    
                    } while (repeat != 0 && index != -1);
                    
                // Search Patient
                } else if (choice == '4') {
                    int index;

                    System.out.print("\nSearch Patient");
                    index = PatFunctions.selectPatient(patients);

                    if (index != -1) {
                        PatFunctions.searchPatient(patients, requests, index);
                    }
                }  // Else return to main menu

            } else if (choice == '2') {
                System.out.print("\nManage Services\n[1] Add New Service\n[2] Edit Service\n[3] Delete Service\n[4] Search Service\n[X] Return to Main Menu\n\nSelect a transaction: ");
                choice = scanChoice.next().charAt(0);

                while (choice != '1' && choice != '2' && choice != '3' && choice != '4' && choice != 'X' && choice != 'x') {
                    System.out.print("\nInvalid input.\nPlease select a transaction: ");
                    choice = scanChoice.next().charAt(0);
                }

                // Add Service
                if (choice == '1') {
                    int repeat = 0;  // initialize loop

                    do {
                        ServFunctions.addService(services);
                        do {
                            System.out.print("\nDo you want to add another service? [Y/N] ");
                            choice = scanChoice.next().charAt(0);

                            if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                                System.out.print("\nInvalid input.");
                            }
                        } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

                        if (choice == 'Y' || choice == 'y') {
                            repeat = 1;
                        } else {
                            repeat = 0;
                        }
                    } while (repeat != 0);
                
                // Edit Service
                } else if (choice == '2') {
                    int repeat = 0;  // initialize loop
                    int proceed;

                    if (services.size() >= 1) {
                        do {
                            proceed = ServFunctions.editService(services);

                            if (proceed == 1) {
                                do {
                                    System.out.print("\nDo you want to edit another service? [Y/N]: ");
                                    choice = scanChoice.next().charAt(0);

                                    if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                                        System.out.print("\nInvalid input.");
                                    }
                                } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');
                                
                                if (choice == 'Y' || choice == 'y') {
                                    repeat = 1;
                                } else {
                                    repeat = 0;
                                }
                            }   
                        } while (repeat != 0 && proceed == 1);
                    } else {
                        System.out.println("No record found.");
                    }

                // Delete Service
                } else if (choice == '3') {
                    int repeat = 0;  // initialize loop

                    if (services.size() >= 1) {
                        do {
                            ServFunctions.deleteService(services);
                            do {
                                System.out.print("\nDo you want to delete another serivce? [Y/N]: ");
                                choice = scanChoice.next().charAt(0);
                                
                                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                                    System.out.print("\nInvalid input.");
                                }
                            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

                            if (choice == 'Y' || choice == 'y') {
                                repeat = 1;
                            } else {
                                repeat = 0;
                            }
                                
                        } while (repeat != 0);

                    } else {
                        System.out.println("No record found.");
                    }

                // Search Service
                } else if (choice == '4') {
                    int repeat = 0;  // initialize loop

                    if (services.size() >= 1) {
                        do {
                            ServFunctions.searchService(services);
                            do {
                                System.out.print("\nDo you want to search another service? [Y/N]: ");
                                choice = scanChoice.next().charAt(0);

                                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                                    System.out.print("\nInvalid input.");
                                }
                            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

                            if (choice == 'Y' || choice == 'y') {
                                repeat = 1;
                            } else {
                                repeat = 0;
                            }
                                
                        } while (repeat != 0);
                    }
                    
                    else {
                        System.out.println("No record found.");
                    }
                }  // Else return to main menu
            
            // Manage Laboratory Request
            } else {
                System.out.print("\nManage Laboratory Requests\n[1] Add New Laboratory Request\n[2] Edit Laboratory Request\n[3] Delete Laboratory Request\n[4] Search Laboratory Request\n[X] Return to Main Menu\n\nSelect a transaction: ");
                choice = scanChoice.next().charAt(0);

                while (choice != '1' && choice != '2' && choice != '3' && choice != '4' && choice != 'X' && choice != 'x') {
                    System.out.print("\nInvalid input.\nPlease select a transaction: ");
                    choice = scanChoice.next().charAt(0);
                }

                // Add Laboratory Request
                if (choice == '1') {
                    int repeat = 0;  // initialize loop
                    int success;

                    do {
                        success = ReqFunctions.addRequest(patients, services, requests);

                        if (success != -1) {
                            do {
                                System.out.print("\nDo you want to add another laboratory request? [Y/N] ");
                                choice = scanChoice.next().charAt(0);

                                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                                    System.out.print("\nInvalid input.");
                                }
                            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

                            if (choice == 'Y' || choice == 'y') {
                                repeat = 1;
                            } else {
                                repeat = 0;
                            }
                        }
                    } while (repeat != 0 && success != -1);
                
                // Edit Laboratory Request
                } else if (choice == '2') {
                    int repeat = 0;  // initialize loop
                    int index;
                    do {
                        System.out.print("\nRequest Update Options:\n[1] Result\n[X] Return to Main Menu\n\nSelect an update option: ");
                        choice = GeneralObject.scanChoice.next().charAt(0);

                        while (choice != '1' && choice != 'X' && choice != 'x') {
                            System.out.print("\nInvalid input.\nPlease select an update option: ");
                            choice = GeneralObject.scanChoice.next().charAt(0);
                        }

                        if (choice != 'X' && choice != 'x') {
                            index = ReqFunctions.selectRequest(patients, services, requests);

                            if (index != -1) {
                                ReqFunctions.editRequest(services, requests, index);
                            }
                            do {
                                System.out.print("\nDo you want to edit another request? [Y/N] ");
                                choice = scanChoice.next().charAt(0);

                                if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                                    System.out.print("\nInvalid input.");
                                }
                            } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

                            if (choice == 'Y' || choice == 'y') {
                                repeat = 1;
                            } 
                            else {
                                repeat = 0;
                            }
                        }

                    } while (repeat != 0);

                // Delete Laboratory Request
                } else if (choice == '3') {
                    int repeat = 0;  // initialize loop
                    int index;
                    
                    do {
                        System.out.print("\nDelete Request");
                        index = ReqFunctions.selectRequest(patients, services, requests);

                        if (index != -1) {
                            ReqFunctions.deleteRequest(requests, index);
                        }
                        
                        do {
                            System.out.print("\nDo you want to delete another lab request? [Y/N] ");
                            choice = scanChoice.next().charAt(0);

                            if (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                                System.out.print("\nInvalid input.");
                            }
                        } while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n');

                        if (choice == 'Y' || choice == 'y') {
                            repeat = 1;
                        } else {
                            repeat = 0;
                        }
                    } while (repeat != 0);

                // Search Laboratory Request
                } else if (choice == '4') {
                    do {
                        ReqFunctions.searchRequest(patients, services, requests);

                        System.out.print("\nDo you want to search another laboratory test result? [Y/N]: ");
                        choice = scanChoice.next().charAt(0);
            
                        while (choice != 'Y' && choice != 'y' && choice != 'N' && choice != 'n') {
                            System.out.print("\nInvalid input.\nDo you want to search another laboratory test result? [Y/N]: \n");
                            choice = scanChoice.next().charAt(0);
                        }
        
                    } while (choice == 'Y' || choice == 'y');
                }
            }
        } while (true);
    }
}