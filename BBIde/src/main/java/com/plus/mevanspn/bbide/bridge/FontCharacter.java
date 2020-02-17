package com.plus.mevanspn.bbide.bridge;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.IndexColorModel;
import java.io.*;

final public class FontCharacter {
	public FontCharacter(char asciiValue) {
		/*  Make sure the given character value is sensible, it can't be < 32 as
				that would fall within the control codes and it can't be over 255 as
				that's out of range, if either case use 255 as default. */
		if (asciiValue < 32 || asciiValue > 255) asciiValue = 255;
		this.asciiValue = asciiValue;

		//  Create a indexed colour model holding blank and white entries.
		this.characterImagePalette = new IndexColorModel(1, 2,
				new byte[] {(byte) 0, (byte) 255},
				new byte[] {(byte) 0, (byte) 255},
				new byte[] {(byte) 0, (byte) 255});

		//  Create an 8x8 image using the black and white colour model.
		this.characterImage = new BufferedImage(8, 8,
				BufferedImage.TYPE_BYTE_INDEXED,
				this.characterImagePalette);

		//  Set all pixel values to black (transparent) by default.
		Graphics g = this.characterImage.getGraphics();
		g.setColor(Color.BLACK);

		g.fillRect(0, 0, 8,8);

	}

	public char getAsciiValue() {
		return asciiValue;
	}

	public BufferedImage getCharacterImage() {
		return characterImage;
	}

	public void setPixel(Color colour, int x, int y) {
		// Sanity check, do nothing if the colour isn't black or white.
		if (colour != Color.WHITE || colour != Color.BLACK) return;
		// Sanity check, do nothing if the horizontal location is invalid.
		if (x < 0 || x > 7) return;
		// Sanity check, do nothing if the vertical location is invalid.
		if (y < 0 || y > 7) return;

		// If we've got this far everything's fine so set the pixel to the given colour.
		characterImage.setRGB(x, y, colour.getRGB());
	}

	public int[] getVDUCodes() {
		// Create an array to hold the codes.
		int[] vduCodes = new int[8];
		// Make sure all array entries are initially set to 0.
		for (int i = 0 ; i < 8; i++) vduCodes[i] = 0;

		/*  Iterate through the pixel data from top to bottom.  For each row, store the
				bit-wise value for any pixels whose colour value corresponds to white. */
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				if (characterImage.getRGB(x,y) == Color.WHITE.getRGB()) vduCodes[y] += 1 << x;
			}
		}

		// Return the ascii character's value codes to the calling method.
		return vduCodes;
	}

	public void writeToStream(DataOutputStream dos) throws IOException {
		if (dos != null) {
			int[] vduCodes = this.getVDUCodes();
			if (vduCodes != null) {
				dos.writeByte(this.asciiValue);
				for (int vduCode : vduCodes) {
					dos.writeByte(vduCode);
				}
			}
		}
	}

	private final char asciiValue;
	private final BufferedImage characterImage;
	private final IndexColorModel characterImagePalette;
}
