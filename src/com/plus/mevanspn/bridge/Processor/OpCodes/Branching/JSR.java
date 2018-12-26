package com.plus.mevanspn.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bridge.Storage.RAM.*;
import com.plus.mevanspn.bridge.Processor.OpCode;
/** The JSR class allows for the creation of JSR (Jump to SubRoutine) mnemonic objects within a 
 * BBIDE pseudo program.
 * The JSR mnemonic is the equivalent to the FN statement of a BASIC program in that it allows 
 * us to move the current address of execution to an abitrary location in memory and continue 
 * execution from there.  The program counter prior to the jump is stored on the stack so that we 
 * can return to the original point of execution after that subroutine has finished.
 * Branch commands only affect the position of the Program Counter.  No other register or flag 
 * is affected by a branching command.
 */
public class JSR extends OpCode {

	@Override
	public char[] getASM() {
		return null;
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case Absolute: return 3;
			default: return 0;
		}
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case Absolute: return 6;
			default: return 0;
		}
	}

	public JSR(int address, AddressMode addressMode) {
		this.address = address;
		this.addressMode = addressMode;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException,
		MemoryMissingException, StackOverflowException
	{
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// Get and push the program counter onto the stack
		int pc = memory.registers.get("PC");
		memory.stack.push(pc);
		// Get the new program counter address
		int newPCAddress = memory.getValueAt(address, addressMode);
		// Move the program counter to new address.
		memory.registers.replace("PC", newPCAddress);
	}

	private int address;
	private AddressMode addressMode;
}
