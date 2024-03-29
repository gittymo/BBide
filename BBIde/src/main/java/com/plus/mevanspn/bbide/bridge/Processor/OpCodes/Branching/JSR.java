package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCode;
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
	public int[] getASM() {
		if (addressMode == AddressMode.Absolute)
			return new int[] { 0x20, address & 0xFF, (address & 0xFF00) >> 8};
		else return null;
	}

	@Override
	public int getSize() {
		if (addressMode == AddressMode.Absolute) {
			return 3;
		}
		return 0;
	}

	@Override
	public int getBaseCycles() {
		if (addressMode == AddressMode.Absolute) {
			return 6;
		}
		return 0;
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
		// Make sure we're using the correct addressing mode
		if (addressMode != AddressMode.Absolute) throw new InvalidAddressModeException();
		// Push the program counter onto the stack
		memory.stack.pushPC();
		// Get the new program counter address
		int newPCAddress = memory.getValueAt(address, addressMode);
		// Move the program counter to new address.
		memory.registers.replace("PC", newPCAddress);
	}

	private final int address;
	private final AddressMode addressMode;
}
