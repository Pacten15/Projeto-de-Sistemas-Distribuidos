package pt.tecnico.distledger.server.exception;

public class InvalidArgumentsException extends Exception {
    public InvalidArgumentsException() {
        super("Amount must be above zero.\n");
    }
    
}
