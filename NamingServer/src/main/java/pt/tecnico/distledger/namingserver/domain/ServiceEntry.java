package pt.tecnico.distledger.namingserver.domain;

import java.util.*;

public class ServiceEntry {
    
    String service;

    Set<ServerEntry> serverEntries = new HashSet<>();

    public ServiceEntry(String service) {

        this.service = service;
    }

    public Set<ServerEntry> getServerEntries() {
        return serverEntries;
    }

    public void addServerEntry(ServerEntry serverEntry) {
        serverEntries.add(serverEntry);
    }

    public void removeServerEntry(ServerEntry serverEntry) {
        serverEntries.remove(serverEntry);
    }

    public boolean serverAlreadyRegisterForService(String address, String qualifier) {
        boolean result = false;
        for (ServerEntry serverEntry : serverEntries) {
            if (serverEntry.getAddress().equals(address) && serverEntry.getQualifier().equals(qualifier)) {
                result = true;
            break;
            }
        }
        return result;
    }

    public boolean hasQualifier(String qualifier) {
        for (ServerEntry serverEntry : serverEntries) {
            if (qualifier.equals(serverEntry.getQualifier())) {
                return true;
            }
        }
        return false;
    }

    public boolean addressAlreadyRegistered(String address) {
        boolean result = false;
        for (ServerEntry serverEntry : serverEntries) {
            if (serverEntry.getAddress().equals(address)) {
                return true;
            }
        }
        return result;
    }

    @Override
    public String toString() {
        String aux = "";
        for (ServerEntry serverEntry : serverEntries) {
            aux += "    " + serverEntry.toString();
        }

        return "ServiceEntry {\n" +
               "    service: " + service + "\n" +
               aux + 
               "}\n";
    }

    
}
