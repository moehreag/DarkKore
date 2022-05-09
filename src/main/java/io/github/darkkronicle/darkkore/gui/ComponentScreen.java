package io.github.darkkronicle.darkkore.gui;

import io.github.darkkronicle.darkkore.gui.components.Component;
import io.github.darkkronicle.darkkore.util.RenderUtil;
import lombok.Getter;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

public abstract class ComponentScreen extends Screen {

    @Getter
    private List<Component> components = new ArrayList<>();

    protected ComponentScreen() {
        super(new LiteralText(""));
    }

    public void addComponent(Component component) {
        components.add(component);
    }

    public void renderComponents(MatrixStack matrices, int mouseX, int mouseY) {
        for (Component component : components) {
            component.render(matrices, null, 0, 0, mouseX, mouseY);
        }
    }

    @Override
    protected void init() {
        components.clear();
        initImpl();
    }

    public abstract void initImpl();

    @Override
    public void render(MatrixStack matrices, int mouseX, int mouseY, float partialTicks) {
        RenderUtil.drawRectangle(matrices, 0, 0, this.width, this.height, 0xB0000000);
        renderComponents(matrices, mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        for (Component component : components) {
            if (component.isHovered()) {
                return component.mouseClicked(0, 0, (int) mouseX, (int) mouseY);
            }
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
        for (Component component : components) {
            if (component.isHovered()) {
                return component.mouseScrolled(0, 0, (int) mouseX, (int) mouseY, amount);
            }
        }
        return false;
    }
}