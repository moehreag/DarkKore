package io.github.darkkronicle.darkkore.gui.components;

import io.github.darkkronicle.darkkore.util.PositionedRectangle;
import io.github.darkkronicle.darkkore.util.Rectangle;
import lombok.Getter;
import lombok.Setter;
import net.minecraft.client.util.math.MatrixStack;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public class ListComponent extends BasicComponent {

    @Getter
    private List<Component> components = new ArrayList<>();

    @Setter
    private int width;
    @Setter
    private int height;

    private boolean vertical;

    private boolean autoUpdateWidth;
    private boolean autoUpdateHeight;

    @Getter @Setter private int leftPad = 2;
    @Getter @Setter private int rightPad = 2;
    @Getter @Setter private int topPad = 2;
    @Getter @Setter private int bottomPad = 2;

    @Getter @Setter private int componentXPad = 2;
    @Getter @Setter private int componentYPad = 2;

    @Getter private Component hoveredComponent = null;

    public ListComponent(int width, int height, boolean vertical) {
        super();
        this.width = width;
        this.height = height;
        this.vertical = vertical;
        if (width < 0) {
            autoUpdateWidth = true;
        }
        if (height < 0) {
            autoUpdateHeight = true;
        }
    }

    public void addComponent(Component component) {
        components.add(component);
        if (autoUpdateWidth) {
            updateWidth();
        }
        if (autoUpdateHeight) {
            updateHeight();
        }
    }

    public void addComponent(int index, Component component) {
        components.add(index, component);
        if (autoUpdateWidth) {
            updateWidth();
        }
        if (autoUpdateHeight) {
            updateHeight();
        }
    }

    public void setComponent(int index, Component component) {
        components.set(index, component);
        if (autoUpdateWidth) {
            updateWidth();
        }
        if (autoUpdateHeight) {
            updateHeight();
        }
    }

    public void removeComponent(int index) {
        components.remove(index);
        if (autoUpdateWidth) {
            updateWidth();
        }
        if (autoUpdateHeight) {
            updateHeight();
        }
    }

    public void clear() {
        components.clear();
        if (autoUpdateWidth) {
            updateWidth();
        }
        if (autoUpdateHeight) {
            updateHeight();
        }
    }

    public void updateWidth() {
        if (vertical) {
            int maxWidth = 0;
            for (Component component : components) {
                maxWidth = Math.max(maxWidth, component.getBoundingBox().width());
            }
            width = maxWidth + leftPad + rightPad;
            return;
        }
        int totalWidth = 0;
        for (Component component : components) {
            totalWidth += component.getBoundingBox().width() + componentXPad;
        }
        width = totalWidth + leftPad + rightPad;
    }

    public void updateHeight() {
        if (vertical) {
            int totalHeight = 0;
            for (Component component : components) {
                totalHeight += component.getBoundingBox().height() + componentYPad;
            }
            height = totalHeight + topPad + bottomPad;
            return;
        }
        int offsetRenderX = leftPad;
        int renderY = 0;
        int maxHeight = 0;
        for (Component component : components) {
            Rectangle bounds = component.getBoundingBox();
            if (bounds.width() + offsetRenderX + rightPad > width || vertical) {
                offsetRenderX = leftPad;
                renderY += maxHeight + componentYPad;
                maxHeight = 0;
            }
            offsetRenderX += bounds.width() + componentXPad;
            maxHeight = Math.max(maxHeight, bounds.height());
        }
        height = renderY + maxHeight + topPad + bottomPad;
    }

    public void forEach(Consumer<Component> componentConsumer) {
        components.forEach(componentConsumer);
    }

    @Override
    public Rectangle getBoundingBox() {
        return new Rectangle(width, height);
    }

    @Override
    public boolean mouseClicked(int x, int y, int mouseX, int mouseY) {
        if (hoveredComponent != null) {
            return hoveredComponent.mouseClicked(x, y, mouseX, mouseY);
        }
        return false;
    }

    @Override
    public boolean mouseScrolled(int x, int y, int mouseX, int mouseY, double amount) {
        if (hoveredComponent != null) {
            return hoveredComponent.mouseScrolled(x, y, mouseX, mouseY, amount);
        }
        return false;
    }

    @Override
    public void renderComponent(MatrixStack matrices, PositionedRectangle renderBounds, int x, int y, int mouseX, int mouseY) {
        int offsetRenderX = leftPad;
        int renderY = y + topPad;
        int maxHeight = 0;
        hoveredComponent = null;
        for (Component component : components) {
            Rectangle bounds = component.getBoundingBox();
            if (bounds.width() + offsetRenderX + rightPad > width || vertical) {
                offsetRenderX = leftPad;
                renderY += maxHeight + componentYPad;
                maxHeight = 0;
            }
            if (renderBounds == null || new PositionedRectangle(x + offsetRenderX, renderY, bounds.width(), bounds.height()).intersects(renderBounds)) {
                component.render(matrices, renderBounds, x + offsetRenderX, renderY, mouseX, mouseY);
            }
            offsetRenderX += bounds.width() + componentXPad;
            maxHeight = Math.max(maxHeight, bounds.height());
            if (component.isHovered()) {
                hoveredComponent = component;
            }
        }
    }

}