package io.github.darkkronicle.darkkore.util.render;

import io.github.darkkronicle.darkkore.DarkKore;
import org.ladysnake.satin.api.managed.ManagedCoreShader;
import org.ladysnake.satin.api.managed.ShaderEffectManager;
import net.minecraft.client.render.VertexFormats;
import net.minecraft.util.Identifier;

public class ShaderHandler {

    public final static ManagedCoreShader CHROMA = ShaderEffectManager.getInstance().manageCoreShader(Identifier.of(DarkKore.MOD_ID, "darkkore_chroma"), VertexFormats.POSITION_COLOR);

    public final static ManagedCoreShader CIRCLE = ShaderEffectManager.getInstance().manageCoreShader(Identifier.of(DarkKore.MOD_ID, "darkkore_circle"), VertexFormats.POSITION_COLOR);

    public static void initialize() {
        // We just want chroma and circle to load
    }

}
