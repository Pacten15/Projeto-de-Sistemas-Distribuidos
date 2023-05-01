package pt.tecnico.distledger.server.grpc;

import io.grpc.stub.StreamObserver;

import pt.ulisboa.tecnico.distledger.contract.user.UserServiceGrpc;
import pt.ulisboa.tecnico.distledger.contract.user.UserDistLedger.BalanceRequest;
import pt.ulisboa.tecnico.distledger.contract.user.UserDistLedger.BalanceResponse;
import pt.ulisboa.tecnico.distledger.contract.user.UserDistLedger.CreateAccountRequest;
import pt.ulisboa.tecnico.distledger.contract.user.UserDistLedger.CreateAccountResponse;
import pt.ulisboa.tecnico.distledger.contract.user.UserDistLedger.TransferToRequest;
import pt.ulisboa.tecnico.distledger.contract.user.UserDistLedger.TransferToResponse;
import pt.tecnico.distledger.server.domain.ServerState;
import pt.tecnico.distledger.server.exception.AccountAlreadyExistsException;
import pt.tecnico.distledger.server.exception.CannotModifyBrokerException;
import pt.tecnico.distledger.server.exception.CannotTransferToSelfException;
import pt.tecnico.distledger.server.exception.InvalidArgumentsException;
import pt.tecnico.distledger.server.exception.NoSuchAccountException;
import pt.tecnico.distledger.server.exception.NoSuchDestinationAccountException;
import pt.tecnico.distledger.server.exception.NotEnoughCoinsException;
import pt.tecnico.distledger.server.exception.ServerNotActiveException;
import pt.tecnico.distledger.server.exception.BalanceNotUpdatedException;

import static io.grpc.Status.INVALID_ARGUMENT;
import static io.grpc.Status.NOT_FOUND;
import static io.grpc.Status.ALREADY_EXISTS;
import static io.grpc.Status.PERMISSION_DENIED;
import static io.grpc.Status.UNAVAILABLE;

import java.util.List;

import static io.grpc.Status.FAILED_PRECONDITION;

public class UserServiceImpl extends UserServiceGrpc.UserServiceImplBase {
        
    private ServerState serverState;
    
    public UserServiceImpl(ServerState serverState) {
        this.serverState = serverState;
    }

    @Override
    public void balance(BalanceRequest request, StreamObserver<BalanceResponse> responseObserver) {
        try {
            List<Integer> res = serverState.balance(request.getUserId(), request.getPrevTSList());
            BalanceResponse response = BalanceResponse.newBuilder()
                                                      .setValue(res.get(3))
                                                      .addValueTS(res.get(0))
                                                      .addValueTS(res.get(1))
                                                      .addValueTS(res.get(2))
                                                      .build();
            responseObserver.onNext(response);
		    responseObserver.onCompleted();
        } catch (ServerNotActiveException e) {
            responseObserver.onError(UNAVAILABLE.withDescription(e.getMessage()).asRuntimeException());
        } catch (NoSuchAccountException e) {
            responseObserver.onError(NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch(BalanceNotUpdatedException e) {
            responseObserver.onError(UNAVAILABLE.withDescription(e.getMessage()).asRuntimeException());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createAccount(CreateAccountRequest request, StreamObserver<CreateAccountResponse> responseObserver) {
        try {
            List<Integer> TS = serverState.createAccount(request.getUserId(), request.getPrevTSList());
            CreateAccountResponse response = CreateAccountResponse.newBuilder().addAllTS(TS).build();
            responseObserver.onNext(response);
		    responseObserver.onCompleted();
        } catch (ServerNotActiveException e) {
            responseObserver.onError(UNAVAILABLE.withDescription(e.getMessage()).asRuntimeException());
        } catch (CannotModifyBrokerException e) {
            responseObserver.onError(PERMISSION_DENIED.withDescription(e.getMessage()).asRuntimeException());
        } catch (AccountAlreadyExistsException e) {
            responseObserver.onError(ALREADY_EXISTS.withDescription(e.getMessage()).asRuntimeException());   
        }
    }

    @Override
    public void transferTo(TransferToRequest request, StreamObserver<TransferToResponse> responseObserver) {
        try {
            List<Integer> TS = serverState.transferTo(request.getAccountFrom(), request.getAccountTo(), request.getAmount(), request.getPrevTSList());
            TransferToResponse response = TransferToResponse.newBuilder().addAllTS(TS).build();
            responseObserver.onNext(response);
		    responseObserver.onCompleted();
        } catch (ServerNotActiveException e) {
            responseObserver.onError(UNAVAILABLE.withDescription(e.getMessage()).asRuntimeException());
        } catch (NoSuchAccountException e) {
            responseObserver.onError(NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (NoSuchDestinationAccountException e) {
            responseObserver.onError(NOT_FOUND.withDescription(e.getMessage()).asRuntimeException());
        } catch (CannotTransferToSelfException e) {
            responseObserver.onError(PERMISSION_DENIED.withDescription(e.getMessage()).asRuntimeException());
        } catch (InvalidArgumentsException e) {
            responseObserver.onError(INVALID_ARGUMENT.withDescription(e.getMessage()).asRuntimeException());
        } catch (NotEnoughCoinsException e) {
            responseObserver.onError(FAILED_PRECONDITION.withDescription(e.getMessage()).asRuntimeException());
        }
    }
}