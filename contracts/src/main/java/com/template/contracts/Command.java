package com.template.contracts;

import net.corda.core.contracts.CommandData;

public interface Command extends CommandData {

    class CreateToDoCommand implements CommandData {}
    class AssignToDoCommand implements CommandData {}

}
