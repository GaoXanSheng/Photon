package com.lowdragmc.lowdraglib.utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import javax.annotation.ParametersAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.SharedConstants;
import net.minecraft.server.packs.FolderPackResources;
import net.minecraft.server.packs.PackType;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/utils/CustomResourcePack.class */
public class CustomResourcePack extends FolderPackResources {
    private final PackType type;
    private final String namespace;

    public CustomResourcePack(File location, String namespace, PackType type) {
        super(location);
        new File(location, "assets/" + namespace).mkdirs();
        this.namespace = namespace;
        this.type = type;
    }

    protected InputStream m_5541_(String resourcePath) throws IOException {
        if ("pack.mcmeta".equals(resourcePath)) {
            return new ByteArrayInputStream("{\n    \"pack\": {\n        \"description\": \"Generated resources for %s\",\n        \"pack_format\": %d\n    }\n}\n".formatted(new Object[]{this.namespace, Integer.valueOf(this.type.m_143756_(SharedConstants.m_183709_()))}).getBytes());
        }
        return super.m_5541_(resourcePath);
    }
}
