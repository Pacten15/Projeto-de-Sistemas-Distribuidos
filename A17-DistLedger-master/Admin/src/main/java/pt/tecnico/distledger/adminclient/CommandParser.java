package pt.tecnico.distledger.adminclient;

import pt.tecnico.distledger.server.exception.NoSuchQualifierException;

import pt.tecnico.distledger.adminclient.grpc.AdminService;

import java.util.Scanner;

public class CommandParser {

    private static final String SPACE = " ";
    private static final String ACTIVATE = "activate";
    private static final String DEACTIVATE = "deactivate";
    private static final String GET_LEDGER_STATE = "getLedgerState";
    private static final String GOSSIP = "gossip";
    private static final String HELP = "help";
    private static final String EXIT = "exit";

    private final AdminService adminService;

    public CommandParser(AdminService adminService) {
        this.adminService = adminService;
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
                    case ACTIVATE:
                        this.activate(line);
                        break;

                    case DEACTIVATE:
                        this.deactivate(line);
                        break;

                    case GET_LEDGER_STATE:
                        this.getLedgerState(line);
                        break;

                    case GOSSIP:
                        this.gossip(line);
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
                        printUsage();
                        break;
                }
            } catch (NoSuchQualifierException e) {
                System.err.println(e.getMessage());
            } catch (Exception e) {
                System.err.println("Something went wrong: " + e.getMessage()+"\n");
            }
        }
    }

    private boolean serverIsValid(String server) {
        return server.equals("A") || server.equals("B") || server.equals("C");
    }

    private void activate(String line) throws NoSuchQualifierException {
        String[] split = line.split(SPACE);

        if (split.length != 2) {
            this.printUsage();
            return;
        }
        String server = split[1];
        if (!serverIsValid(server)) { throw new NoSuchQualifierException(); }

        adminService.activate(server);
    }

    private void deactivate(String line) throws NoSuchQualifierException {
        String[] split = line.split(SPACE);

        if (split.length != 2) {
            this.printUsage();
            return;
        }
        String server = split[1];
        if (!serverIsValid(server)) { throw new NoSuchQualifierException(); }

        adminService.deactivate(server);
    }

    private void getLedgerState(String line) throws NoSuchQualifierException {
        String[] split = line.split(SPACE);

        if (split.length != 2) {
            this.printUsage();
            return;
        }
        String server = split[1];
        if (!serverIsValid(server)) { throw new NoSuchQualifierException(); }

        adminService.getLedgerState(server);   
    }

    private void gossip(String line) throws NoSuchQualifierException {
        String[] split = line.split(SPACE);

        if (split.length != 2) {
            this.printUsage();
            return;
        }
        String server = split[1];
        if (!serverIsValid(server)) { throw new NoSuchQualifierException(); }
        adminService.gossip(server);
    }

    private void exit(String line) {
        String[] split = line.split(SPACE);

        if (split.length != 1) {
            this.printUsage();
            return;
        }
        adminService.close();
    }

    private void printUsage() {
        System.out.println("Usage:\n" +
                "- activate <server>\n" +
                "- deactivate <server>\n" +
                "- getLedgerState <server>\n" +
                "- gossip <server>\n" +
                "- exit\n");
    }
}
