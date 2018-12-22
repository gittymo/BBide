package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;
import com.plus.mevanspn.bridge.MemoryMissingException;
import com.plus.mevanspn.bridge.Processor.OpCode;

/** THe BIT class is used to create objects representing the BIT assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to do a bitwise AND comparison of the
 * contents of the accumulator against a the contents of a memory location.  If the bits
 * match then the zero flag is unset (0) otherwise if the bits are different the zero flag
 * is set (1)  THe (N)egative flag's contents are set to the 7th bit value of the memory area
 * and the (M)minus flag contains the 6th bit value of the memory area.
 */
public class BIT extends OpCode {
	@Override
	public char[] getASM() {
		return new char[0];
	}

	@Override
	public int getSize() {
		return (addressMode == AddressMode.Absolute) ? 3 : 2;
	}

	@Override
	public int getBaseCycles() {
		return 0;
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
		//
		int result = memoryValue & memory.registers.get('A');
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
