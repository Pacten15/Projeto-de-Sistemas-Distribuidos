package pt.tecnico.distledger.namingserver.exception;

public class ServerEntryAlreadyExistsOnServiceException extends Exception {
    
    public ServerEntryAlreadyExistsOnServiceException() {
        super("Not possible to register the server\n");
    }   
}
