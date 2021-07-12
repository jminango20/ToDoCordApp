package com.template.contracts;

import com.template.states.ToDoState;
import net.corda.core.contracts.CommandData;
import net.corda.core.contracts.CommandWithParties;
import net.corda.core.contracts.Contract;
import net.corda.core.transactions.LedgerTransaction;

import java.util.List;

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
        //Using Commands
        List<CommandWithParties<CommandData>> commands = tx.getCommands();
        CommandData command = commands.get(0).getValue();
        ToDoState toDoOutput = (ToDoState) tx.getOutputStates().get(0);

        if (command instanceof Command.CreateToDoCommand){
            requireThat(r -> {
                r.using("Tarefa em branco.", !toDoOutput.getTaskDescription().trim().equals(""));
                r.using("Tarefa tem tamanho maior a 25 caracteres", toDoOutput.getTaskDescription().length()<25);
                return null;
            });
        } else if (command instanceof Command.AssignToDoCommand){
            ToDoState toDoInput = (ToDoState) tx.getInputStates().get(0);
            requireThat(r ->{
               r.using("Ja assignado para um Party", !toDoInput.getAssignedTo().equals(toDoOutput.getAssignedTo()));
               return null;
            });
        } else {
            System.out.println("Commando nao encontrado no Contract");
        }
    }
}