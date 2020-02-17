package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Processor.OpCode;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;

/** The STY class is used to create objects representing the STY assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to store the Y register in memory.
 * No flags in the status register are affected by this operation. */
public class STY extends OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case ZeroPage: return new int[] {0x84, address & 0xFF};
			case ZeroPageX: return new int[] {0x94, address & 0xFF};
			case Absolute: return new int[] {0x8C, address & 0xFF, (address & 0xFF00) >> 8};
			default : return null;
		}
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case ZeroPage:
			case ZeroPageX: return 2;
			case Absolute: return 3;
		}

		return 0;
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case ZeroPage: return 3;
			case ZeroPageX:
			case Absolute:  return 4;
		}

		return 0;
	}

	public STY(int address, AddressMode addressMode) {
		this.address = address;
		this.addressMode = addressMode;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException,
			InvalidValueException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		if (addressMode == null || addressMode == AddressMode.Indirect ||
				addressMode == AddressMode.Immediate || addressMode == AddressMode.Accumulator ||
				addressMode == AddressMode.AbsoluteX || addressMode == AddressMode.AbsoluteY ||
				addressMode == AddressMode.PreIndirectX || addressMode == AddressMode.PostIndirectY ||
				addressMode == AddressMode.ZeroPageY)	throw new InvalidAddressModeException();
		int value = memory.registers.get("Y");
		memory.setValueAt(value, address, addressMode);
	}

	private final AddressMode addressMode;
	private final int address;
}
