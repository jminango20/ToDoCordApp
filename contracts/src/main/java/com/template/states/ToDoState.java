package com.template.states;



// *********
// * State *
// *********

import com.template.contracts.TemplateContract;
import net.corda.core.contracts.BelongsToContract;
import net.corda.core.contracts.ContractState;
import net.corda.core.contracts.LinearState;
import net.corda.core.contracts.UniqueIdentifier;
import net.corda.core.identity.AbstractParty;
import net.corda.core.identity.Party;
import net.corda.core.serialization.ConstructorForDeserialization;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.List;

@BelongsToContract(TemplateContract.class)
public class ToDoState implements ContractState, LinearState {

    private final Party assignedBy;
    private final Party assignedTo;
    private final String taskDescription;
    private UniqueIdentifier linearId;

    public ToDoState(Party assignedBy, Party assignedTo, String taskDescription) {
        this.assignedBy = assignedBy;
        this.assignedTo = assignedTo;
        this.taskDescription = taskDescription;
        this.linearId = new UniqueIdentifier(); //Produz a UUID;
    }

    //Como ToDoState es uma classe inmutavel so adiciono os getters
    public Party getAssignedBy() {return assignedBy;}
    public Party getAssignedTo() {return assignedTo;}
    public String getTaskDescription() {return taskDescription;}

    //Sobrescrita dos metodos
    @NotNull
    @Override
    public List<AbstractParty> getParticipants() {
        return Arrays.asList(assignedBy,assignedTo);
    }

    @NotNull
    @Override
    public UniqueIdentifier getLinearId() {
        System.out.println(linearId);
        return linearId;
    }

    //Novo Contrustor
    @ConstructorForDeserialization
    public ToDoState(Party assignedBy, Party assignedTo, String taskDescription, UniqueIdentifier linearId) {
        this.assignedBy = assignedBy;
        this.assignedTo = assignedTo;
        this.taskDescription = taskDescription;
        this.linearId = linearId;
    }
    //Metodo para Assignar a tarefa
    public ToDoState assign(Party assignedTo){
        return new ToDoState(assignedBy,assignedTo,taskDescription,linearId);
    }


}
