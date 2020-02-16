package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.*;

/** The STA class is used to create objects representing the STA assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to store the (A)ccumulator in memory.
 * No flags in the status register are affected by this operation. */
public class STA extends OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case ZeroPage: return new int[] {0x85, address & 0xFF};
			case ZeroPageX: return new int[] {0x95, address & 0xFF};
			case Absolute: return new int[] {0x8D, address & 0xFF, (address & 0xFF00) >> 8};
			case AbsoluteX: return new int[] {0x9D, address & 0xFF, (address & 0xFF00) >> 8};
			case AbsoluteY: return new int[] {0x99, address & 0xFF, (address & 0xFF00) >> 8};
			case PreIndirectX: return new int[] {0x81, address & 0XFF};
			case PostIndirectY: return new int[] {0x91, address & 0xFF};
			default : return null;
		}
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case ZeroPage:
			case ZeroPageX:
			case PreIndirectX:
			case PostIndirectY: return 2;
			case Absolute:
			case AbsoluteX:
			case AbsoluteY: return 3;
		}

		return 0;
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case ZeroPage: return 3;
			case ZeroPageX:
			case Absolute:  return 4;
			case AbsoluteX:
			case AbsoluteY: return 5;
			case PostIndirectY:
			case PreIndirectX: return 6;
		}

		return 0;
	}

	public STA(int address, AddressMode addressMode) {
		this.address = address;
		this.addressMode = addressMode;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException,
			InvalidValueException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		if (addressMode == null || addressMode == AddressMode.Indirect ||
			addressMode == AddressMode.Immediate || addressMode == AddressMode.Accumulator) throw new InvalidAddressModeException();
		int value = memory.registers.get("A");
		memory.setValueAt(value, address, addressMode);
	}

	private final AddressMode addressMode;
	private final int address;
}
