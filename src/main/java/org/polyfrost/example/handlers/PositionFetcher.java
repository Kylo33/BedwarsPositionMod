package org.polyfrost.example.handlers;

import cc.polyfrost.oneconfig.events.event.ChatReceiveEvent;
import cc.polyfrost.oneconfig.events.event.ChatSendEvent;
import cc.polyfrost.oneconfig.events.event.Stage;
import cc.polyfrost.oneconfig.events.event.TickEvent;
import cc.polyfrost.oneconfig.libs.eventbus.Subscribe;
import cc.polyfrost.oneconfig.utils.hypixel.HypixelUtils;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawInfo;
import cc.polyfrost.oneconfig.utils.hypixel.LocrawUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PositionFetcher {
    public static int wins = 0;
    public static int position = 0;

    private int ticks = 0;
    private boolean hidingMessages = false;
    private boolean lookingForCooldownMessage = false;

    /** Stores the message from the last hidden /mp call, to show if the player types it again within the cooldown */
    private List<IChatComponent> hiddenMessages = new ArrayList<>();

    @SubscribeEvent
    public void onWorldLoad(WorldEvent.Load event) {
        ticks = 0;
        hiddenMessages = new ArrayList<>();
    }

    @Subscribe
    private void onTick(TickEvent event) {
        if (event.stage == Stage.START) return;
        if (ticks <= 30) {
            ticks++;
        }
        if (ticks == 30) {
            if (HypixelUtils.INSTANCE.isHypixel()) {
                hidingMessages = true;
                Minecraft.getMinecraft().thePlayer.sendChatMessage("/mp bedwars");
            }
        }
    }

    private Pattern mpPattern = Pattern.compile("Lifetime (.+): (.+)");
    private Pattern endPattern = Pattern.compile("Hover over the lines for more information.");
    private Pattern bwPattern = Pattern.compile("([0-9,]+) \\(#([0-9,].+)\\/.+\\)");
    private Pattern cooldownPattern = Pattern.compile("Command Failed: This command is on cooldown! Try again in.+");
    @Subscribe
    private void onChat(ChatReceiveEvent event) {
        String message = event.getFullyUnformattedMessage();

        if (lookingForCooldownMessage) {
            Matcher cooldownMatcher = cooldownPattern.matcher(message);
            if (cooldownMatcher.matches()) {
                event.isCancelled = true;
                lookingForCooldownMessage = false;
                hiddenMessages.forEach(hiddenMessage -> {
                    Minecraft.getMinecraft().thePlayer.addChatMessage(hiddenMessage);
                });
            }
        }

        Matcher endMatcher = endPattern.matcher(event.getFullyUnformattedMessage());
        if (endMatcher.matches()) {
            if (hidingMessages) {
                event.isCancelled = true;
                hiddenMessages.add(event.message);
                hidingMessages = false;
            }

            return;
        }

        Matcher mpMatcher = mpPattern.matcher(event.getFullyUnformattedMessage());
        if (mpMatcher.matches()) {
            if (hidingMessages) {
                event.isCancelled = true;
                hiddenMessages.add(event.message);
            }

            if (!mpMatcher.group(1).equals("Solo Wins")) return;

            Matcher bedwarsMatcher = bwPattern.matcher(mpMatcher.group(2));
            if (bedwarsMatcher.matches()) {
                PositionFetcher.wins = Integer.parseInt(bedwarsMatcher.group(1).replace(",", ""));
                PositionFetcher.position = Integer.parseInt(bedwarsMatcher.group(2).replace(",", ""));
            }
        }
    }

    @Subscribe
    private void onSendChat(ChatSendEvent event) {
        if (!HypixelUtils.INSTANCE.isHypixel()) return;
        if (event.message.toLowerCase().matches("/(mp|mypos|myposition) +(bw|bedwars)( +.*)?") || (event.message.toLowerCase().matches("/(mp|mypos|myposition)( +)?") && LocrawUtil.INSTANCE.getLocrawInfo().getGameType().equals(LocrawInfo.GameType.BEDWARS))) {
            lookingForCooldownMessage = true;
        }
    }
}
