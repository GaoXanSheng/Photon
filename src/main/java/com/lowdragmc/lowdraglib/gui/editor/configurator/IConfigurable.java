package com.lowdragmc.lowdraglib.gui.editor.configurator;

import com.lowdragmc.lowdraglib.gui.editor.ILDLRegister;
import com.lowdragmc.lowdraglib.gui.editor.runtime.ConfiguratorParser;
import java.util.HashMap;
/**
 * @author KilaBash
 * @date 2022/12/3
 * @implNote IConfigurable
 */
public interface IConfigurable extends ILDLRegister {

    /**
     * Add configurators into given group
     * @param father father group
     */
    default void buildConfigurator(ConfiguratorGroup father) {
        ConfiguratorParser.createConfigurators(father, new HashMap<>(), getClass(), this);
    }

}