package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bridge.Storage.RAM.MemoryMissingException;

/** The TAY class is used to create objects representing the TAY assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to transfer the contents of the
 * (A)ccumulator to the Y register.  The (Z)ero flag is set if the Y register is loaded
 * with zero (0) and the (N)egative flag is set if bit 7 of the Y register is set.  No
 * other flags in the status register are affected by this operation. */
public class TAY extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0xA8};
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
		memory.registers.replace("Y", memory.registers.get("A"));
		memory.setNegativeZeroFlags(0,AddressMode.YRegister);
	}
}
