package pt.tecnico.distledger.userclient;

import pt.tecnico.distledger.server.exception.NoSuchQualifierException;
import pt.tecnico.distledger.userclient.grpc.UserService;

import java.util.Scanner;

public class CommandParser {

    private static final String SPACE = " ";
    private static final String CREATE_ACCOUNT = "createAccount";
    private static final String TRANSFER_TO = "transferTo";
    private static final String BALANCE = "balance";
    private static final String HELP = "help";
    private static final String EXIT = "exit";

    private final UserService userService;

    public CommandParser(UserService userService) {
        this.userService = userService;
    }

    void parseInput() {

        Scanner scanner = new Scanner(System.in);
        boolean exit = false;
        
        while (!exit) {
            System.out.print("> ");
            String line = scanner.nextLine().trim();
            String cmd = line.split(SPACE)[0];

            try {
                switch (cmd) {
                    case CREATE_ACCOUNT:
                        this.createAccount(line);
                        break;

                    case TRANSFER_TO:
                        this.transferTo(line);
                        break;

                    case BALANCE:
                        this.balance(line);
                        break;

                    case HELP:
                        this.printUsage();
                        break;

                    case EXIT:
                        exit = true;
                        this.exit(line);
                        scanner.close();
                        break;

                    default:
                        this.printAllCommands();
                        break;
                }
            } catch (NoSuchQualifierException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println("Something went wrong: " + e.getMessage()+"\n");
                System.err.println(e.getStackTrace());
            }
        }
    }

    private boolean serverIsValid(String server) {
        return server.equals("A") || server.equals("B") || server.equals("C");
    }

    private void createAccount(String line) throws NoSuchQualifierException {
        String[] split = line.split(SPACE);

        if (split.length != 3) {
            this.printUsage();
            return;
        }

        String server = split[1];
        if (!serverIsValid(server)) { throw new NoSuchQualifierException(); }
        String username = split[2];
        
        userService.createAccount(server, username);
    }

    private void balance(String line) throws NoSuchQualifierException {
        String[] split = line.split(SPACE);

        if (split.length != 3) {
            this.printUsage();
            return;
        }
        
        String server = split[1];
        if (!serverIsValid(server)) { throw new NoSuchQualifierException(); }
        String username = split[2];
        
        userService.balance(server, username);
    }

    private void transferTo(String line) throws NoSuchQualifierException {
        String[] split = line.split(SPACE);

        if (split.length != 5) {
            this.printUsage();
            return;
        }
        
        String server = split[1];
        if (!serverIsValid(server)) { throw new NoSuchQualifierException(); }
        String from = split[2];
        String dest = split[3];
        Integer amount = Integer.valueOf(split[4]);

        userService.transferTo(server, from, dest,amount);
    }

    private void exit(String line) {
        String[] split = line.split(SPACE);

        if (split.length != 1) {
            this.printUsage();
            return;
        }
        
        userService.close();
    }

    private void printUsage() {
        System.out.println("Usage:\n" +
                        "- createAccount <server> <username>\n" +
                        "- balance <server> <username>\n" +
                        "- transferTo <server> <username_from> <username_to> <amount>\n" +
                        "- exit\n");
    }

    private void printAllCommands() {
        System.out.println("All Possible Commands:\n" +
                        "- createAccount <server> <username>\n" +
                        "- balance <server> <username>\n" +
                        "- transferTo <server> <username_from> <username_to> <amount>\n" +
                        "- exit\n");
    }
}
