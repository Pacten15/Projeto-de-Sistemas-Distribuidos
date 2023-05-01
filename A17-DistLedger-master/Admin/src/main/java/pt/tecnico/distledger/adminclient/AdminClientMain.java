package pt.tecnico.distledger.adminclient;

import pt.tecnico.distledger.namingserver.grpc.NamingServerService;
import pt.tecnico.distledger.adminclient.grpc.AdminService;

public class AdminClientMain {
    public static void main(String args[]) {

        System.out.println(AdminClientMain.class.getSimpleName());

        final String namingServerHost = "localhost";
        final int namingServerPort = 5001;

        CommandParser parser = new CommandParser(new AdminService(new NamingServerService(namingServerHost, namingServerPort)));
        parser.parseInput();

    }
}
