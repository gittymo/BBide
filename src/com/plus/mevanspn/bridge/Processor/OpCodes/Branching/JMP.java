package com.plus.mevanspn.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;
import com.plus.mevanspn.bridge.MemoryMissingException;
import com.plus.mevanspn.bridge.Processor.OpCode;
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
	public char[] getASM() {
		return null;
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case Absolute: return 3;
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
		// Get the new program counter address
		int newPCAddress = memory.getValueAt(address, addressMode);
		// Move the program counter to new address.
		memory.registers.replace("PC", newPCAddress);
	}

	private int address;
	private AddressMode addressMode;
}
