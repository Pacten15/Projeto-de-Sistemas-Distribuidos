package pt.tecnico.distledger.server.grpc;

import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import pt.tecnico.distledger.namingserver.grpc.NamingServerService;
import pt.tecnico.distledger.namingserver.other.NamingServerUtilities;
import pt.tecnico.distledger.server.domain.operation.Operation;
import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions.LedgerState;
import pt.ulisboa.tecnico.distledger.contract.distledgerserver.CrossServerServiceGrpc;
import pt.ulisboa.tecnico.distledger.contract.distledgerserver.CrossServerDistLedger.PropagateStateRequest;
import io.grpc.StatusRuntimeException;

import java.util.List;

public class CrossServerService implements AutoCloseable {
    
    private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);
	private static void debug(String debugMessage) { if (DEBUG_FLAG) System.err.println("CrossServerService: " + debugMessage); }

    private final NamingServerService namingServerService;

    private final String sourceServerAddress;

    private ManagedChannel channel;
    private CrossServerServiceGrpc.CrossServerServiceBlockingStub stub;

    public CrossServerService(String sourceServerAddress, NamingServerService namingServerService) {
        this.sourceServerAddress = sourceServerAddress;
        this.namingServerService = namingServerService;
    }

    private void swapTargetServer(String address) {
        String host = NamingServerUtilities.parseServerHost(address);
        int port = NamingServerUtilities.parseServerPort(address);
        if (channel != null) { this.close(); }
        this.channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build();
        this.stub = CrossServerServiceGrpc.newBlockingStub(channel);
    }

    public void propagateState(List<Operation> ledger, List<Integer> replicaTS) {
        for (String targetServerAddress : namingServerService.lookup(NamingServerUtilities.DISTLEDGER_SERVICE)) {
            if (!sourceServerAddress.equals(targetServerAddress)) {
                swapTargetServer(targetServerAddress);
                LedgerState.Builder ledgerStateBuilder = LedgerState.newBuilder();
                for (Operation operation : ledger) {
                    ledgerStateBuilder.addLedger(ServerGrpcUtilities.buildDistLedgerCommonDefinitionsOperation(operation));
                }
                try {
                    stub.propagateState(PropagateStateRequest.newBuilder()
                                                             .setState(ledgerStateBuilder.build())
                                                             .addAllReplicaTS(replicaTS)
                                                             .build());
                } catch (StatusRuntimeException e) {
                    System.out.println(e.getMessage());
                }
                debug("gossiped state to server with address: '" + targetServerAddress + "'");
            }
        }
    }

    @Override
	public final void close() {
		channel.shutdown();
	}
}
