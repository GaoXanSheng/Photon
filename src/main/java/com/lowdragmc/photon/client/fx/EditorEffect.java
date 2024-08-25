package com.lowdragmc.photon.client.fx;

import com.lowdragmc.lowdraglib.gui.editor.data.IProject;
import com.lowdragmc.photon.client.emitter.IParticleEmitter;
import com.lowdragmc.photon.gui.editor.ParticleEditor;
import com.lowdragmc.photon.gui.editor.ParticleProject;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jetbrains.annotations.Nullable;

/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:com/lowdragmc/photon/client/fx/EditorEffect.class */
public class EditorEffect implements IEffect {
    final ParticleEditor editor;
    final Map<String, IParticleEmitter> cache = new HashMap();

    public EditorEffect(ParticleEditor particleEditor) {
        this.editor = particleEditor;
    }

    @Override // com.lowdragmc.photon.client.fx.IEffect
    public List<IParticleEmitter> getEmitters() {
        IProject currentProject = this.editor.getCurrentProject();
        if (currentProject instanceof ParticleProject) {
            ParticleProject particleProject = (ParticleProject) currentProject;
            return particleProject.getEmitters();
        }
        return Collections.emptyList();
    }

    @Override // com.lowdragmc.photon.client.fx.IEffect
    public boolean updateEmitter(IParticleEmitter emitter) {
        return false;
    }

    @Override // com.lowdragmc.photon.client.fx.IEffect
    @Nullable
    public IParticleEmitter getEmitterByName(String name) {
        return this.cache.computeIfAbsent(name, s -> {
            return super.getEmitterByName(name);
        });
    }
}
