package com.plus.mevanspn.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bridge.Memory;

public class ADC implements com.plus.mevanspn.bridge.Processor.OpCode {
	@Override
	public int getASM() {
		return 0;
	}

	@Override
	public int getSize() {
		return addressMode.getSize();
	}

	@Override
	public void perform(Memory memory) {

	}

	public ADC(char value, AddressMode addressMode) {
		this.value = Memory.getValidByteValue(value);
		this.addressMode = addressMode;
	}

	private AddressMode addressMode;
	private char value;
}
