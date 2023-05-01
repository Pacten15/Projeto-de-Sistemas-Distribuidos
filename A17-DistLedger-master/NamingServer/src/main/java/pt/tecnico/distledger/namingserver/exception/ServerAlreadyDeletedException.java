package pt.tecnico.distledger.namingserver.exception;

public class ServerAlreadyDeletedException extends Exception {
    
    public ServerAlreadyDeletedException() {
        super("Not possible to remove the server\n");
    }   
}
