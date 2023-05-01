package pt.tecnico.distledger.server.grpc;

import io.grpc.stub.StreamObserver;
import pt.tecnico.distledger.server.domain.ServerState;
import pt.ulisboa.tecnico.distledger.contract.distledgerserver.CrossServerDistLedger.PropagateStateRequest;
import pt.ulisboa.tecnico.distledger.contract.distledgerserver.CrossServerDistLedger.PropagateStateResponse;
import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;
import pt.ulisboa.tecnico.distledger.contract.distledgerserver.CrossServerServiceGrpc;
import pt.tecnico.distledger.server.exception.ServerNotActiveException;
import pt.tecnico.distledger.server.domain.operation.*;

import static io.grpc.Status.UNAVAILABLE;

import java.util.ArrayList;
import java.util.List;

public class CrossServerServiceImpl extends CrossServerServiceGrpc.CrossServerServiceImplBase {

    private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);
    private static void debug(String debugMessage) { if (DEBUG_FLAG) System.err.println("CrossServerServiceImpl: " + debugMessage); }

    private ServerState serverState;
    
    public CrossServerServiceImpl(ServerState serverState) {
        this.serverState = serverState;
    }

    @Override
    public void propagateState(PropagateStateRequest request, StreamObserver<PropagateStateResponse> responseObserver) {
        List<Operation> newLedger = new ArrayList<>();
        for (DistLedgerCommonDefinitions.Operation ledger : request.getState().getLedgerList()) {
            newLedger.add(ServerGrpcUtilities.buildOperation(ledger));
        }
        try {
            serverState.update(newLedger, request.getReplicaTSList());
            PropagateStateResponse response = PropagateStateResponse.newBuilder().build();
            responseObserver.onNext(response);
		    responseObserver.onCompleted();
        } catch (ServerNotActiveException e) {
            responseObserver.onError(UNAVAILABLE.asRuntimeException());
        }
    }
}
