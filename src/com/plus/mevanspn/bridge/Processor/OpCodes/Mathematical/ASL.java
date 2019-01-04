package com.plus.mevanspn.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bridge.Storage.RAM.*;
import com.plus.mevanspn.bridge.Processor.OpCode;

public class ASL extends OpCode {

	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Accumulator : return new int[] { 0x0A };
			case ZeroPage : return new int[] { 0x06, address & 0xFF };
			case ZeroPageX : return new int[] { 0x16, address & 0xFF };
			case Absolute : return new int[] { 0x0E, address & 0xFF, (address >> 8) & 0xFF };
			case AbsoluteX : return new int[] { 0x1E, address & 0xFF, (address >> 8) & 0xFF };
			default : return null;
		}
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case Accumulator : return 1;
			case ZeroPage :
			case ZeroPageX : return 2;
			case Absolute :
			case AbsoluteX : return 3;
			default: return 0;
		}
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case Accumulator : return 2;
			case ZeroPage : return 5;
			case ZeroPageX :
			case Absolute : return 6;
			case AbsoluteX : return 7;
			default: return 0;
		}
	}

	public ASL(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException,
		MemoryMissingException
	{
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// Get the base value
		int baseValue = memory.getValueAt(address, addressMode);
		// Get the value shifted one place to the left (i.e. value x 2)
		char shiftedValue = (char) (baseValue << 1);
		// Determine if the 9th bit is set.
		boolean carry = (shiftedValue > 255) ? true : false;
		// Set the carry flag accoringly.
		memory.flags.replace('C', carry);
		// Set the negative and zero flags accordingly
		memory.setNegativeZeroFlags();
		// Store the shifted value less the 9th bit in the required memory location (or the Accumulator)
		memory.setValueAt(((char) (shiftedValue & 255)), address, addressMode);
	}

	private AddressMode addressMode;
	private int address;
}
