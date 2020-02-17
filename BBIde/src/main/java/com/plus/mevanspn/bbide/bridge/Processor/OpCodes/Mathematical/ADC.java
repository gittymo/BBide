package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;

public class ADC extends com.plus.mevanspn.bbide.bridge.Processor.OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Immediate : return new int[] {0x69, address & 0xFF};
			case ZeroPage : return new int[] {0x65, address & 0xFF};
			case ZeroPageX : return new int[] {0x75, address & 0xFF};
			case Absolute : return new int[] {0x6D, (address & 0xFF), ((address >> 8) & 0xFF)};
			case AbsoluteX : return new int[] {0x7D, (address & 0xFF), ((address >> 8) & 0xFF)};
			case AbsoluteY : return new int[] {0x79, (address & 0xFF), ((address >> 8) & 0xFF)};
			case PreIndirectX : return new int[] {0x61, address & 0xFF};
			case PostIndirectY: return new int[] {0x71, address & 0xFF};
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
		// Add the memory and accumulator values together.
		int total = accumulator + mem;
		// Add 1 if carry flag set.
		if (memory.flags.get('C')) total += 1;

		// Check if total > 255
		if (total > 255) {
			// Total is greater value that a byte can hold, so set carry flag.
			memory.flags.replace('C', Boolean.TRUE);
			// Remove 256 from the total.
			total -= 256;
		} else if (total > 127 && accumulator < 128) {
			// Has a carry into bit 7, so set oVerflow flag.
			memory.flags.replace('V', Boolean.TRUE);
		}

		// Set the negative and zero flags appropriately.
		memory.setNegativeZeroFlags(address, addressMode);

		// Store the total in the accumulator.
		memory.registers.replace("A", total);
	}

	public ADC(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
	}

	private final AddressMode addressMode;
	private final int address;
}
