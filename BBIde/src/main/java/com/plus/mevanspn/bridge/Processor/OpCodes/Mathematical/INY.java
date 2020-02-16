package com.plus.mevanspn.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bridge.Storage.RAM.*;
import com.plus.mevanspn.bridge.Processor.OpCode;

/** The INY class is used to create objects representing the INY assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to increment the value of the Y
 * register by 1.  WARNING! - Does not set the carry flag if value exceeds range of byte!
 * Depending on the result the following flags can be affected:
 * (Z)ero is set if memory contents becomes 0.
 * (N)egative is set if the 7th bit of the memory contents is set.
 * No other flags are affected. */
public class INY extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] { 0xC8 };
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
	public void perform(Memory memory) throws MemoryMissingException, InvalidAddressException {
		// Make sure we've got a viable memory object
		if (memory == null) throw new MemoryMissingException();

		// Get the incremented value of the Y register.
		int result = memory.registers.get("Y") + 1;

		// Set the Y register to the incremented value if appropriate.
		if (result < 127) memory.registers.replace("Y", result);

		// Set Zero and Negative flags according to the result.
		memory.setNegativeZeroFlags(0, AddressMode.YRegister);
	}
}
