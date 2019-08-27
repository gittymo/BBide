/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.ui;

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
		IndexColorModel icm = palette.getColourModel();
		
	}
	
	public void addPaletteChangeListener(PaletteChangeListener pcl) {
		paletteChangeListeners.add(pcl);
	}
	
	private Palette palette;
	private Color activeColour;
	private ArrayList<PaletteChangeListener> paletteChangeListeners;
	
	final class ColourPreviewButton extends JButton {
		ColourPreviewButton(Color colour, PalettePanel parentPanel) {
			setColour(colour);
			this.parentPanel = parentPanel;
		}
		
		void setColour(Color colour) {
			if (colour != null && colour.getRGB() != this.colour.getRGB()) {
				this.colour = colour;
				// TODO: Change pallete listener to bridge, add colour change functionality.
			}
		}
		
		@Override
		public void paintComponent(Graphics g) {
			super.paintComponent(g);
			
			Graphics2D g2 = (Graphics2D) g;
			g2.setPaint(colour);
		}
		
		private Color colour;
		private PalettePanel parentPanel;
	}
}
