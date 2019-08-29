/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.ui.Palette;

import com.plus.mevanspn.ui.Palette.PaletteChangeListener;
import com.plus.mevanspn.bridge.Palette;
import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

import java.awt.event.*;
import java.awt.image.*;

/**
 *
 * @author win10
 */
public class PalettePanel extends JPanel {
	public PalettePanel(Palette palette) {
		super();
		
		this.palette = palette;
		this.paletteChangeListeners = new ArrayList<>();
		
		createAndAddPaletteButtons();
	}
	
	private void createAndAddPaletteButtons() {
		// Get the IndexColorModel to retrieve colour entries from the palette.
		IndexColorModel icm = palette.getColourModel();
				
		// Get an array containing the colour entries.
		final int MAP_SIZE = icm.getMapSize();
		int[] entries = new int[MAP_SIZE];
		icm.getRGBs(entries);
		
		// Add a colour preview button to the panel for each palette entry.
		int index = 0;
		for (int entry : entries) {
			ColourPreviewButton cpb = 
							new ColourPreviewButton(new Color(entry), index++, this);
			this.add(cpb);
		}
		
		// TODO: Add a currently selected colour preview panel to the palette panel.
	}
	
	private void paletteChanged() {
		for (PaletteChangeListener pcl : paletteChangeListeners) {
			pcl.paletteEntryChanged();
		}
	}
	
	public void addPaletteChangeListener(PaletteChangeListener pcl) {
		paletteChangeListeners.add(pcl);
	}
	
	private Palette palette;
	private Color activeColour;
	private ArrayList<PaletteChangeListener> paletteChangeListeners;
	
	final class ColourPreviewButton extends JButton {
		ColourPreviewButton(Color colour, int index, PalettePanel parentPanel) {
			super();
			setColour(colour);
			this.index = index;
			this.parentPanel = parentPanel;
			this.setPreferredSize(new Dimension(32,32));
			this.setSize(32,32);
		}
		
		void setColour(Color colour) {
			if (colour != null && colour.getRGB() != this.colour.getRGB()) {
				this.colour = colour;
				parentPanel.paletteChanged();
			}
		}
		
		Color getColour() {
			return this.colour;
		}
		
		int getIndex() {
			return this.index;
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint(colour);
		}
		
		private Color colour;
		private int index;
		private PalettePanel parentPanel;
	}
}
