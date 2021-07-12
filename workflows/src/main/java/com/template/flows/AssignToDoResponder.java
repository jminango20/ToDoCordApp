package com.template.flows;

import co.paralleluniverse.fibers.Suspendable;
import net.corda.core.flows.*;
import net.corda.core.transactions.SignedTransaction;
import org.jetbrains.annotations.NotNull;

// ******************
// * Responder flow *
// ******************
@InitiatedBy(AssignToDoInitiator.class)
public class AssignToDoResponder extends FlowLogic<SignedTransaction> {

    //private variable
    private FlowSession counterpartySession;

    //Constructor
    public AssignToDoResponder(FlowSession counterpartySession) {
        this.counterpartySession = counterpartySession;
    }

    @Suspendable
    @Override
    public SignedTransaction call() throws FlowException {

        final SignTransactionFlow signTransactionFlow = new SignTransactionFlow(counterpartySession) {
            @Override
            protected void checkTransaction(@NotNull SignedTransaction stx) throws FlowException {

            }
        };

        SignedTransaction stx = subFlow(signTransactionFlow);

        return subFlow(new ReceiveFinalityFlow(counterpartySession, stx.getId()));

    }
}
