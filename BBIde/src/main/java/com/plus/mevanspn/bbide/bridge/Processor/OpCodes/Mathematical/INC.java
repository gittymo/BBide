package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCode;

/** The INC class is used to create objects representing the INC assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to increment the value at the given
 * memory address by 1.  BE WARY! - This mnemonic does not set the Carry flag if the value 
 * exceeds the range a byte provides!
 * Depending on the result the following flags can be affected:
 * (Z)ero is set if memory contents becomes 0.
 * (N)egative is set if the 7th bit of the memory contents is set.
 * No other flags are affected. */

public class INC extends OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case ZeroPage: return new int[] { 0xE6, addressOrValue & 0xFF };
			case ZeroPageX: return new int[] { 0xF6, addressOrValue & 0xFF };
			case Absolute: return new int[] { 0xEE, addressOrValue & 0xFF, (addressOrValue & 0xFF00) >> 8};
			case AbsoluteX: return new int[] { 0xFE, addressOrValue & 0xFF, (addressOrValue & 0xFF00) >> 8};
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

	public INC(int addressOrValue, AddressMode addressMode) {
		this.addressOrValue = addressOrValue;
		this.addressMode = addressMode;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException,
			InvalidValueException {
		// Make sure we've got a viable memory object
		if (memory == null) throw new MemoryMissingException();

		// Get the decremented value of the memory location.
		int result;
		switch (addressMode) {
			case ZeroPage:
			case ZeroPageX:
			case Absolute:
			case AbsoluteX: result = 	memory.getValueAt(addressOrValue, addressMode) + 1; break;
			default: throw new InvalidAddressModeException();
		}

		// Set the value in memory.
		if (result <= 127) memory.setValueAt(result, addressOrValue, addressMode);

		// Set Carry, Zero and Negative flags according to the result.
		memory.setNegativeZeroFlags(addressOrValue, addressMode);
	}

	private final AddressMode addressMode;
	private final int addressOrValue;
}
