package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Processor.OpCode;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;

/** The PHA class is used to create objects representing the PHA assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to push the Accumulator on to the
 * stack.  No flags are affected by this operation. */
public class PHA extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0x48};
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
		int accValue = memory.registers.get("A");
		// Push the value of the accumulator onto the stack
		memory.stack.push(accValue);
	}

	private AddressMode addressMode;
	private int address;
}
