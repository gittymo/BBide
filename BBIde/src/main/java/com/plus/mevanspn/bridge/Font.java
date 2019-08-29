package com.plus.mevanspn.bridge;

import java.util.*;
import java.io.*;

final public class Font {
	public Font(String fontName) {
		// Set the default font name.
		setName(fontName);

		/*  Create a hash map containing a FontCharacter object representing each of the
				visible characters (32-255). */
		characters = new HashMap<>();
		for (int i = 32; i < 256; i++)
			characters.put((char) i, new FontCharacter((char) i));
	}

	public void setName(String fontName) {
		if (fontName == null || fontName.trim().isEmpty())
			setName("Font" + Font.fontCount++);
		else
			this.fontName = fontName;
	}

	public String getName() {
		return fontName;
	}

	public HashMap<Character, FontCharacter> getCharacters() {
		return characters;
	}
	
	public void writeToStream(DataOutputStream dos) throws IOException {
		if (dos != null) {
			dos.writeChars("BFF");
			dos.writeInt(fontName.length());
			dos.writeChars(fontName);
			for (Map.Entry<Character, FontCharacter> me : characters.entrySet()) {
				me.getValue().writeToStream(dos);
			}
		}
	}

	private HashMap<Character, FontCharacter> characters;
	private String fontName;
	private static int fontCount = 0;
}
