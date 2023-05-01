package pt.tecnico.distledger.server.exception;

public class ServerAlreadyDeactiveException extends Exception {
    public ServerAlreadyDeactiveException() {
        super("Server is already deactivated.\n");
    }
}
