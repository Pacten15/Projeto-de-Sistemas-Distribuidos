package pt.tecnico.distledger.namingserver.grpc;

import io.grpc.stub.StreamObserver;
import pt.tecnico.distledger.namingserver.exception.ServerEntryAlreadyExistsOnServiceException;
import pt.tecnico.distledger.namingserver.domain.NamingServerState;
import pt.tecnico.distledger.namingserver.domain.ServerEntry;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerServiceGrpc;
import pt.tecnico.distledger.namingserver.exception.AddressAlreadyInUseException;
import pt.tecnico.distledger.namingserver.exception.ServerAlreadyDeletedException;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.LookupRequest;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.LookupResponse;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.RegisterRequest;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.RegisterResponse;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.DeleteRequest;
import pt.ulisboa.tecnico.distledger.contract.namingserver.NamingServerDistLedger.DeleteResponse;


import static io.grpc.Status.ALREADY_EXISTS;
import static io.grpc.Status.NOT_FOUND;
import java.util.Set;

public class NamingServerServiceImpl extends NamingServerServiceGrpc.NamingServerServiceImplBase {
    
    private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);
    private static void debug(String debugMessage) { if (DEBUG_FLAG) System.err.println("NamingServerServiceImpl: " + debugMessage); }
    
    private NamingServerState namingServerState;
    
    public NamingServerServiceImpl(NamingServerState namingServerState) {
        this.namingServerState = namingServerState;
    }

    @Override
    public void register(RegisterRequest request, StreamObserver<RegisterResponse> responseObserver) {
        try {
            namingServerState.register(request.getService(), request.getQualifier(), request.getAddress());
            RegisterResponse response = RegisterResponse.newBuilder().build();
            responseObserver.onNext(response);
		    responseObserver.onCompleted();
        } catch (ServerEntryAlreadyExistsOnServiceException e) {
            responseObserver.onError(ALREADY_EXISTS.withDescription(e.getMessage()).asRuntimeException());
        } catch (AddressAlreadyInUseException e) {
            responseObserver.onError(ALREADY_EXISTS.withDescription(e.getMessage()).asRuntimeException());
        } 
    }

    @Override
    public synchronized void lookup(LookupRequest request, StreamObserver<LookupResponse> responseObserver) {
        String service = request.getService();
        String qualifier = request.getQualifier();
        LookupResponse.Builder responseBuilder = LookupResponse.newBuilder();
        Set<ServerEntry> serverList = namingServerState.lookup(service, qualifier);
        for (ServerEntry serverEntry : serverList) {
            responseBuilder.addAddress(serverEntry.getAddress());
        }
        responseObserver.onNext(responseBuilder.build());
		responseObserver.onCompleted();
    }

    @Override
    public void delete(DeleteRequest request, StreamObserver<DeleteResponse> responseObserver) {
        try {
            namingServerState.delete(request.getService(),request.getAddress());
            DeleteResponse response = DeleteResponse.newBuilder().build();
            responseObserver.onNext(response);
		    responseObserver.onCompleted();
        } catch (ServerAlreadyDeletedException e) {
            responseObserver.onError(NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } 
    }
}



