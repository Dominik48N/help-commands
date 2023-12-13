package com.github.dominik48n.helpcommands;

import com.velocitypowered.api.command.SimpleCommand;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import ninja.leaping.configurate.ConfigurationNode;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class HelpCommand implements SimpleCommand {

    private final @NotNull Component message;
    private final @Nullable String permission;

    HelpCommand(final @NotNull ConfigurationNode config) throws RuntimeException {
        this.permission = config.getNode("permission").getString();
        this.message = MiniMessage.miniMessage().deserialize(config.getNode("message").getString("<red>This command is not configured."));
    }

    @Override
    public void execute(final Invocation invocation) {
        invocation.source().sendMessage(this.message);
    }

    @Override
    public boolean hasPermission(final Invocation invocation) {
        return this.permission == null || invocation.source().hasPermission(this.permission);
    }
}
