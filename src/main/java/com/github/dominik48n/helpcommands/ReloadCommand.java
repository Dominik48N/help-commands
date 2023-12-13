package com.github.dominik48n.helpcommands;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.jetbrains.annotations.NotNull;

public class ReloadCommand implements SimpleCommand {

    private final @NotNull HelpCommandsPlugin plugin;

    ReloadCommand(final @NotNull HelpCommandsPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public void execute(final Invocation invocation) {
        this.plugin.reloadCommands();
        invocation.source().sendMessage(Component.text("You have reloaded all help commands.", NamedTextColor.GREEN));
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return invocation.source().hasPermission("helpcommands.reload");
    }
}
