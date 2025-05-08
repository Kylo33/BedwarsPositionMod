package org.polyfrost.example.hud;

import cc.polyfrost.oneconfig.hud.SingleTextHud;
import org.polyfrost.example.config.TestConfig;
import org.polyfrost.example.handlers.PositionFetcher;

/**
 * An example OneConfig HUD that is started in the config and displays text.
 *
 * @see TestConfig#positionHUD
 */
public class PositionHUD extends SingleTextHud {
    public PositionHUD() {
        super("Position", true);
    }

    @Override
    public String getText(boolean example) {
        if (example) return "#1,234";
        return String.format("#%,d", PositionFetcher.position);
    }
}
