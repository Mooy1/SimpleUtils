package io.github.mooy1.slimegrid.setup;

import io.github.mooy1.infinitylib.command.LibCommand;
import io.github.mooy1.slimegrid.SlimeGrid;
import me.mrCookieSlime.Slimefun.cscorelib2.chat.ChatColors;
import org.bukkit.command.CommandSender;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public class TimingsCommand extends LibCommand {

    public TimingsCommand() {
        super("timings", "displays the timings of a slimegrid tick", true);
    }

    @Override
    public void onExecute(@Nonnull CommandSender commandSender, @Nonnull String[] strings) {
        commandSender.sendMessage(ChatColors.color("&6SlimeGrid timings: &e" + SlimeGrid.getTimings() + " ms"));
    }

    @Nonnull
    @Override
    public List<String> onTab(@Nonnull CommandSender commandSender, @Nonnull String[] strings) {
        return new ArrayList<>(0);
    }

}
