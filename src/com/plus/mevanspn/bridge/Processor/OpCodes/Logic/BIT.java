package com.plus.mevanspn.bridge.Processor.OpCodes.Logic;

import com.plus.mevanspn.bridge.Storage.RAM.*;
import com.plus.mevanspn.bridge.Processor.OpCode;

/** THe BIT class is used to create objects representing the BIT assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to do a bitwise AND comparison of the
 * contents of the accumulator against a the contents of a memory location.  If the bits
 * match then the zero flag is unset (0) otherwise if the bits are different the zero flag
 * is set (1)  THe (N)egative flag's contents are set to the 7th bit value of the memory area
 * and the (M)inus flag contains the 6th bit value of the memory area.
 * The results of the AND operation are not stored in the Accumulator.
 */
public class BIT extends OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case ZeroPage : return new int[] { 0x24, address & 0xFF};
			case Absolute : return new int[] { 0x2C, address & 0xFF, (address >> 8) & 0xFF };
			default : return null;
		}
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case ZeroPage : return 2;
			case Absolute : return 3;
			default: return 0;
		}
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case ZeroPage : return 3;
			case Absolute : return 4;
			default: return 0;
		}
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException,
		MemoryMissingException
	{
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// Only allow zero page and absolute addressing, throw an exception for the rest.
		if (addressMode != AddressMode.ZeroPage ||
						addressMode != AddressMode.Absolute) throw new InvalidAddressModeException();
		// Make sure zero page addressing is within range.
		if (addressMode == AddressMode.ZeroPage) address = address & 255;
		// Get value at address
		int memoryValue = memory.getValueAt(address, addressMode);
		// AND with Accumulator
		int result = memoryValue & memory.registers.get("A");
		memory.flags.replace('Z', (result == memoryValue));
		// Set overflow flag to value of bit 6 of memory location
		memory.flags.replace('V', (memoryValue & 64) == 64);
		// Set negative flag to value of bit 7 of memory location
		memory.flags.replace('N', (memoryValue & 128) == 128);
	}

	public BIT(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
	}

	private AddressMode addressMode;
	private int address;
}
