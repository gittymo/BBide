package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Processor.OpCode;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.MemoryMissingException;

/** The SEI class is used to create objects representing the SEI assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to set the interrupt disable flag.
 * When set, maskable interrupts cannot occur.
 * No other flags in the status register are affected by this operation. */
public class SEI extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0x78};
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
		// Set the interrupt disable flag
		memory.flags.replace('I', true);
	}
}
