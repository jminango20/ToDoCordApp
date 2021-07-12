package com.template.flows;

// ******************
// * Initiator flow *
// ******************

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.DummyToDoCommand;
import com.template.states.ToDoState;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

import java.util.Collections;

@StartableByRPC
public class CreateToDoFlow extends FlowLogic<Void> {

    private final String taskDescription;

    public CreateToDoFlow(String taskDescription) {
        this.taskDescription = taskDescription;
    }


    @Suspendable
    @Override
    public Void call() throws FlowException {

        ServiceHub serviceHub = getServiceHub();
        Party notary = serviceHub.getNetworkMapCache().getNotaryIdentities().get(0);

        Party me = getOurIdentity();

        ToDoState ts = new ToDoState(me,me,taskDescription);

        TransactionBuilder tb = new TransactionBuilder(notary);
        tb.addOutputState(ts);
        tb.addCommand(new DummyToDoCommand(), me.getOwningKey());

        SignedTransaction stx = getServiceHub().signInitialTransaction(tb);
        subFlow(new FinalityFlow(stx, Collections.<FlowSession>emptySet()));

        return null;

    }
}