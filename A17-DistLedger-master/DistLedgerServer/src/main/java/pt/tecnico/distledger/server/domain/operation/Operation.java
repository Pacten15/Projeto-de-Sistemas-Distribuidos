package pt.tecnico.distledger.server.domain.operation;

import pt.tecnico.distledger.server.gossip.GossipUtilities;

import java.util.List;
import java.util.ArrayList;

public class Operation implements Comparable<Operation> {
    
    private int i;
    private String account;
    private List<Integer> TS;
    private List<Integer> prevTS;

    private boolean executed;
    public void setExecuted(boolean executed) { this.executed = executed; }
    public boolean isExecuted() { return executed; }

    public Operation(String account, List<Integer> prevTS) {
        this.account = account;
        this.prevTS = new ArrayList<>(prevTS);
        this.TS = deriveTS(this.prevTS);
        this.executed = false;
    }

    public Operation(String account, List<Integer> prevTS, List<Integer> TS) {
        this.account = account;
        this.prevTS = new ArrayList<>(prevTS);
        this.TS = new ArrayList<>(TS);
        this.executed = false;
    }

    private List<Integer> deriveTS(List<Integer> prevTS) {
        List<Integer> TS = new ArrayList<>(prevTS);
        TS.set(GossipUtilities.TS_INDEX, TS.get(GossipUtilities.TS_INDEX)+1);
        return TS;
    }

    public void deriveTSNow() {
        this.TS = deriveTS(this.prevTS);
    }

    public boolean isStable(List<Integer> valueTS) { 
        return GossipUtilities.TSLessOrEqual(prevTS, valueTS);
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public List<Integer> getPrevTS() {
        return prevTS;
    }

    public void setPrevTS(List<Integer> prevTS) {
        this.prevTS = prevTS;
    }

    public List<Integer> getTS() {
        return TS;
    }

    public void setTS(List<Integer> tS) {
        this.TS = tS;
    }

    @Override
    public String toString() {
        return "Operation [account=" + account + ", TS=" + TS + ", prevTS=" + prevTS + "]";
    }


    public int compareTo(Operation other) {
        if (GossipUtilities.TSEqual(this.getPrevTS(), other.getPrevTS())) {
            return 0;
        } else if (GossipUtilities.TSLess(other.getPrevTS(), this.getPrevTS())) {
            return 1;
        } else {
            return -1;
        }
    }
}

