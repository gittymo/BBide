package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCode;

/** THe CLC class is used to create objects representing the CLC assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to clear the carry flag which is often
 * set during arithmetic and bit shift operations. If is advisable to do with before
 * performing and ADC operation. No other flags in the status register are affected by this
 * operation. */
public class CLC extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] { 0x18 };
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
		memory.flags.replace('C', false);
	}
}
