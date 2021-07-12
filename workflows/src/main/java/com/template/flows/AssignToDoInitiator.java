package com.template.flows;

// ******************
// * AssignToDoInitiator flow *
// ******************

import co.paralleluniverse.fibers.Suspendable;
import com.template.contracts.Command;
import com.template.states.ToDoState;
import net.corda.core.contracts.StateAndRef;
import net.corda.core.flows.*;
import net.corda.core.identity.Party;
import net.corda.core.node.ServiceHub;
import net.corda.core.node.services.Vault;
import net.corda.core.node.services.vault.QueryCriteria;
import net.corda.core.transactions.SignedTransaction;
import net.corda.core.transactions.TransactionBuilder;

import java.security.PublicKey;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@InitiatingFlow
@StartableByRPC
public class AssignToDoInitiator extends FlowLogic<Void> {

    private String linearId;
    private String assignedTo;

    public AssignToDoInitiator(String linearId, String assignedTo) {
        this.linearId = linearId;
        this.assignedTo = assignedTo;
    }

    @Suspendable
    @Override
    public Void call() throws FlowException {
        ServiceHub sb = getServiceHub();
        Party notary = sb.getNetworkMapCache().getNotaryIdentities().get(0);

        QueryCriteria q = new QueryCriteria.LinearStateQueryCriteria(null, Arrays.asList(UUID.fromString(linearId)));

        Vault.Page<ToDoState> taskStatePage = sb.getVaultService().queryBy(ToDoState.class, q);

        List<StateAndRef<ToDoState>> states = taskStatePage.getStates();
        StateAndRef<ToDoState> currentStateAndRefToDo = states.get(0);
        ToDoState toDoState = currentStateAndRefToDo.getState().getData();
        System.out.println(toDoState.getTaskDescription());


        Set<Party> parties = sb.getIdentityService().partiesFromName(assignedTo, true);
        Party assignedToParty = parties.iterator().next();
        System.out.println("Party Encontrada");


        ToDoState newToDoState = toDoState.assign(assignedToParty);

        PublicKey myKey = getOurIdentity().getOwningKey();

        TransactionBuilder tb = new TransactionBuilder(notary)
                .addInputState(currentStateAndRefToDo)
                .addOutputState(newToDoState)
                .addCommand(new Command.AssignToDoCommand(), myKey, assignedToParty.getOwningKey());

        tb.verify(getServiceHub());
        SignedTransaction ptx = getServiceHub().signInitialTransaction(tb);

        List<Party> otherParties = newToDoState.getParticipants().stream().map(el -> (Party)el).collect(Collectors.toList());
        otherParties.remove(getOurIdentity());
        List<FlowSession> sessions = otherParties.stream().map(el -> initiateFlow(el)).collect(Collectors.toList());

        SignedTransaction stx = subFlow(new CollectSignaturesFlow(ptx, sessions));

        subFlow(new FinalityFlow(stx, sessions));

        return null;
    }
}