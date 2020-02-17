package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bbide.bridge.Processor.OpCode;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;

/** The RTS class allows for the creation of RTS (ReRurn from Subroutine) mnemonic objects within a
 * BBIDE pseudo program.
 * The RTS mnemonic is used to terminate a subroutine which was reached using the JSR mnemonic.  When the JSR
 * instruction is called, the program counter register value is pushed to the stack.  When RTS is called, this value is
 * retrieved from the stack, incremented by one and stored as offset to the next executable instruction.
 * No flags are affected by this call.
 */
public class RTS extends OpCode {

	@Override
	public int[] getASM() {
		return new int[] { 0x60 };
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public int getBaseCycles() {
		return 6;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException,
		MemoryMissingException, StackOverflowException
	{
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// Pull the program counter from the stack
		memory.stack.pullPC();
		// Increment the program counter by one.
		memory.registers.replace("PC", memory.registers.get("PC") + 1);
	}
}
