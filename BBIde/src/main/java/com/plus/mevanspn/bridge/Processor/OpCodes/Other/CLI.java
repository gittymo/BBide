package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Storage.RAM.*;
import com.plus.mevanspn.bridge.Processor.OpCode;

/** THe CLI class is used to create objects representing the CLI assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is clear the interrupt flag, which re-enables
 * interrupts after the flag has been set.  It is unwise to use this operation without good
 * reason as it can adversely affect the interrupt system.  No other flags in the status
 * register are affected by this operation. */
public class CLI extends OpCode {
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
		memory.flags.replace('I', false);
	}
}
