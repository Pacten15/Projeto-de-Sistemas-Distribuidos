package pt.tecnico.distledger.server.exception;

public class NoSuchDestinationAccountException extends Exception {
    public NoSuchDestinationAccountException() {
        super("Destination account does not exist.\n");
    }
}
