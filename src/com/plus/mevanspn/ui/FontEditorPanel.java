package com.plus.mevanspn.ui;

import com.plus.mevanspn.bridge.FontCharacter;

import javax.swing.*;
import java.awt.*;

final public class FontEditorPanel extends JPanel {
	public FontEditorPanel() {

	}

	public void setFontCharacter(FontCharacter fontCharacter) {
		if (this.fontCharacter == null || fontCharacter != this.fontCharacter) {
			if (fontCharacter.getAsciiValue() < 32 || fontCharacter.getAsciiValue() > 255) return;
			else this.fontCharacter = fontCharacter;
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

	@Override
	public void repaint() {
		super.repaint();

		if (fontCharacter != null) {
			final int ZOOMED_WIDTH = fontCharacter.getCharacterImage().getWidth() * zoom;
			final int ZOOMED_HEIGHT = fontCharacter.getCharacterImage().getHeight() * zoom;
			final int LEFT = (this.getWidth() - ZOOMED_WIDTH) / 2;
			final int TOP = (this.getHeight() - ZOOMED_HEIGHT) / 2;
			Graphics2D g2 = (Graphics2D) this.getGraphics();
			g2.drawImage(fontCharacter.getCharacterImage(),
					LEFT, TOP,
					ZOOMED_WIDTH,
					ZOOMED_HEIGHT,
					null);
			// TODO: Draw yellow border and dotted grid lines.
		}
	}

	private FontCharacter fontCharacter = null;
	private int zoom = 8;
}
