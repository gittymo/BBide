package com.plus.mevanspn.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bridge.Storage.RAM.MemoryMissingException;

public class SBC extends com.plus.mevanspn.bridge.Processor.OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Immediate : return new int[] {0xE9, address & 0xFF};
			case ZeroPage : return new int[] {0xE5, address & 0xFF};
			case ZeroPageX : return new int[] {0xF5, address & 0xFF};
			case Absolute : return new int[] {0xED, (address & 0xFF), ((address >> 8) & 0xFF)};
			case AbsoluteX : return new int[] {0xFD, (address & 0xFF), ((address >> 8) & 0xFF)};
			case AbsoluteY : return new int[] {0xF9, (address & 0xFF), ((address >> 8) & 0xFF)};
			case PreIndirectX : return new int[] {0xE1, address & 0xFF};
			case PostIndirectY: return new int[] {0xF1, address & 0xFF};
			default : return null;
		}
	}

	@Override
	public int getSize() {
		return addressMode.getSize();
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case Immediate : return 2;
			case ZeroPage : return 3;
			case ZeroPageX:
			case Absolute:
			case AbsoluteX:
			case AbsoluteY: return 4;
			case PreIndirectX: return 6;
			case PostIndirectY: return 5;
			default: return 0;
		}
	}

	@Override
	public void perform(Memory memory)
					throws InvalidAddressException, InvalidAddressModeException, MemoryMissingException
	{
		// We don't allow direct access to the accumulator with this opcode, so throw an exception.
		if (addressMode == AddressMode.Accumulator)
			throw new InvalidAddressModeException();
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// Get the value from the accumulator.
		int accumulator = memory.registers.get("A");
		// Get the value from the memory location given.
		int mem = memory.getValueAt(address, addressMode);
		// Subtract the memory value from the accumulator value.
		int total = accumulator - mem;
		// Subtract the borrow if the carry flag is clear
		if (!memory.flags.get('C')) total -= 1;
		// Set inverse of Carry flag depending upon borrow (i.e. if result < 0)
		memory.flags.replace('C', !(total < 0));
		// Set oVerflow flag if carry is into the 7th bit
		memory.flags.replace('V', (total < -128));
		// Set the Zero flag if the result is zero
		memory.flags.replace('Z', (total == 0));
		// If the total if less than -127, add 256 to it.
		if (total < -127) total += 256;
		// Store the result back in the accumulator
		memory.setValueAt(total, 0, AddressMode.Accumulator);
	}

	public SBC(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
	}

	private final AddressMode addressMode;
	private final int address;
}
