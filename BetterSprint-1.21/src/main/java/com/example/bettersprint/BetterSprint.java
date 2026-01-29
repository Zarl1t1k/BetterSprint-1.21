package com.example.bettersprint;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.glfw.GLFW;

@Mod("bettersprint")
public class BetterSprint {
    private static final KeyMapping sprintKey = new KeyMapping("Toggle Sprint", GLFW.GLFW_KEY_G, "Movement");
    private static boolean enabled = false;

    public BetterSprint() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventBusSubscriber(modid = "bettersprint", bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientSetup {
        @SubscribeEvent
        public static void onKeyRegister(RegisterKeyMappingsEvent event) {
            event.register(sprintKey);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null) {
            while (sprintKey.consumeClick()) {
                enabled = !enabled;
            }
            if (enabled) {
                Minecraft.getInstance().player.setSprinting(true);
            }
        }
    }
}