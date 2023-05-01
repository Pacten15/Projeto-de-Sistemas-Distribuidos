package pt.tecnico.distledger.namingserver.domain;

public class ServerEntry {

    String address;

    String qualifier;

    public ServerEntry(String address, String qualifier) {
        this.address = address; 
        this.qualifier = qualifier;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getQualifier() {
        return qualifier;
    }

    public void setQualifier(String qualifier) {
        this.qualifier = qualifier;
    }

    @Override
    public String toString() {
        return "ServerEntry {address: '" + address + "', qualifier: '" + qualifier + "'}\n";
    }
}
