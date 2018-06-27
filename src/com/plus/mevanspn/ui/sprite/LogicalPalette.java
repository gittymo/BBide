/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.ui.sprite;

/**
 *
 * @author win10
 */
public class LogicalPalette {
    private int[] indices;
    
    private LogicalPalette(int entries) {
        indices = new int[entries];
        for (int i = 0; i < entries; i++)
            indices[i] = i;
    }   // Private constructor will create a basic 1 to 1 mapped palette.
    
    public void mapLogicalToPhysical(int logical_colour, int physical_colour) {
        indices[logical_colour] = physical_colour;
    }
    
    static public LogicalPalette createMonochromePalette() {
        LogicalPalette mono_palette = new LogicalPalette(2);
        mono_palette.mapLogicalToPhysical(1, 7);
        return mono_palette;
    }
    
    static public LogicalPalette createFourColourPalette() {
        LogicalPalette four_colour_palette = new LogicalPalette(4);
        four_colour_palette.mapLogicalToPhysical(2, 3);
        four_colour_palette.mapLogicalToPhysical(3, 7);
        return four_colour_palette;
    }
    
    static public LogicalPalette createSixteenColourPalette() {
        return new LogicalPalette(16);
    }
}
