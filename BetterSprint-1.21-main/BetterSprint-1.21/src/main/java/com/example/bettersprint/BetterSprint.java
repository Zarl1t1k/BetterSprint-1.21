package net.zarl1t1k.bettersprint;

import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.lwjgl.glfw.GLFW;

@Mod("bettersprint")
public class BetterSprint {
    // Создаем кнопку B в настройках
    public static final KeyMapping BIND = new KeyMapping("Автоспринт: Вкл/Выкл", GLFW.GLFW_KEY_B, "Better Sprint");
    private boolean isEnabled = true;

    public BetterSprint() {
        MinecraftForge.EVENT_BUS.register(this);
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onKeyReg);
    }

    public void onKeyReg(RegisterKeyMappingsEvent event) {
        event.register(BIND);
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.END && Minecraft.getInstance().player != null) {
            var player = Minecraft.getInstance().player;

            // Проверяем нажатие кнопки B
            while (BIND.consumeClick()) {
                isEnabled = !isEnabled;
                player.displayClientMessage(Component.literal("Автоспринт: " + (isEnabled ? "§aВКЛ" : "§cВЫКЛ")), true);
            }

            // Логика бега
            if (isEnabled && player.input.hasForwardImpulse() && !player.isShiftKeyDown() && !player.isUsingItem()) {
                if (!player.isSprinting() && player.getFoodData().getFoodLevel() > 6) {
                    player.setSprinting(true);
                }
            }
        }
    }
}
