package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Processor.OpCode;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.MemoryMissingException;

/** The SEC class is used to create objects representing the SEC assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to set the carry flag which is often
 * used during arithmetic and bit shift operations.
 * No other flags in the status register are affected by this operation. */
public class SEC extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0x38};
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
		// Set the carry flag
		memory.flags.replace('C', true);
	}
}
