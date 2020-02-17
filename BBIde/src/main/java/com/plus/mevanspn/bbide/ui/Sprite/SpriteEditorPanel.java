/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.bbide.ui.Sprite;

import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.awt.event.*;

/**
 *
 * @author morgan
 */
public class SpriteEditorPanel extends JPanel 
				implements MouseListener, MouseMotionListener {
	public SpriteEditorPanel() {
		super();
	}
	
	public SpriteEditorPanel(BufferedImage sprite) {
		super();
	}
	
	@Override
	public void repaint() {
		super.repaint();

		// Check to see if there is a valid sprite set.
		if (sprite != null) {
			// Is it, so paint it centered in the panel.
			final int ZOOMED_WIDTH = sprite.getWidth() * zoom;
			final int ZOOMED_HEIGHT = sprite.getHeight() * zoom;
			final int LEFT = (this.getWidth() - ZOOMED_WIDTH) / 2;
			final int TOP = (this.getHeight() - ZOOMED_HEIGHT) / 2;
			Graphics2D g2 = (Graphics2D) this.getGraphics();
			g2.drawImage(sprite,
					LEFT, TOP,
					ZOOMED_WIDTH,
					ZOOMED_HEIGHT,
					null);
			// Draw a solid yellow border around the image.
			g2.setPaint(Color.YELLOW);
			g2.drawRect(LEFT, TOP, ZOOMED_WIDTH, ZOOMED_HEIGHT);
			// Draw a dotted grid around the pixels if zoom > 2
			if (zoom > 2) {
				g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT,
								BasicStroke.JOIN_MITER,
								1.0f, new float[] {1.0f, 1.0f}, 0));
				for (int y = TOP + zoom; y < (TOP + ZOOMED_HEIGHT) - zoom; y += zoom) {
					g2.drawLine(LEFT, y, LEFT + ZOOMED_WIDTH, y);
				}
				for (int x = LEFT + zoom; x < (LEFT + ZOOMED_WIDTH) - zoom; x += zoom) {
					g2.drawLine(x, TOP, x, TOP + ZOOMED_HEIGHT);
				}
			}
		}
	}
	
	public void setPixel(int mouseX, int mouseY) {
		if (activeColour != null) {
			// Get the current position and display dimensions of the preview.
			final int ZOOMED_WIDTH = sprite.getWidth() * zoom;
			final int ZOOMED_HEIGHT = sprite.getHeight() * zoom;
			final int LEFT = (this.getWidth() - ZOOMED_WIDTH) / 2;
			final int TOP = (this.getHeight() - ZOOMED_HEIGHT) / 2;
			// Sanity check:  Do nothing if mouse click is outside image area.
			if (mouseX < LEFT || mouseX > LEFT + ZOOMED_WIDTH) return;
			if (mouseY < TOP || mouseY > TOP + ZOOMED_HEIGHT) return;
			// Get the column and row of the pixel being set.
			final int SET_COLUMN = (mouseX - LEFT) / zoom;
			final int SET_ROW = (mouseY - TOP) / zoom;
			/*	Check the RGB value of the target pixel with the active colour, if same
					do nothing. */
			if (sprite.getRGB(SET_COLUMN, SET_ROW) ==
							activeColour.getRGB()) return;
			// If the colour isn't the same, change the pixel colour.
			sprite.setRGB(SET_COLUMN, SET_ROW, 
							activeColour.getRGB());
			// Repaint the image to show the colour change.
			this.repaint();
		}
	}
	
	public void setZoom(int zoom) {
		if (zoom != this.zoom) {
			if (zoom > 0 && zoom < 32) {
				this.zoom = zoom;
				this.repaint();
			}
		}
	}

	public int getZoom() {
		return zoom;
	}

	public void setColour(Color newActiveColour) {
		if (newActiveColour != null && newActiveColour.getRGB() != activeColour.getRGB()) {
			activeColour = newActiveColour;
		}
	}
	
	public Color getColour() {
		return activeColour;
	}
	
	public void setSprite(BufferedImage sprite) {
		if (sprite != null && sprite != this.sprite) {
			this.sprite = sprite;
			repaint();
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent me) {
		setPixel(me.getX(), me.getY());
	}

	@Override
	public void mousePressed(MouseEvent me) {
		if (!drawing) {
			drawing = true;
			setPixel(me.getX(), me.getY());
		}
	}

	@Override
	public void mouseReleased(MouseEvent me) {
		if (drawing) {
			drawing = false;
			setPixel(me.getX(), me.getY());
		}
	}

	@Override
	public void mouseEntered(MouseEvent me) {
		// Not used
	}

	@Override
	public void mouseExited(MouseEvent me) {
		// Net used
	}
		
	@Override
	public void mouseDragged(MouseEvent me) {
		
	}

	@Override
	public void mouseMoved(MouseEvent me) {
		if (drawing) setPixel(me.getX(), me.getY());
	}
	
	private int zoom = 1;
	private BufferedImage sprite = null;
	private int transparentIndex = -1;
	private boolean drawing;
	private Color activeColour = null;
}
