package com.template.contracts;

import com.template.states.ToDoState;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

import static net.corda.core.contracts.ContractsDSL.requireThat;

// ************
// * Contract *
// ************
public class ToDoContract implements Contract {
    // This is used to identify our contract when building a transaction.
    //public static final String ID = "com.template.contracts.TemplateContract";

    @Override
    public void verify(LedgerTransaction tx) {
        System.out.println("O metodo verify() do ToDoContract e chamado");
        ToDoState toDoOutput = (ToDoState) tx.getOutputStates().get(0);
        requireThat(r -> {
            r.using("Tarefa em branco.", !toDoOutput.getTaskDescription().trim().equals(""));
            r.using("Tarefa tem tamanho maior a 25 caracteres", toDoOutput.getTaskDescription().length()<25);
            return null;
        });
    }



}