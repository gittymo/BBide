package com.plus.mevanspn.bbide.bridge;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;
public class Model {
	Model(String configFile) {
		
	}

	public int getRAMSize() {
		return memory.getSize();
	}

	private String name;
	private Memory memory;
	private MemoryMap memoryMap;
}