package pt.tecnico.distledger.userclient;

import pt.tecnico.distledger.namingserver.grpc.NamingServerService;
import pt.tecnico.distledger.userclient.grpc.UserService;

public class UserClientMain {

    public static void main(String[] args) {

        System.out.println(UserClientMain.class.getSimpleName());

        final String namingServerHost = "localhost";
        final int namingServerPort = 5001;

        CommandParser parser = new CommandParser(new UserService(new NamingServerService(namingServerHost, namingServerPort)));
        parser.parseInput();
    }
}
