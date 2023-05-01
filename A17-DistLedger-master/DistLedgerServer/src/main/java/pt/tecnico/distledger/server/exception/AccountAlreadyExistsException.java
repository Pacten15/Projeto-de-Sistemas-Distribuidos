package pt.tecnico.distledger.server.exception;

public class AccountAlreadyExistsException extends Exception {
    public AccountAlreadyExistsException() {
        super("Account already exits.\n");
    }   
}
