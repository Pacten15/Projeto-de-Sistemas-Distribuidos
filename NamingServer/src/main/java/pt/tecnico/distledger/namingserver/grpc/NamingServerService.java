package pt.tecnico.distledger.namingserver.grpc;

import java.util.List;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.DeleteRequest;
import pt.tecnico.distledger.namingserver.other.NamingServerUtilities;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerServiceGrpc;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.LookupRequest;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.RegisterRequest;

public class NamingServerService implements AutoCloseable {

    private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);
    private static void debug(String debugMessage) { if (DEBUG_FLAG) System.err.println("NamingServerService: " + debugMessage); }

    private final ManagedChannel channel;
    private final NamingServerServiceGrpc.NamingServerServiceBlockingStub stub;

    public NamingServerService(String host, int port) {
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub = NamingServerServiceGrpc.newBlockingStub(channel);
    }
    
    // register

    public void register(String qualifier, String address) {
        debug("requested register");
        stub.register(RegisterRequest.newBuilder().setService(NamingServerUtilities.DISTLEDGER_SERVICE).setAddress(address).setQualifier(qualifier).build());
    }

    // lookup

    public List<String> lookup(String service) {
        debug("requested lookup without specified qualifier");
        return stub.lookup(LookupRequest.newBuilder().setService(service).build()).getAddressList();
    }

    public List<String> lookup(String service, String qualifier) {
        debug("requested lookup with specified qualifier");
        return stub.lookup(LookupRequest.newBuilder().setService(service).setQualifier(qualifier).build()).getAddressList();
    }

    public void delete(String address) {
        debug("requested delete");
        stub.delete(DeleteRequest.newBuilder().setService("DistLedger").setAddress(address).build());
    }

    @Override
    public final void close() {
        channel.shutdown();
    }
}   
