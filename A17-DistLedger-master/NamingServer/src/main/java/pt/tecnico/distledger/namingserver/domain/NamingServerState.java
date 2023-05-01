package pt.tecnico.distledger.namingserver.domain;

import pt.tecnico.distledger.namingserver.exception.ServerAlreadyDeletedException;
import pt.tecnico.distledger.namingserver.exception.ServerEntryAlreadyExistsOnServiceException;
import pt.tecnico.distledger.namingserver.exception.AddressAlreadyInUseException;

import java.util.Map;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;



public class NamingServerState {

    // attributes

    private static final boolean DEBUG_FLAG = (System.getProperty("debug") != null);
    private static void debug(String debugMessage) { if (DEBUG_FLAG) System.err.println("NamingServerState: " + debugMessage); }

    Map<String, ServiceEntry> serviceEntries = new HashMap<>();

    // constructors

    public NamingServerState() {
    }

    // accessors
    public ServiceEntry getServiceEntry(String service) {
        return serviceEntries.get(service);
    }

    // naming server interface

    public synchronized void register(String service, String qualifier, String address) throws ServerEntryAlreadyExistsOnServiceException, AddressAlreadyInUseException {
        ServerEntry serverEntry = new ServerEntry(address,qualifier);
        if (serviceEntries.containsKey(service)) {
            if (serviceEntries.get(service).serverAlreadyRegisterForService(address, qualifier)) {
                throw new ServerEntryAlreadyExistsOnServiceException();
            } else if (serviceEntries.get(service).addressAlreadyRegistered(address)) {
                throw new AddressAlreadyInUseException();
            } else {
                ServiceEntry serviceEntry = serviceEntries.get(service);
                serviceEntry.addServerEntry(serverEntry);
                serviceEntries.put(service, serviceEntry);
                debug("registered '" + qualifier + " " + address + "' " + "for service '" + service + "'" );
            }  
        } else {
            ServiceEntry serviceEntry = new ServiceEntry(service);
            serviceEntry.addServerEntry(serverEntry);
            serviceEntries.put(service, serviceEntry);
            debug("registered '" + qualifier + " " + address + "' " + "for new service '" + service + "'" );
        }
        
    }

    public synchronized Set<ServerEntry> lookup(String service, String qualifier) {
        Set<ServerEntry> serverList = new HashSet<>();
        if (serviceEntries.containsKey(service)) {
            if (qualifier.equals("")) {
                // all servers of service
                for (ServerEntry serverEntry : serviceEntries.get(service).getServerEntries()) {
                    serverList.add(serverEntry); 
                }
            } else {
                if (this.hasServiceAndQualifier(service, qualifier)) {
                    // all servers of service and qualifier
                    for (ServerEntry serverEntry : serviceEntries.get(service).getServerEntries()) {
                        if (serverEntry.getQualifier().equals(qualifier)) {
                            serverList.add(serverEntry); 
                        }
                    }
                } else {
                    // empty list
                }
            }
        }
        return serverList;
    }

    public synchronized void delete(String service, String address) throws ServerAlreadyDeletedException {
        if (serviceEntries.containsKey(service)) {
            Set<ServerEntry> servers = serviceEntries.get(service).getServerEntries();
            for (ServerEntry serverEntry : servers) {
                if (serverEntry.getAddress().equals(address)) {
                    serviceEntries.get(service).removeServerEntry(serverEntry);
                }
            }
        } else {
            throw new ServerAlreadyDeletedException();
        }
        debug("deleted '" + address + "' " + "for service '" + service + "'" );
    }

    // other methods

    public boolean hasService(String service) {
        return serviceEntries.containsKey(service);
    }

    public boolean hasServiceAndQualifier(String service, String qualifier) {
        ServiceEntry serviceEntry = serviceEntries.get(service);
        if (serviceEntry == null) return false;
        return serviceEntry.hasQualifier(qualifier);
    }
}
