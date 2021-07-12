package com.template.contracts;

import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

// ************
// * Contract *
// ************
public class ToDoContract implements Contract {
    // This is used to identify our contract when building a transaction.
    //public static final String ID = "com.template.contracts.TemplateContract";

    @Override
    public void verify(LedgerTransaction tx) {
        System.out.println("O metodo verify() do ToDoContract e chamado");


    }



}