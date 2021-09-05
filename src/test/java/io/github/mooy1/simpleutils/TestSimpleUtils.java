package io.github.mooy1.simpleutils;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import be.seeseemelk.mockbukkit.MockBukkit;
import io.github.thebusybiscuit.slimefun4.implementation.Slimefun;
import io.github.thebusybiscuit.slimefun4.implementation.setup.SlimefunItemSetup;

class TestSimpleUtils {

    @BeforeAll
    public static void load() {
        MockBukkit.mock();
        SlimefunItemSetup.setup(MockBukkit.load(Slimefun.class));
    }

    @AfterAll
    public static void unload() {
        MockBukkit.unmock();
    }

    @Test
    void testLoad() {
        MockBukkit.load(SimpleUtils.class);
    }

}
