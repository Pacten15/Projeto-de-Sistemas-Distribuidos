package pt.tecnico.distledger.server.grpc;

import io.grpc.stub.StreamObserver;

import pt.tecnico.distledger.server.domain.operation.*;
import pt.tecnico.distledger.server.exception.ServerAlreadyActiveException;
import pt.tecnico.distledger.server.exception.ServerAlreadyDeactiveException;
import pt.tecnico.distledger.server.domain.ServerState;

import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions.LedgerState;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminServiceGrpc;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.ActivateRequest;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.ActivateResponse;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.DeactivateRequest;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.DeactivateResponse;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.GossipRequest;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.GossipResponse;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.getLedgerStateRequest;
import pt.ulisboa.tecnico.distledger.contract.admin.AdminDistLedger.getLedgerStateResponse;

import static io.grpc.Status.FAILED_PRECONDITION;


public class AdminServiceImpl extends AdminServiceGrpc.AdminServiceImplBase {

    private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);
    private static void debug(String debugMessage) { if (DEBUG_FLAG) System.err.println("AdminServiceImpl: " + debugMessage); }

    private ServerState serverState;
    
    public AdminServiceImpl(ServerState serverState) {
        this.serverState = serverState;
    }

    @Override
    public void activate(ActivateRequest request, StreamObserver<ActivateResponse> responseObserver) {
        try {
            serverState.activate();
            ActivateResponse response = ActivateResponse.newBuilder().build();
            responseObserver.onNext(response);
		    responseObserver.onCompleted();
        } catch (ServerAlreadyActiveException e) {
            responseObserver.onError(FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void deactivate(DeactivateRequest request, StreamObserver<DeactivateResponse> responseObserver) {
        try {
            serverState.deactivate();
            DeactivateResponse response = DeactivateResponse.newBuilder().build();;
            responseObserver.onNext(response);
		    responseObserver.onCompleted();
        } catch (ServerAlreadyDeactiveException e) {
            responseObserver.onError(FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        }
    }

    @Override
    public void gossip(GossipRequest request, StreamObserver<GossipResponse> responseObserver) {
        serverState.gossip();
        GossipResponse response = GossipResponse.newBuilder().build();
        responseObserver.onNext(response);
        responseObserver.onCompleted();
    }
    
    @Override
    public synchronized void getLedgerState(getLedgerStateRequest request, StreamObserver<getLedgerStateResponse> responseObserver) {
        LedgerState.Builder ledgerStateBuilder = LedgerState.newBuilder();
        for (Operation operation : serverState.getLedgerState()) {
            ledgerStateBuilder.addLedger(ServerGrpcUtilities.buildDistLedgerCommonDefinitionsOperation(operation));
        }
        getLedgerStateResponse response = getLedgerStateResponse.newBuilder().setLedgerState(ledgerStateBuilder).build();
        responseObserver.onNext(response);
		responseObserver.onCompleted();
    }
}

