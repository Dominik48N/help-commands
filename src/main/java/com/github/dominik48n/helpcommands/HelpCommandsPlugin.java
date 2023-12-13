package com.github.dominik48n.helpcommands;

import static com.github.dominik48n.helpcommands.Constants.CONFIG_NAME;
import static com.github.dominik48n.helpcommands.Constants.VERSION;
import com.github.dominik48n.helpcommands.config.ConfigAdapter;
import com.google.common.reflect.TypeToken;
import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.plugin.annotation.DataDirectory;
import com.velocitypowered.api.proxy.ProxyServer;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.objectmapping.ObjectMappingException;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

@Plugin(
    id = "help-commands",
    name = "Help Commands",
    version = VERSION,
    url = "https://github.com/Dominik48N/help-commands",
    description = "Create help commands via a config",
    authors = {"Dominik48N"}
)
public class HelpCommandsPlugin {

    private final @NotNull ProxyServer server;
    private final @NotNull Logger logger;
    private final @NotNull Path dataFolder;

    @Inject
    public HelpCommandsPlugin(final @NotNull ProxyServer server, final @NotNull Logger logger, final @NotNull @DataDirectory Path dataFolder) {
        this.server = server;
        this.logger = logger;
        this.dataFolder = dataFolder;
    }

    @Subscribe
    public void handleProxyInitialize(final ProxyInitializeEvent event) {
        final File configFile = new File(this.dataFolder.toFile(), CONFIG_NAME);
        if (configFile.getParentFile().mkdirs() || !configFile.exists()) {
            try (final InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(CONFIG_NAME)) {
                if (inputStream == null)
                    throw new RuntimeException("config file for help commands is no resource.");

                Files.copy(inputStream, configFile.toPath());
            } catch (final IOException e) {
                this.logger.error("Cannot write help commands config.", e);
            }
        }

        this.reloadCommands(configFile);
    }

    public void reloadCommands(final @NotNull File configFile) {
        final ConfigAdapter configAdapter;
        try {
            configAdapter = new ConfigAdapter(configFile.toPath());
        } catch (final IOException e) {
            this.logger.error("Cannot read help commands config.", e);
            return;
        }

        final TypeToken<String> stringToken = TypeToken.of(String.class);
        for (final ConfigurationNode node : configAdapter.getChildrenList()) {
            final List<String> aliases;
            try {
                aliases = node.getNode("alias").getList(stringToken, new ArrayList<>());
            } catch (final ObjectMappingException e) {
                this.logger.error("Cannot get command aliases.", e);
                continue;
            }

            if (aliases.isEmpty()) {
                continue;
            }

            this.server.getCommandManager().register(
                this.server.getCommandManager()
                    .metaBuilder(aliases.getFirst())
                    .aliases(aliases.size() > 1 ? aliases.subList(1, aliases.size()).toArray(String[]::new) : new String[0])
                    .plugin(this)
                    .build(),
                new HelpCommand(node)
            );
        }
    }
}
