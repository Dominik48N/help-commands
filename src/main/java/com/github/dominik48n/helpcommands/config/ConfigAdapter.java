package com.github.dominik48n.helpcommands.config;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;
import ninja.leaping.configurate.ConfigurationNode;
import ninja.leaping.configurate.gson.GsonConfigurationLoader;
import org.jetbrains.annotations.NotNull;

public class ConfigAdapter {

    private final @NotNull ConfigurationNode root;

    public ConfigAdapter(final @NotNull Path configPath) throws IOException {
        this.root = GsonConfigurationLoader.builder().setPath(configPath).build().load();
    }

    public @NotNull List<? extends ConfigurationNode> getChildrenList() {
        return this.root.getChildrenList();
    }
}
