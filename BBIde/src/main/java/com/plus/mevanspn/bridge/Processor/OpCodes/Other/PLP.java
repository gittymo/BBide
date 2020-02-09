package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.*;

/** The PLP class is used to create objects representing the PLP assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to pull the next value from the
 * stack set the status register (all flags) according to the following bit values:
 * (C)arry Flag - bit 0 of stack value
 * (Z)ero Flag - bit 1 of stack value
 * (I)nterrupt Disable - bit 2 of stack value
 * (D)ecimal Mode Flag - bit 3 of stack value
 * (B)reak Command - bit 4 of stack value
 * O(V)erflow Flag - bit 6 of stack value
 * (N)egative FLag - bit 7 of stack value
 * The (N)egative flag is affected if bit 7 of the (A)ccumulator is set.
 * No other flags are affected by this operation. */
public class PLP extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0x28};
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
		// Set the status flags
		memory.flags.replace('C', ((stackValue & 1) == 1));
		memory.flags.replace('Z', ((stackValue & 2) >> 1 == 1));
		memory.flags.replace('I', ((stackValue & 4) >> 2 == 1));
		memory.flags.replace('D', ((stackValue & 8) >> 3 == 1));
		memory.flags.replace('B', ((stackValue & 16) >> 4 == 1));
		memory.flags.replace('V', ((stackValue & 64) >> 6 == 1));
		memory.flags.replace('N', ((stackValue & 128) >> 7 == 1));
	}

	private AddressMode addressMode;
	private int address;
}
