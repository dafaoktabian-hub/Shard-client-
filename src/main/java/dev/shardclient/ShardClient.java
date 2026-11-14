
package dev.shardclient;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.gui.widget.ButtonWidget;
import net.minecraft.client.util.InputUtil;
import net.minecraft.text.Text;
import org.lwjgl.glfw.GLFW;

public class ShardClient implements ClientModInitializer {

    private static KeyBinding openKey;

    @Override
    public void onInitializeClient() {
        openKey = KeyBindingHelper.registerKeyBinding(
            new KeyBinding(
                "key.shardclient.open",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_M,
                "category.shardclient"
            )
        );

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            while (openKey.wasPressed()) {
                if (client.currentScreen == null) {
                    client.setScreen(new ShardScreen());
                }
            }
        });
    }

    public static class ShardScreen extends Screen {

        private static final String[] MODULES = {
            "X-Ray",
            "Chest ESP",
            "Block ESP",
            "Entity ESP",
            "Freecam",
            "Fullbright",
            "Auto Mine"
        };

        private final boolean[] enabled =
            new boolean[MODULES.length];

        protected ShardScreen() {
            super(Text.literal("Shard Client"));
        }

        @Override
        protected void init() {
            int width = 150;
            int height = 22;
            int gap = 5;
            int startX = (this.width - width) / 2;
            int startY = 65;

            for (int i = 0; i < MODULES.length; i++) {
                final int index = i;

                ButtonWidget button = ButtonWidget.builder(
                    getModuleText(index),
                    b -> {
                        enabled[index] = !enabled[index];
                        b.setMessage(getModuleText(index));
                    }
                ).dimensions(
                    startX,
                    startY + i * (height + gap),
                    width,
                    height
                ).build();

                addDrawableChild(button);
            }
        }

        private Text getModuleText(int index) {
            return Text.literal(
                MODULES[index] + ": " +
                (enabled[index] ? "ON" : "OFF")
            );
        }

        @Override
        public void render(
            DrawContext context,
            int mouseX,
            int mouseY,
            float delta
        ) {
            this.renderBackground(context, mouseX, mouseY, delta);

            context.fill(
                0, 0, this.width, this.height,
                0xCC080D18
            );

            context.fill(
                this.width / 2 - 110,
                20,
                this.width / 2 + 110,
                this.height - 15,
                0xEE101B30
            );

            context.fill(
                this.width / 2 - 110,
                20,
                this.width / 2 + 110,
                23,
                0xFF168BFF
            );

            context.drawCenteredTextWithShadow(
                this.textRenderer,
                "SHARD CLIENT",
                this.width / 2,
                35,
                0xFF55AAFF
            );

            super.render(context, mouseX, mouseY, delta);
        }

        @Override
        public boolean shouldPause() {
            return false;
        }
    }
}
