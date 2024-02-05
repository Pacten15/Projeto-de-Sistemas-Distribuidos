package pt.tecnico.distledger.server.exception;

public class CannotModifyBrokerException extends Exception {
    public CannotModifyBrokerException() {
        super("A broker can not be modified.\n");
    }
    
}
