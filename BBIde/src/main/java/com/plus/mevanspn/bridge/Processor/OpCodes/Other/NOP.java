package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bridge.Storage.RAM.MemoryMissingException;

/** The NOP class is used to create objects representing the NOP assembler mnemonic in
 * BBide programs.  This mnemonic is a dummy instruction and does nothing to affect memory,
 * flags or registers.
 */
public class NOP extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] {0xEA};
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
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException,
		MemoryMissingException
	{
		memory.registers.replace("PC", memory.registers.get("PC") + 1);
	}

	public NOP(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
	}

	private AddressMode addressMode;
	private int address;
}
