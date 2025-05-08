package org.polyfrost.example.hud;

import cc.polyfrost.oneconfig.hud.SingleTextHud;
import org.polyfrost.example.config.TestConfig;
import org.polyfrost.example.handlers.PositionFetcher;

/**
 * An example OneConfig HUD that is started in the config and displays text.
 *
 * @see TestConfig#winsHUD
 */
public class WinsHUD extends SingleTextHud {
    public WinsHUD() {
        super("Wins", true);
    }

    @Override
    public String getText(boolean example) {
        if (example) return "12,345";
        return String.format("%,d", PositionFetcher.wins);
    }
}
