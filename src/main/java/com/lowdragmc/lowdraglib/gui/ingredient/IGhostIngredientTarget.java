package com.lowdragmc.lowdraglib.gui.ingredient;

import java.util.List;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


public interface IGhostIngredientTarget {

    @OnlyIn(Dist.CLIENT)
    List<Target> getPhantomTargets(Object ingredient);

}