package com.plus.mevanspn.bridge;

import com.plus.mevanspn.bridge.Storage.RAM.*;
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