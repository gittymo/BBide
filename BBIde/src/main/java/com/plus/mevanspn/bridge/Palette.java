/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.bridge;

import java.awt.image.*;

/**
 *
 * @author win10
 */
public class Palette {
	public Palette(String name, int entries) {
		// Sanity check:  Make sure number of palette entries is sensible.
		if (entries < 2 || entries > 16) entries = 2;
		setName(name);
		
		// Initialise the array that will hold the colour values used by the indices
		redComponents = new byte[entries];
		greenComponents = new byte[entries];
		blueComponents = new byte[entries];
		for (int i = 0; i < entries; i++) {
			redComponents[i] = greenComponents[i] = blueComponents[i] = 0;
		}
		
		// Determine the number of bits used for pixel indices
		final int indexSize = (entries < 3) ? 1 : (entries < 5) ? 2 : 4;
		// Create a colour model that uses the newly created colour array.
		colourModel = new IndexColorModel(indexSize, entries, redComponents,
			greenComponents, blueComponents);
	}
	
	final public void setName(String name) {
		if (name == null || name.trim().isEmpty()) {
			name = "Palette" + Palette.paletteCount++;
		}
		
		this.name = name;
	}
	
	public String getName() {
		return name;
	}
	
	public IndexColorModel getColourModel() {
		return colourModel;
	}
	
	private String name;
	private IndexColorModel colourModel;
	private byte[] redComponents;
	private byte[] greenComponents;
	private byte[] blueComponents;
	
	private static int paletteCount = 0;
}
