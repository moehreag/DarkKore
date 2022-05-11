package io.github.darkkronicle.darkkore;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class DarkKore implements ClientModInitializer {

    public final static String MOD_ID = "darkkore";

    public static final Logger LOGGER = LogManager.getLogger(MOD_ID);

    @Override
    public void onInitializeClient() {
        KeyBinding keyBinding =
                new KeyBinding(
                        "refinedcreativeinventory.key.test",
                        InputUtil.Type.KEYSYM,
                        GLFW.GLFW_KEY_U,
                        "category.keys");
        ClientTickEvents.START_CLIENT_TICK.register(
                s -> {
                    if (keyBinding.wasPressed()) {
                        MinecraftClient.getInstance().setScreen(new TestScreen());
                    }
                });
    }
}
