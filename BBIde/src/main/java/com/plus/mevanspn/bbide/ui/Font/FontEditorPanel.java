package com.plus.mevanspn.bbide.ui.Font;

import com.plus.mevanspn.bbide.bridge.FontCharacter;
import com.plus.mevanspn.bbide.ui.Sprite.SpriteEditorPanel;
import com.plus.mevanspn.bbide.bridge.Font;

import java.awt.*;

final public class FontEditorPanel extends SpriteEditorPanel {
	public FontEditorPanel(Font font) {
		super();
		if (font != null & font != this.font) {
			this.font = font;
			this.setCharacter('A');
		}
	}

	public void setCharacter(char character) {
		if (font != null) {
			if (character > 31 && character < 256 && character != activeCharacter) {
				activeCharacter = character;
				FontCharacter fc = font.getCharacters().get(character);
				setSprite(fc.getCharacterImage());
				this.repaint();
			}
		}
	}
	
	public char getCharacter() {
		return activeCharacter;
	}
	
	private Font font;
	private int zoom = 8;
	private Color activeColour = Color.WHITE;
	private boolean drawing = false;
	private char activeCharacter = 0;
	
}
