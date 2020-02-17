package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCode;
/** The JMP class allows for the creation of JMP (JuMP to new location) mnemonic objects within a 
 * BBIDE pseudo program.
 * The JMP mnemonic is the equivalent to the GOTO statement of a BASIC program in that it allows 
 * us to move the current address of execution to an abitrary location in memory.
 * THe address can be given as am absolute address, or an indrect address.
 * Branch commands only affect the position of the Program Counter.  No other register or flag 
 * is affected by a branching command.
 */
public class JMP extends OpCode {

	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Absolute:
				return new int[] { 0x4C, address & 0xFF, (address & 0xFF00) >> 8};
			case Indirect:
				return new int[] { 0x6C, address & 0xFF, (address & 0xFF00) >> 8};
			default: return null;
		}
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case Absolute:
			case Indirect: return 3;
			default: return 0;
		}
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case Absolute: return 3;
			case Indirect: return 5;
			default: return 0;
		}
	}

	public JMP(int address, AddressMode addressMode) {
		this.address = address;
		this.addressMode = addressMode;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException,
		MemoryMissingException
	{
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// Make sure we're using the correct addressing modes
		if (addressMode != AddressMode.Absolute &&
				addressMode != AddressMode.Indirect) throw new InvalidAddressModeException();
		// Get the new program counter address
		int newPCAddress = memory.getValueAt(address, addressMode);
		// Move the program counter to new address.
		memory.registers.replace("PC", newPCAddress);
	}

	private final int address;
	private final AddressMode addressMode;
}
