package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCode;

/** THe BRK class is used to create objects representing the BRK assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to dforce an interrupt to occur.  The
 * program counter is pushed onto the stack, followed by the status register.  The processor
 * jumps to memory location 0xFFEE.  Usually a BRK instruction represents an error condition.
 * Other than pushing the program counter and status register to the stack, the break flag is
 * also set.  No other flags are changed.
 */
public class BRK extends OpCode {
	@Override
	public int[] getASM() {
		return new int[] { 0x00 };
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public int getBaseCycles() {
		return 7;
	}

	@Override
	public void perform(Memory memory) throws
			MemoryMissingException
	{
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		// Set break flag
		memory.flags.replace('B', true);
		// Get the index of the stack pointer
		int spIndex = memory.registers.get("SP");
		// Check if there's enough room on the stack
		if (spIndex < 0x01FD) {
			// There is so push the program counter onto the stack.
			// Get the contents of the program counter.
			int pcValue = memory.registers.get("PC");
			memory.memory[spIndex++] = (char) (pcValue & 0xFF00) >> 8;
			memory.memory[spIndex++] = (char) (pcValue & 0xFF);
			// Store the status register
			memory.memory[spIndex] = 0;
			if (memory.flags.get('C')) memory.memory[spIndex] += 1;
			if (memory.flags.get('Z')) memory.memory[spIndex] += 2;
			if (memory.flags.get('I')) memory.memory[spIndex] += 4;
			if (memory.flags.get('D')) memory.memory[spIndex] += 8;
			if (memory.flags.get('B')) memory.memory[spIndex] += 16;
			if (memory.flags.get('V')) memory.memory[spIndex] += 32;
			if (memory.flags.get('N')) memory.memory[spIndex] += 64;
			memory.registers.replace("SP", spIndex + 1);
		}
	}
}
