package com.plus.mevanspn.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.*;

/** The RTI class allows for the creation of RTI (Return From Interrupt) mnemonic objects within a
 * BBIDE pseudo program.
 * The RTI mnemonic is used to return from an interrupt handling routine.  When an intterupt occurs the current program
 * counter and status register are pushed onto the stack.  These are restored by the RTI instruction.
 * All flags in the statu register can be affected by this call.
 */
public class RTI extends OpCode {

	@Override
	public int[] getASM() {
		return new int[] { 0x40 };
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
		// Pull the status register (flags) from the stack
		memory.setFlags(memory.stack.pull());
		// Pull the program counter from the stack
		memory.stack.pullPC();
	}
}
