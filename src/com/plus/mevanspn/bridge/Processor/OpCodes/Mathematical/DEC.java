package com.plus.mevanspn.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bridge.Storage.RAM.*;
import com.plus.mevanspn.bridge.Processor.OpCode;

/** The DEC class is used to create objects representing the DEC assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to decrement the value at the given
 * memory address by 1.  Depending on the result the following flags can be affected:
 * (Z)ero is set if memory contents becomes 0.
 * (N)egative is set if the 7th bit of the memory contents is set.
 * No other flags are affected. */
public class DEC extends OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case ZeroPage: return new int[] { 0xC6, addressOrValue & 0xFF};
			case ZeroPageX: return new int[] { 0xD6, addressOrValue & 0xFF};
			case Absolute:
				return new int[] { 0xCE, addressOrValue & 0xFF, (addressOrValue & 0xFF00) >> 8};
			case AbsoluteX:
				return new int[] { 0xDE, addressOrValue & 0xFF, (addressOrValue & 0xFF00) >> 8};
			default: return null;
		}
	}

	@Override
	public int getSize() {
		if (addressMode == null) return 0;
		switch (addressMode) {
			case ZeroPage:
			case ZeroPageX: return 2;
			case Absolute:
			case AbsoluteX: return 3;
			default: return 0;
		}
	}

	@Override
	public int getBaseCycles() {
		if (addressMode == null) return 0;
		switch (addressMode) {
			case ZeroPage : return 5;
			case ZeroPageX :
			case Absolute : return 6;
			case AbsoluteX : return 7;
			default : return 0;
		}
	}

	public DEC(int addressOrValue, AddressMode addressMode) {
		this.addressOrValue = addressOrValue;
		this.addressMode = addressMode;
	}

	private void setFlagsBasedUponResult(int result, Memory memory) {
		if (result == 0) {
			memory.flags.replace('Z', true);
			memory.flags.replace('N', false);
		} else if (result >= 128) {
			memory.flags.replace('N', true);
			memory.flags.replace('Z', false);
		}
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException {
		// Make sure we've got a viable memory object
		if (memory == null) throw new MemoryMissingException();

		// Make sure we're using a valid addressing mode
		if (addressMode != AddressMode.ZeroPage &&
				addressMode != AddressMode.ZeroPageX &&
				addressMode != AddressMode.Absolute &&
				addressMode != AddressMode.AbsoluteX) throw new InvalidAddressModeException();

		// Get the decremented value of the memory location.
		int result;
		switch (addressMode) {
			case ZeroPage:
			case ZeroPageX:
			case Absolute:
			case AbsoluteX: result = 	memory.getValueAt(addressOrValue, addressMode) - 1; break;
			default: throw new InvalidAddressModeException();
		}

		// Set the value in memory.
		if (result >= -128) memory.setValueAt(result, addressOrValue, addressMode);

		// Set Carry, Zero and Negative flags according to the result.
		setFlagsBasedUponResult(result, memory);
	}

	private AddressMode addressMode;
	private int addressOrValue;
}
