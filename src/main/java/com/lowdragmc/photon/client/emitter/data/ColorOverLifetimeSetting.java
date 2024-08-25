package com.lowdragmc.photon.client.emitter.data;

import com.lowdragmc.lowdraglib.gui.editor.annotation.Configurable;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunction;
import com.lowdragmc.photon.client.emitter.data.number.NumberFunctionConfig;
import com.lowdragmc.photon.client.emitter.data.number.color.Gradient;
import com.lowdragmc.photon.client.emitter.data.number.color.RandomGradient;
import com.lowdragmc.photon.client.particle.LParticle;
import lombok.Getter;
import lombok.Setter;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * @author KilaBash
 * @date 2023/5/30
 * @implNote ColorOverLifetimeSetting
 */
@Getter
@OnlyIn(Dist.CLIENT)
public class ColorOverLifetimeSetting extends ToggleGroup {

    @Setter
    @Configurable(tips = "photon.emitter.config.colorOverLifetime.color")
    @NumberFunctionConfig(types = {Gradient.class, RandomGradient.class}, defaultValue = -1)
    protected NumberFunction color = new Gradient();

    public int getColor(LParticle particle, float partialTicks) {
        return color.get(particle.getT(partialTicks), () -> particle.getMemRandom(this)).intValue();
    }

}
