package pt.tecnico.distledger.server.grpc;

import pt.tecnico.distledger.server.domain.operation.Operation;
import pt.tecnico.distledger.server.domain.operation.CreateAccountOp;
import pt.tecnico.distledger.server.domain.operation.TransferOp;
import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions;
import pt.ulisboa.tecnico.distledger.contract.DistLedgerCommonDefinitions.OperationType;

public class ServerGrpcUtilities {    
    public static DistLedgerCommonDefinitions.Operation buildDistLedgerCommonDefinitionsOperation(Operation operation) {
        DistLedgerCommonDefinitions.Operation.Builder ledgerBuilder = DistLedgerCommonDefinitions.Operation.newBuilder();
        if (operation instanceof CreateAccountOp) {
            ledgerBuilder.setType(OperationType.OP_CREATE_ACCOUNT)
                         .setUserId(operation.getAccount());
        } else if (operation instanceof TransferOp) {
            TransferOp transferOp = (TransferOp) operation;
            ledgerBuilder.setType(OperationType.OP_TRANSFER_TO)
                         .setUserId(transferOp.getAccount())
                         .setDestUserId(transferOp.getDestAccount())
                         .setAmount(transferOp.getAmount());
        } else {
            ledgerBuilder.setType(OperationType.OP_UNSPECIFIED);
        }
        ledgerBuilder.addAllPrevTS(operation.getPrevTS())
                     .addAllTS(operation.getTS());
        return ledgerBuilder.build();
    }

    public static Operation buildOperation(DistLedgerCommonDefinitions.Operation ledger) {
        Operation operation;
        if (ledger.getType() == OperationType.OP_CREATE_ACCOUNT) {
            operation = new CreateAccountOp(ledger.getUserId(), ledger.getPrevTSList(), ledger.getTSList());
        } else if (ledger.getType() == OperationType.OP_TRANSFER_TO) {
            operation = new TransferOp(ledger.getUserId(), ledger.getDestUserId(), ledger.getAmount(), ledger.getPrevTSList(), ledger.getTSList());
        } else {
            operation = null;
        }
        operation.setPrevTS(ledger.getPrevTSList());
        operation.setTS(ledger.getTSList());
        return operation;
    }
}
