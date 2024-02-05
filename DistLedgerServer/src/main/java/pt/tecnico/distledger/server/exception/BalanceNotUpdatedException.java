package pt.tecnico.distledger.server.exception;

public class BalanceNotUpdatedException extends Exception {

    public BalanceNotUpdatedException() {
        super("balance still not updated\n");
    }
    
}
