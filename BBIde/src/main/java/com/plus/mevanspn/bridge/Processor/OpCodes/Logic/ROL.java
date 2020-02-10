package com.plus.mevanspn.bridge.Processor.OpCodes.Logic;

import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bridge.Storage.RAM.MemoryMissingException;

/** The ROL mnemonic causes the value in memory (or the Accumulator) to shift left one bit.  The bit shifted out of
 * the byte (bit 7) is stored in the carry flag, whose original contents are stored in bit 0 of the memory value.
 * The following flags can be affected:
 * (C)arry set to bit value shifted out (old value of bit 7)
 * (Z)ero set if resultant value is 0
 * (N)egative set if bit 7 of value is set.
 * No other flags are affected.
 */
public class ROL extends com.plus.mevanspn.bridge.Processor.OpCode {

	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Accumulator:
				return new int[]{0x2A};
			case ZeroPage:
				return new int[]{0x26, address & 0xFF};
			case ZeroPageX:
				return new int[]{0x36, address & 0xFF};
			case Absolute:
				return new int[]{0x2E, address & 0xFF, (address & 0xFF00) >> 8};
			case AbsoluteX:
				return new int[]{0x3E, address & 0xFF, (address & 0xFF00) >> 8};
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

	public ROL(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// We don't allow direct access to this mnemonic using these addressing modes, so throw an exception.
		if (addressMode == AddressMode.AbsoluteY || addressMode == AddressMode.Immediate ||
				addressMode == AddressMode.Indirect || addressMode == AddressMode.PostIndirectY ||
				addressMode == AddressMode.PreIndirectX || addressMode == AddressMode.ZeroPageY)
			throw new InvalidAddressModeException();
		// Get the required value
		int value =
				(addressMode == AddressMode.Accumulator) ? memory.registers.get("A") : memory.getValueAt(address, addressMode);
		// Shift the value one bit to the left
		value = value << 1;
		// Add the current carry flag value if set
		if (memory.flags.get('C')) value = value + 1;
		// Store the value of the 9th bit (the one shifted out) in the carry flag.
		memory.flags.replace('C', ((value & 256) >> 8 == 1));
		// Store the value back into memory
		memory.setValueAt(value & 0xFF, address, addressMode);
		// Update the negative and zero flags accordingly.
		memory.setNegativeZeroFlags(address, addressMode);
	}

	private final AddressMode addressMode;
	private final int address;
}
