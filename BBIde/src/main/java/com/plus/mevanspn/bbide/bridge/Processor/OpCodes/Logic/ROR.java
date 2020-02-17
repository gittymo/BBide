package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Logic;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;

/** The ROR mnemonic causes the value in memory (or the Accumulator) to shift right one bit.  The bit shifted out of
 * the byte (bit 0) is stored in the carry flag, whose original contents are stored in bit 7 of the memory value.
 * The following flags can be affected:
 * (C)arry set to bit value shifted out (old value of bit 0)
 * (Z)ero set if resultant value is 0
 * (N)egative set if bit 7 of value is set.
 * No other flags are affected.
 */
public class ROR extends com.plus.mevanspn.bbide.bridge.Processor.OpCode {

	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Accumulator:
				return new int[]{0x6A};
			case ZeroPage:
				return new int[]{0x66, address & 0xFF};
			case ZeroPageX:
				return new int[]{0x76, address & 0xFF};
			case Absolute:
				return new int[]{0x6E, address & 0xFF, (address & 0xFF00) >> 8};
			case AbsoluteX:
				return new int[]{0x7E, address & 0xFF, (address & 0xFF00) >> 8};
			default:
				return null;
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
			default: return 0;
		}
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case Accumulator: return 2;
			case ZeroPage: return 5;
			case ZeroPageX:
			case Absolute: return 6;
			case AbsoluteX: return 7;
			default: return 0;
		}
	}

	public ROR(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException,
			InvalidValueException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// We don't allow direct access to this mnemonic using these addressing modes, so throw an exception.
		if (addressMode == AddressMode.AbsoluteY || addressMode == AddressMode.Immediate ||
				addressMode == AddressMode.Indirect || addressMode == AddressMode.PostIndirectY ||
				addressMode == AddressMode.PreIndirectX || addressMode == AddressMode.ZeroPageY)
			throw new InvalidAddressModeException();
		// Get the required value
		int value =	memory.getValueAt(address, addressMode);
		// Add the current carry flag bit value as bit 8
		if (memory.flags.get('C') == true) value += 256;
		// Set the carry flag to match the value at bit 0
		memory.flags.replace('C', ((value & 1) == 1));
		// Shift the value one bit to the right
		value = value >> 1;
		// Store the value back into memory
		memory.setValueAt(value & 0xFF, address, addressMode);
		// Update the negative and zero flags accordingly.
		memory.setNegativeZeroFlags(address, addressMode);
	}

	private final AddressMode addressMode;
	private final int address;
}
