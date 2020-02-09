package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.*;

/** The PLA class is used to create objects representing the PLA assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to pull the next value from the
 * stack and place it in the (A)ccumulator.
 * The (Z)ero flag is affected if the (A)ccumulator value is zero (0)
 * The (N)egative flag is affected if bit 7 of the (A)ccumulator is set.
 * No other flags are affected by this operation. */
public class PLA extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0x68};
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
	public void perform(Memory memory) throws MemoryMissingException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();

		// Retrieve the value from the stack
		int stackValue = memory.stack.pull();
		// Push the value onto the accumulator.
		memory.registers.replace("A", stackValue);
		// Set the appropriate zero and negative flags.
		memory.setNegativeZeroFlags();
	}

	private AddressMode addressMode;
	private int address;
}
