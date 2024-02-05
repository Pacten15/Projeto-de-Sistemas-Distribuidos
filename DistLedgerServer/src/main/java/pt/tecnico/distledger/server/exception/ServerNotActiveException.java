package pt.tecnico.distledger.server.exception;

public class ServerNotActiveException extends Exception {
    public ServerNotActiveException() {
        super("Server is unavailable.\n");
    }
}
