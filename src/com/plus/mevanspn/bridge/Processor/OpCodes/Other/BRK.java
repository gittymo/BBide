package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;
import com.plus.mevanspn.bridge.Processor.OpCode;

/** THe BRK class is used to create objects representing the BRK assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to dforce an interrupt to occur.  The
 * program counter is pushed onto the stack, followed by the status register.  The processor
 * jumps to memory location 0xFFEE.  Usually a BRK instruction represents an error condition.
 * Other than pushing the program counter and status register to the stack, the break flag is
 * also set.  No other flags are changed.
 */
public class BRK extends OpCode {
	@Override
	public char[] getASM() {
		return new char[0];
	}

	@Override
	public int getSize() {
		return 1;
	}

	@Override
	public int getBaseCycles() {
		return 0;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException {
		// Set break flag
		memory.flags.replace('B', true);
		// Get the index of the stack pointer
		int spIndex = memory.registers.get("SP");
		// Check if there's enough room on the stack
		if (spIndex < 0x01FD) {
			// There is so push the program counter onto the stack.
			// Get the countents of the program counter.
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

	public BRK(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
	}

	private AddressMode addressMode;
	private int address;
}
