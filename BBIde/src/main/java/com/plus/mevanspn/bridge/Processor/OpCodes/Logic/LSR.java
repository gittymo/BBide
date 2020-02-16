package com.plus.mevanspn.bridge.Processor.OpCodes.Logic;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.*;

/** The LSR class is used to create objects representing the LSR assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to shift the contents of a memory
 * location or a register one bit to the right.  Bit 7 of the location will become zero (0),
 * whilst the value at bit 0 will be used to set the carry flag.  (C)arry flag will be set
 * to the value of bit 0 of the original value.  (Z)ero flag will be set if the location
 *  value becomes zero (0), (N)egative flag will be cleared.  ALl other flags not changed. */
public class LSR extends OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Accumulator: return new int[] { 0x4A };
			case ZeroPage: return new int[] { 0x46, address & 0xFF };
			case ZeroPageX: return new int[] { 0x56, address & 0xFF };
			case Absolute: return new int[] { 0x4E, address & 0xFF, (address & 0xFF00) >> 8};
			case AbsoluteX: return new int[] { 0x5E, address & 0xFF, (address & 0xFF00) >> 8};
			default: return null;
		}
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case Accumulator: return 1;
			case ZeroPage:
			case ZeroPageX: return 2;
			case Absolute:
			case AbsoluteX: return 3;
		}

		return 0;
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case Accumulator: return 2;
			case ZeroPage: return 5;
			case ZeroPageX:
			case Absolute: return 6;
			case AbsoluteX: return 7;
		}

		return 0;
	}

	public LSR(int address, AddressMode addressMode) {
		this.address = address;
		this.addressMode = addressMode;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException,
			InvalidValueException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		if (addressMode == null) throw new InvalidAddressModeException();
		if (addressMode != AddressMode.Accumulator &&
				addressMode != AddressMode.ZeroPage &&
				addressMode != AddressMode.ZeroPageX &&
				addressMode != AddressMode.Absolute &&
				addressMode != AddressMode.AbsoluteX) throw new InvalidAddressException();

		int memoryValue = memory.getValueAt(this.address, this.addressMode);
		if ((memoryValue & 1) == 1) memory.flags.replace('C', true);
		else memory.flags.replace('C', false);
		memory.setValueAt(memoryValue >> 1, this.address, this.addressMode);
	}

	private final AddressMode addressMode;
	private final int address;
}
