package com.lowdragmc.lowdraglib.client.shader.management;

import com.lowdragmc.lowdraglib.LDLib;
import com.mojang.blaze3d.platform.GlStateManager;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Objects;
import java.util.Optional;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.io.IOUtils;
import org.lwjgl.opengl.GL20;

@OnlyIn(Dist.CLIENT)
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/management/Shader.class */
public class Shader {
    public final ShaderType shaderType;
    public final String source;
    private int shaderId;
    private boolean isCompiled;

    public Shader(ShaderType type, String source) {
        this.shaderType = type;
        this.source = source;
        this.shaderId = GL20.glCreateShader(this.shaderType.shaderMode);
        if (this.shaderId == 0) {
            LDLib.LOGGER.error("GL Shader Allocation Fail!");
            throw new RuntimeException("GL Shader Allocation Fail!");
        }
    }

    public void attachShader(ShaderProgram program) {
        if (!this.isCompiled) {
            compileShader();
        }
        GlStateManager.m_84423_(program.programId, this.shaderId);
    }

    public void deleteShader() {
        if (this.shaderId == 0) {
            return;
        }
        GlStateManager.m_84421_(this.shaderId);
        this.shaderId = 0;
    }

    public Shader compileShader() {
        if (!this.isCompiled && this.shaderId != 0) {
            GL20.glShaderSource(this.shaderId, this.source);
            GL20.glCompileShader(this.shaderId);
            if (GL20.glGetShaderi(this.shaderId, 35713) == 0) {
                int maxLength = GL20.glGetShaderi(this.shaderId, 35716);
                String error = String.format("Unable to compile %s shader object:\n%s", this.shaderType.name(), GL20.glGetShaderInfoLog(this.shaderId, maxLength));
                LDLib.LOGGER.error(error);
            }
            this.isCompiled = true;
        }
        return this;
    }

    public static Shader loadShader(ShaderType type, String rawShader) {
        return new Shader(type, rawShader).compileShader();
    }

    public static Shader loadShader(ShaderType type, ResourceLocation resourceLocation) throws IOException {
        Optional<Resource> resource = Minecraft.m_91087_().m_91098_().m_213713_(resourceLocation);
        if (resource.isPresent()) {
            Resource iresource = resource.get();
            InputStream stream = iresource.m_215507_();
            StringBuilder sb = new StringBuilder();
            BufferedReader bin = new BufferedReader(new InputStreamReader(stream));
            while (true) {
                String line = bin.readLine();
                if (line != null) {
                    sb.append(line).append('\n');
                } else {
                    stream.close();
                    IOUtils.closeQuietly(stream);
                    return loadShader(type, sb.toString());
                }
            }
        } else {
            throw new IOException("find no resource " + resourceLocation);
        }
    }

    /* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/client/shader/management/Shader$ShaderType.class */
    public enum ShaderType {
        VERTEX("vertex", ".vsh", 35633),
        FRAGMENT("fragment", ".fsh", 35632);
        
        public final String shaderName;
        public final String shaderExtension;
        public final int shaderMode;

        ShaderType(String shaderNameIn, String shaderExtensionIn, int shaderModeIn) {
            this.shaderName = shaderNameIn;
            this.shaderExtension = shaderExtensionIn;
            this.shaderMode = shaderModeIn;
        }
    }

    public boolean equals(Object obj) {
        if (super.equals(obj)) {
            return true;
        }
        if (obj instanceof Shader) {
            Shader shader = (Shader) obj;
            return Objects.equals(shader.source, this.source) && shader.shaderType == this.shaderType;
        }
        return false;
    }

    public int hashCode() {
        return Objects.hash(this.shaderType, this.source);
    }
}
