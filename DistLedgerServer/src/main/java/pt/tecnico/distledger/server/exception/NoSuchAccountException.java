package pt.tecnico.distledger.server.exception;

public class NoSuchAccountException extends Exception {
    public NoSuchAccountException() {
        super("Account doesn't exist.\n");
    }
}