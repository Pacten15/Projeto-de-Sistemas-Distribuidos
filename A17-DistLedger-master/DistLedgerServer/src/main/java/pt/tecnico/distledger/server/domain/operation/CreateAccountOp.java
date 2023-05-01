package pt.tecnico.distledger.server.domain.operation;

import java.util.List;

public class CreateAccountOp extends Operation {

    public CreateAccountOp(String account, List<Integer> prevTS) {
        super(account, prevTS);
    }

    public CreateAccountOp(String account,  List<Integer> prevTS, List<Integer> TS) {
        super(account, prevTS, TS);
    }
}
