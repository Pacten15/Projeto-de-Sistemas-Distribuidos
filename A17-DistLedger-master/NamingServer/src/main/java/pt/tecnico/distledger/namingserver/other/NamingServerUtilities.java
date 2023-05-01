package pt.tecnico.distledger.namingserver.other;

public class NamingServerUtilities {

    public final static String DISTLEDGER_SERVICE = "DistLedger";

    public final static int ADDRESS_HOST_INDEX = 0;
    public final static int ADDRESS_PORT_INDEX = 1;

    public static String[] parseServerAddress(String address) {
        return address.split(":");
    }

    public static String parseServerHost(String address) {
        return parseServerAddress(address)[ADDRESS_HOST_INDEX];
    }

    public static int parseServerPort(String address) {
        return Integer.parseInt(parseServerAddress(address)[ADDRESS_PORT_INDEX]);
    }
}
