package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCode;

/** The DEX class is used to create objects representing the DEX assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to decrement the value of the X
 * register by 1.  Depending on the result the following flags can be affected:
 * (Z)ero is set if memory contents becomes 0.
 * (N)egative is set if the 7th bit of the memory contents is set.
 * No other flags are affected. */
public class DEX extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] { 0xCA };
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public int getBaseCycles() {
		return 2;
	}

	private void setFlagsBasedUponResult(int result, Memory memory) {
		if (result == 0) {
			memory.flags.replace('Z', true);
			memory.flags.replace('N', false);
		} else if (result >= 128) {
			memory.flags.replace('N', true);
			memory.flags.replace('Z', false);
		}
	}

	@Override
	public void perform(Memory memory) throws MemoryMissingException, InvalidAddressException {
		// Make sure we've got a viable memory object
		if (memory == null) throw new MemoryMissingException();

		// Get the decremented value of the X register.
		int result = memory.registers.get("X") - 1;

		// Set the X register to the decremented value if appropriate.
		if (result >= -128) memory.registers.replace("X", result);

		// Set Zero and Negative flags according to the result.
		memory.setNegativeZeroFlags(0, AddressMode.XRegister);
	}
}
