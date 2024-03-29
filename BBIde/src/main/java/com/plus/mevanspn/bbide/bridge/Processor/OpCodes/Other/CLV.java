package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCode;

/** THe CLV class is used to create objects representing the CLV assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is clear the overflow flag, which is usually
 * set when bit 6 of a memory location is shifted into bit 7.  No other flags in the status
 * register are affected by this operation. */
public class CLV extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] { 0xB8 };
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
		// Clear the carry flag
		memory.flags.replace('V', false);
	}
}
