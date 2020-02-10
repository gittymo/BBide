package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bridge.Storage.RAM.MemoryMissingException;

/** The STX class is used to create objects representing the STX assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to store the X register in memory.
 * No flags in the status register are affected by this operation. */
public class STX extends OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case ZeroPage: return new int[] {0x86, address & 0xFF};
			case ZeroPageY: return new int[] {0x96, address & 0xFF};
			case Absolute: return new int[] {0x8E, address & 0xFF, (address & 0xFF00) >> 8};
			default : return null;
		}
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case ZeroPage:
			case ZeroPageY: return 2;
			case Absolute: return 3;
		}

		return 0;
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case ZeroPage: return 3;
			case ZeroPageY:
			case Absolute:  return 4;
		}

		return 0;
	}

	public STX(int address, AddressMode addressMode) {
		this.address = address;
		this.addressMode = addressMode;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		if (addressMode == null || addressMode == AddressMode.Indirect ||
			addressMode == AddressMode.Immediate || addressMode == AddressMode.Accumulator ||
			addressMode == AddressMode.AbsoluteX || addressMode == AddressMode.AbsoluteY ||
			addressMode == AddressMode.PreIndirectX || addressMode == AddressMode.PostIndirectY ||
			addressMode == AddressMode.ZeroPageX)	throw new InvalidAddressModeException();
		int value = memory.registers.get("X");
		memory.setValueAt(value, address, addressMode);
	}

	private final AddressMode addressMode;
	private final int address;
}
