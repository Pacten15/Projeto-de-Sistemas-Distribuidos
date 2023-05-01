package pt.tecnico.distledger.server;

import java.io.IOException;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import pt.tecnico.distledger.namingserver.grpc.NamingServerService;
import pt.tecnico.distledger.server.domain.ServerState;
import pt.tecnico.distledger.server.gossip.GossipUtilities;
import pt.tecnico.distledger.server.grpc.AdminServiceImpl;
import pt.tecnico.distledger.server.grpc.CrossServerService;
import pt.tecnico.distledger.server.grpc.CrossServerServiceImpl;
import pt.tecnico.distledger.server.grpc.UserServiceImpl;

public class ServerMain {
    
    public static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);
    public static void debug(String debugMessage) { if (DEBUG_FLAG) System.err.println("ServerMain: " + debugMessage); }

    public static void main(String[] args) throws IOException, InterruptedException{

        // receive and print arguments
		System.out.printf("Received %d arguments%n", args.length);
		for (int i = 0; i < args.length; i++) {
			System.out.printf("arg[%d] = %s%n", i, args[i]);
		}

        // arguments error check
        if (args.length < 2) {
            System.err.println("Arguments missing!");
        }

        final String qualifier = args[1];
        GossipUtilities.set_TS_INDEX(qualifier);
        
        final String host = "localhost";
        final int port = Integer.parseInt(args[0]);
        final String address = host + ":" + port;
        
        final int namingServerPort = 5001;
        final NamingServerService namingServerService = new NamingServerService(host, namingServerPort);
        namingServerService.register(args[1], address);

        ServerState serverState = new ServerState(qualifier, new CrossServerService(address, namingServerService));
        final BindableService adminImpl = new AdminServiceImpl(serverState);
		final BindableService userImpl = new UserServiceImpl(serverState);
        final BindableService crossServerImpl = new CrossServerServiceImpl(serverState);
        Server server = ServerBuilder.forPort(port).addService(userImpl).addService(adminImpl).addService(crossServerImpl).build();
        
        Thread shutdownThread = new Thread(() -> { 
            namingServerService.delete(address);
            namingServerService.close();
            server.shutdown(); 
        });
        Runtime.getRuntime().addShutdownHook(shutdownThread);

        server.start();
		System.out.println("Server started, press enter to shutdown.");
        System.in.read();
        shutdownThread.start();
    }
}