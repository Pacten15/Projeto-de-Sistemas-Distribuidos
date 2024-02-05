package pt.tecnico.distledger.server.exception;

public class NoSuchQualifierException extends Exception {
    public NoSuchQualifierException() {
        super("Server must be either 'A' or 'B'.\n");
    }
}
