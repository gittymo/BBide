package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bridge.Storage.RAM.MemoryMissingException;

/** The LDY class is used to create objects representing the LDY assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to load the Y register with a value
 * from memory.  The (Z)ero flag is set if the Y register is loaded with zero (0) and the
 * (N)egative flag is set if bit 7 of the Y register is set.  No other flags in the status
 * register are affected by this operation. */
public class LDY extends OpCode {
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

	public LDY(int address, AddressMode addressMode) {
		this.address = address;
		this.addressMode = addressMode;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		if (addressMode == null) throw new InvalidAddressModeException();
		if (addressMode != AddressMode.Immediate &&
				addressMode != AddressMode.ZeroPage &&
				addressMode != AddressMode.ZeroPageX &&
				addressMode != AddressMode.Absolute &&
				addressMode != AddressMode.AbsoluteX) throw new InvalidAddressException();
		// Clear the carry flag
		memory.registers.replace("Y", memory.getValueAt(this.address, this.addressMode));
	}

	private final AddressMode addressMode;
	private final int address;
}
