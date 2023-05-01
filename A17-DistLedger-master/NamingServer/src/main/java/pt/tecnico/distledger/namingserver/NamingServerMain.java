package pt.tecnico.distledger.namingserver;

import java.io.IOException;
import io.grpc.BindableService;
import io.grpc.Server;
import io.grpc.ServerBuilder;

import pt.tecnico.distledger.namingserver.domain.NamingServerState;
import pt.tecnico.distledger.namingserver.grpc.NamingServerServiceImpl;

public class NamingServerMain {

    private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);
    private static void debug(String debugMessage) { if (DEBUG_FLAG) System.err.println("NamingServerMain: " + debugMessage); }

    public static void main(String[] args) throws IOException, InterruptedException {
        
        NamingServerState namingServerState = new NamingServerState();

        final int port = 5001;
        final BindableService namingServerImpl = new NamingServerServiceImpl(namingServerState);

        Server namingServer = ServerBuilder.forPort(port).addService(namingServerImpl).build();

        namingServer.start();
        System.out.println("Naming Server started");
        debug("port " + port);
        namingServer.awaitTermination();
    }

}
