package net.zarlit1k.bettersprint;

import com.mojang.blaze3d.platform.InputConstants;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod(BetterSprint.MODID)
public class BetterSprint {
    public static final String MODID = "bettersprint";

    // Создаем кнопку B
    public static final KeyMapping TOGGLE_KEY = new KeyMapping(
            "Включить Автоспринт", 
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B, 
            "Better Sprint"
    );

    // Флажок: включен мод или нет
    private boolean enabled = true;

    public BetterSprint() {
        // Регистрация событий тика (каждый кадр игры)
        MinecraftForge.EVENT_BUS.register(this);
        // Регистрация кнопки в меню настроек
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::registerKey);
    }

    public void registerKey(RegisterKeyMappingsEvent event) {
        event.register(TOGGLE_KEY);
    }

    @SubscribeEvent
    public void onClientTick(TickEvent.ClientTickEvent event) {
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return;

        // Если нажали B — меняем состояние
        while (TOGGLE_KEY.consumeClick()) {
            enabled = !enabled;
            String text = enabled ? "§aВКЛ" : "§cВЫКЛ";
            mc.player.displayClientMessage(Component.literal("Автоспринт: " + text), true);
        }

        // Если включено — заставляем бежать
        if (enabled) {
            LocalPlayer player = mc.player;
            if (player.input.hasForwardImpulse() && !player.isShiftKeyDown() && !player.isUsingItem()) {
                if (!player.isSprinting() && player.getFoodData().getFoodLevel() > 6) {
                    player.setSprinting(true);
                }
            }
        }
    }
}
