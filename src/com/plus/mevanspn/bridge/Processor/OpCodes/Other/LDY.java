package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bridge.Storage.RAM.MemoryMissingException;

/** The LDX class is used to create objects representing the LDX assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to load the X register with a value
 * from memory.  The (Z)ero flag is set if the X register is loaded with zero (0) and the
 * (N)egative flag is set if bit 7 of the X register is set.  No other flags in the status
 * register are affected by this operation. */
public class LDX extends OpCode {
	@Override
	public int[] getASM() {
		return new int[0];
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case Immediate:
			case ZeroPage:
			case ZeroPageY: return 2;
			case Absolute:
			case AbsoluteY: return 3;
		}

		return 0;
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case Immediate: return 2;
			case ZeroPage: return 3;
			case ZeroPageY:
			case Absolute:
			case AbsoluteY: return 4;
		}

		return 0;
	}

	public LDX(int address, AddressMode addressMode) {
		this.address = address;
		this.addressMode = addressMode;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		if (addressMode == null) throw new InvalidAddressModeException();
		// Clear the carry flag
		memory.registers.replace("X", memory.getValueAt(this.address, this.addressMode));
	}

	private AddressMode addressMode;
	private int address;
}
