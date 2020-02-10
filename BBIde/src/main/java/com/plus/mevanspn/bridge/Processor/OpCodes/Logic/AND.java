package com.plus.mevanspn.bridge.Processor.OpCodes.Logic;

import com.plus.mevanspn.bridge.Storage.RAM.*;

/** A logical AND is performed bit by bit on the Accumulator using a location in memory, leaving
 * the result in the Accumulator.
 * The following flags can be affected:
 * (Z)ero set if Accumulator = 0
 * (N)egative set if bit 7 of Accumulator is set.
 * No other flags are affected.
 */
public class AND extends com.plus.mevanspn.bridge.Processor.OpCode {

	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Immediate: return new int[] { 0x29, addressOrValue & 0xFF };
			case ZeroPage : return new int[] { 0x25, addressOrValue & 0xFF };
			case ZeroPageX : return new int[] { 0x35, addressOrValue & 0xFF };
			case Absolute : return new int[] { 0x2D, addressOrValue & 0xFF, (addressOrValue >> 8) & 0xFF};
			case AbsoluteX : return new int[] { 0x3D, addressOrValue & 0xFF, (addressOrValue >> 8) & 0xFF};
			case AbsoluteY : return new int[] { 0x39, addressOrValue & 0xFF, (addressOrValue >> 8) & 0xFF};
			case PreIndirectX : return new int[] { 0x21, addressOrValue & 0XFF};
			case PostIndirectY : return new int[] { 0x31, addressOrValue & 0xFF};
			default : return null;
		}
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case Immediate:
			case ZeroPage:
			case ZeroPageX:
			case PostIndirectY:
			case PreIndirectX: return 2;
			case Absolute:
			case AbsoluteX:
			case AbsoluteY: return 3;
			default: return 0;
		}
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case Immediate :
			case ZeroPage : return 2;
			case ZeroPageX : return 3;
			case Absolute :
			case AbsoluteX :
			case AbsoluteY : return 4;
			case PreIndirectX : return 6;
			case PostIndirectY : return 5;
			default : return 0;
		}
	}

	public AND(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.addressOrValue = address;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// We don't allow direct access to the accumulator with this opcode, so throw an exception.
		if (addressMode == AddressMode.Accumulator)
			throw new InvalidAddressModeException();
		// Get the accumulator value
		int accumulator = memory.registers.get("A");
		// Get the value stored in the given memory location
		int mem = memory.getValueAt(addressOrValue, addressMode);
		// AND the two together
		int result = (char) (mem & accumulator);
		// Store the result back into the accumulator.
		memory.registers.replace("A", result);
		// Update the negative and zero flags accordingly.
		memory.setNegativeZeroFlags( addressOrValue, addressMode);
	}

	private final AddressMode addressMode;
	private final int addressOrValue;
}
