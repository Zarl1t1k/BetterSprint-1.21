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

    // 1. Создаем саму кнопку. 
    // "key.bettersprint.toggle" — это её внутреннее имя.
    // GLFW_KEY_B — это та самая английская "B".
    public static final KeyMapping MY_KEY = new KeyMapping(
            "Вкл/Выкл Автоспринт", 
            InputConstants.Type.KEYSYM,
            GLFW.GLFW_KEY_B, 
            "Better Sprint Мод"
    );

    // 2. Это «флажок». Если true — мод работает, если false — отдыхает.
    private boolean isEnabled = true;

    public BetterSprint() {
        // Говорим игре: "Слушай этот класс, тут будут события!"
        MinecraftForge.EVENT_BUS.register(this);
        
        // Говорим игре: "У нас есть новая кнопка, зарегистрируй её!"
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::onRegisterKeys);
    }

    // Этот метод просто добавляет кнопку в настройки игры
    public void onRegisterKeys(RegisterKeyMappingsEvent event) {
        event.register(MY_KEY);
    }

    // Это сердце мода. Этот метод вызывается 20 раз в секунду (каждый тик игры).
    @SubscribeEvent
    public void onGameTick(TickEvent.ClientTickEvent event) {
        // Нам нужно проверять нажатие кнопки только в конце игрового тика
        if (event.phase != TickEvent.Phase.END) return;

        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null) return; // Если мы еще не в мире, ничего не делаем

        // 3. Проверяем: нажал ли игрок кнопку B?
        while (MY_KEY.consumeClick()) {
            isEnabled = !isEnabled; // Меняем состояние (с вкл на выкл и наоборот)
            
            // Пишем игроку в чат, что произошло
            String message = isEnabled ? "Автоспринт ВКЛ" : "Автоспринт ВЫКЛ";
            mc.player.displayClientMessage(Component.literal(message), true);
        }

        // 4. Магия бега
        if (isEnabled) {
            LocalPlayer player = mc.player;
            
            // Если игрок жмет кнопку "вперед" (W)
            if (player.input.hasForwardImpulse()) {
                // Если он при этом не крадется и не ест яблоко/пьет зелье
                if (!player.isShiftKeyDown() && !player.isUsingItem()) {
                    // ВКЛЮЧАЕМ БЕГ!
                    player.setSprinting(true);
                }
            }
        }
    }
