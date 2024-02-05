package pt.tecnico.distledger.server.exception;

public class CannotTransferToSelfException extends Exception {
    public CannotTransferToSelfException() {
        super("An accont can not transfer funds to himself.\n");
    }
}
