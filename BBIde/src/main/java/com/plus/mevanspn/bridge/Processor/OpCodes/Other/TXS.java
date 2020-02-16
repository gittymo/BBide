package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bridge.Storage.RAM.MemoryMissingException;

/** The TXS class is used to create objects representing the TXS assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to transfer the contents of the
 * X register to the Stack Pointer.
 * No flags in the status register are affected by this operation. */
public class TXS extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0x9A};
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
		memory.registers.replace("SP", memory.registers.get("X"));
	}
}
