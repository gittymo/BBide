package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Processor.OpCode;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.MemoryMissingException;

/** The TYA class is used to create objects representing the TYA assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to transfer the contents of the
 * Y register to the (A)ccumulator.  The (Z)ero flag is set if the accumulator is loaded
 * with zero (0) and the (N)egative flag is set if bit 7 of the accumulator is set.  No
 * other flags in the status register are affected by this operation. */
public class TYA extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0x98};
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
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		memory.registers.replace("A", memory.registers.get("Y"));
		memory.setNegativeZeroFlags(0,AddressMode.Accumulator);
	}
}
