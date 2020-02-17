package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Processor.OpCode;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.MemoryMissingException;

/** The SED class is used to create objects representing the SED assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to place the 6502 in decimal mode
 * (causing arithmetic operations to be performed in BCD mode)
 * No other flags in the status register are affected by this operation. */
public class SED extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0xF8};
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public int getBaseCycles() {
		return 2;
	}

	@Override
	public void perform(Memory memory) throws MemoryMissingException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// Set the decimal mode flag
		memory.flags.replace('D', true);
	}
}
