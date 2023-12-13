package com.github.dominik48n.helpcommands.event;

import java.util.List;
import org.jetbrains.annotations.NotNull;

public record LoadedHelpCommandsEvent(@NotNull List<String> aliases, int registeredCount) {

}
