package com.lowdragmc.lowdraglib.gui.widget.custom;

import com.lowdragmc.lowdraglib.gui.editor.annotation.LDLRegister;
import com.lowdragmc.lowdraglib.gui.texture.ResourceBorderTexture;
import com.lowdragmc.lowdraglib.gui.widget.SlotWidget;
import com.lowdragmc.lowdraglib.gui.widget.Widget;
import com.lowdragmc.lowdraglib.gui.widget.WidgetGroup;
import com.lowdragmc.lowdraglib.utils.Position;
import net.minecraft.world.entity.player.Player;

@LDLRegister(name = "player_inventory", group = "widget.custom")
/* loaded from: photon-forge-1.19.2-1.0.7.a.jar:META-INF/jars/ldlib-forge-1.19.2-1.0.21.a.jar:com/lowdragmc/lowdraglib/gui/widget/custom/PlayerInventoryWidget.class */
public class PlayerInventoryWidget extends WidgetGroup {
    public PlayerInventoryWidget() {
        super(0, 0, 172, 86);
        setBackground(ResourceBorderTexture.BORDERED_BACKGROUND);
        for (int col = 0; col < 9; col++) {
            String id = "player_inv_" + col;
            Position pos = new Position(5 + (col * 18), 63);
            SlotWidget slot = new SlotWidget();
            slot.initTemplate();
            slot.setSelfPosition(pos);
            slot.setId(id);
            addWidget(slot);
        }
        for (int row = 0; row < 3; row++) {
            for (int col2 = 0; col2 < 9; col2++) {
                String id2 = "player_inv_" + (col2 + ((row + 1) * 9));
                Position pos2 = new Position(5 + (col2 * 18), 5 + (row * 18));
                SlotWidget slot2 = new SlotWidget();
                slot2.initTemplate();
                slot2.setSelfPosition(pos2);
                slot2.setId(id2);
                addWidget(slot2);
            }
        }
    }

    public void setPlayer(Player entityPlayer) {
        int i = 0;
        while (i < this.widgets.size()) {
            Widget widget = this.widgets.get(i);
            if (widget instanceof SlotWidget) {
                SlotWidget slotWidget = (SlotWidget) widget;
                slotWidget.setContainerSlot(entityPlayer.m_150109_(), i);
                slotWidget.setLocationInfo(true, i < 9);
            }
            i++;
        }
    }
}
