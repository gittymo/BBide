package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Logic;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.MemoryMissingException;

/** The ORA mnemonic performs a logical OR operation between a memory location and the contents of the (A)ccumulator,
 * leaving the result in the Accumulator.
 * The following flags can be affected:
 * (Z)ero set if Accumulator = 0
 * (N)egative set if bit 7 of Accumulator is set.
 * No other flags are affected.
 */
public class ORA extends com.plus.mevanspn.bbide.bridge.Processor.OpCode {

	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Immediate:
				return new int[]{0x09, address & 0xFF};
			case ZeroPage:
				return new int[]{0x05, address & 0xFF};
			case ZeroPageX:
				return new int[]{0x15, address & 0xFF};
			case Absolute:
				return new int[]{0x0D, address & 0xFF, (address & 0xFF00) >> 8};
			case AbsoluteX:
				return new int[]{0x1D, address & 0xFF, (address & 0xFF00) >> 8};
			case AbsoluteY:
				return new int[]{0x19, address & 0xFF, (address & 0xFF00) >> 8};
			case PreIndirectX:
				return new int[]{0x01, address & 0xFF};
			case PostIndirectY:
				return new int[]{0x11, address & 0xFF};
			default:
				return null;
		}
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
			case Immediate:
			case ZeroPage: return 2;
			case ZeroPageX: return 3;
			case Absolute:
			case AbsoluteX:
			case AbsoluteY: return 4;
			case PreIndirectX:
			case PostIndirectY: return 6;
			default: return 0;
		}
	}

	public ORA(AddressMode addressMode, int address) {
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
		int result = (char) (mem | accumulator);
		// Store the result back into the accumulator.
		memory.registers.replace("A", result);
		// Update the negative and zero flags accordingly.
		memory.setNegativeZeroFlags(address, addressMode);
	}

	private final AddressMode addressMode;
	private final int address;
}
