/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.bbide.ui.Palette;

import java.awt.Color;

/**
 *
 * @author win10
 */
public interface PaletteChangeListener {
	void activeColourChanged(Color colour);
	void paletteEntryChanged();
}
