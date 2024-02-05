package pt.tecnico.distledger.server.exception;

public class NotEnoughCoinsException extends Exception {
    public NotEnoughCoinsException() {
        super("Not enough coins to transfer the specified amount.\n");
    }
}
