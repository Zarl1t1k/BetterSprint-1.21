@Mod(BetterSprint.MODID)
public class BetterSprint {
    public static final String MODID = "bettersprint";

    public BetterSprint() {
        // Обязательно регистрируем обработчик событий
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        // Выполняем только на клиенте и только в конце тика
        if (event.side.isClient() && event.phase == TickEvent.Phase.END) {
            Minecraft mc = Minecraft.getInstance();
            LocalPlayer player = mc.player;

            if (player != null && player.input.hasForwardImpulse()) {
                // Условие: не крадется, не использует предмет (еду/лук) и может бежать
                if (!player.isShiftKeyDown() && !player.isUsingItem() && !player.isSprinting()) {
                    player.setSprinting(true);
                }
            }
        }
    }
}
