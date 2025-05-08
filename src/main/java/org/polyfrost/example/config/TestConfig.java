package org.polyfrost.example.config;

import org.polyfrost.example.ExampleMod;
import org.polyfrost.example.hud.PositionHUD;
import org.polyfrost.example.hud.WinsHUD;
import cc.polyfrost.oneconfig.config.Config;
import cc.polyfrost.oneconfig.config.annotations.Dropdown;
import cc.polyfrost.oneconfig.config.annotations.HUD;
import cc.polyfrost.oneconfig.config.annotations.Slider;
import cc.polyfrost.oneconfig.config.annotations.Switch;
import cc.polyfrost.oneconfig.config.data.Mod;
import cc.polyfrost.oneconfig.config.data.ModType;
import cc.polyfrost.oneconfig.config.data.OptionSize;

/**
 * The main Config entrypoint that extends the Config type and inits the config options.
 * See <a href="https://docs.polyfrost.cc/oneconfig/config/adding-options">this link</a> for more config Options
 */
public class TestConfig extends Config {
    @HUD(
            name = "Wins"
    )
    public WinsHUD winsHUD = new WinsHUD();

    @HUD(
            name = "Leaderboard Position"
    )
    public PositionHUD positionHUD = new PositionHUD();

    public TestConfig() {
        super(new Mod(ExampleMod.NAME, ModType.UTIL_QOL), ExampleMod.MODID + ".json");
        initialize();
    }
}