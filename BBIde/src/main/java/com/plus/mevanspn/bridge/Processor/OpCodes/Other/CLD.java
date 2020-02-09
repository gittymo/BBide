package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Storage.RAM.*;
import com.plus.mevanspn.bridge.Processor.OpCode;

/** THe CLD class is used to create objects representing the CLD assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to clear the decimal mode flag and places
 * processor in non-decimal mode. No other flags in the status register are affected by this
 * operation. */
public class CLD extends OpCode {
	@Override
	public int[] getASM() {
		return new int[0];
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public int getBaseCycles() {
		return 0;
	}

	@Override
	public void perform(Memory memory) throws MemoryMissingException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// Clear the carry flag
		memory.flags.replace('D', false);
	}
}
