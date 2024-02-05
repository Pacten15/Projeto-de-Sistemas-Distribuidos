package pt.tecnico.distledger.namingserver.exception;

public class AddressAlreadyInUseException extends Exception{
    public AddressAlreadyInUseException() {
        super("Address already in use by one server.\n");
    }
}
