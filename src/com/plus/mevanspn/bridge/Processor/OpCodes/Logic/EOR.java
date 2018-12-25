package com.plus.mevanspn.bridge.Processor.OpCodes.Logic;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;
import com.plus.mevanspn.bridge.MemoryMissingException;

/** A logical EOR is performed bit by bit on the Accumulator using a location in memory, leaving
 * the result in the Accumulator.
 * The following flags can be affected:
 * (Z)ero set if Accumulator = 0
 * (N)egative set if bit 7 of Accumulator is set.
 * No other flags are affected.
 */
public class EOR extends com.plus.mevanspn.bridge.Processor.OpCode {

	@Override
	public char[] getASM() {
		return null;
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case Immediate:
			case ZeroPage:
			case ZeroPageX:
			case PreIndirectX:
			case PostIndirectY: return 2;
			case Absolute:
			case AbsoluteX:
			case AbsoluteY: return 3;
			default: return 0;
		}
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case Immediate: return 2;
			case ZeroPage: return 3;
			case ZeroPageX:
			case Absolute:
			case AbsoluteX:
			case AbsoluteY: return 4;
			case PreIndirectX: return 5;
			case PostIndirectY: return 6;
			default: return 0;
		}
	}	

	public EOR(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
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
		int mem = memory.getValueAt(address, addressMode);
		// EOR the two together
		int result = (char) (mem ^ accumulator);
		// Store the result back into the accumulator.
		memory.registers.replace("A", result);
		// Update the negative and zero flags accordingly.
		memory.setNegativeZeroFlags();
	}

	private AddressMode addressMode;
	private int address;
}
