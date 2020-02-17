package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Processor.OpCode;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;

/** The PHP class is used to create objects representing the PHP assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to push the Status register on to the
 * stack.  No flags are affected by this operation. */
public class PHP extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0x08};
	}

	@Override
	public int getSize() {
		return 3;
	}

	@Override
	public int getBaseCycles() {
		return 1;
	}

	@Override
	public void perform(Memory memory) throws MemoryMissingException,
			StackOverflowException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();

		// Retrieve the value of the accumulator
		int accValue = memory.registers.get("P");
		// Push the value of the accumulator onto the stack
		memory.stack.push(accValue);
	}

	private AddressMode addressMode;
	private int address;
}
