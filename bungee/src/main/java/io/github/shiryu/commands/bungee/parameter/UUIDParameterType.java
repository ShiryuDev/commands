package io.github.shiryu.commands.bungee.parameter;

import io.github.shiryu.commands.api.locale.CommandLocale;
import io.github.shiryu.commands.api.parameter.ParameterType;
import io.github.shiryu.commands.api.sender.SimpleSender;
import io.github.shiryu.commands.bungee.BungeeSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class UUIDParameterType implements ParameterType<UUID> {

    @NotNull
    @Override
    public UUID transform(@NotNull SimpleSender sender, @NotNull String value) {
        if (sender instanceof BungeeSender && (value.equalsIgnoreCase("self") || value.isEmpty())){
            final BungeeSender bungeeSender = (BungeeSender) sender;

            if (bungeeSender.getSender() instanceof ProxiedPlayer)
                return ((ProxiedPlayer)bungeeSender.getSender()).getUniqueId();
        }

        try{
            return UUID.fromString(value);
        }catch (Exception e){
            sender.sendMessage(StringUtils.replace(CommandLocale.NOT_FOUND, "%s", value));

            return null;
        }
    }

    @Override
    public @NotNull List<String> tabComplete(@NotNull SimpleSender sender, @NotNull Set<String> flags, @NotNull String value) {
        return ProxyServer.getInstance().getPlayers().stream()
                .map(ProxiedPlayer::getName)
                .filter(s -> StringUtils.startsWithIgnoreCase(s, value))
                .collect(Collectors.toList());
    }
}
