package io.github.mooy1.gridfoundation.implementation.upgrades;

import me.mrCookieSlime.Slimefun.api.SlimefunItemStack;
import org.bukkit.Material;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A SlimefunItemStack which adds the lore and pdc for the default upgrade tier
 */
public final class UpgradeItemStack extends SlimefunItemStack {

    public UpgradeItemStack(@Nonnull String id, @Nonnull Material type, @Nullable String name, String... lore) {
        super(id, type, name, meta -> {
            List<String> list = new ArrayList<>(Arrays.asList(lore));
            meta.setLore(list);
        });
    }

}
