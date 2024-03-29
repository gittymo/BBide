package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;

import com.plus.mevanspn.bbide.bridge.Processor.OpCode;

/** The BCC class allows for the creation of BCC (Branch if Carry Clear) mnemonic objects within a BBIDE pseudo program.
 * The BCC mnemonic allows us to perform a relative branch - or jump - from the current execution address of the
 * program if the carry flag is not set (0)  As a relative address this can be anything in the range of -128 to 127
 * bytes from the current point of execution.  Branch commands only affect the position of the Program Counter.  No
 * other register or flag is affected by a branching command.
 */
public class BCC extends OpCode {

	@Override
	public int[] getASM() {
		return new int[] { 0x90, address };
	}

	@Override
	public int getSize() {
		return 2;
	}

	@Override
	public int getBaseCycles() {
		return 3;
	}

	public BCC(int address) {
		if (address < -128) address = -128;
		if (address > 127) address = 127;
		this.address = address;
	}

	@Override
	public void perform(Memory memory) {
		// Get the new program counter address
		int newPCAddress = memory.registers.get("PC") + address;
		// If the carry flag is clear (0) we can move the program counter to new address.
		if (!memory.flags.get('C')) memory.registers.replace("PC", newPCAddress);
	}

	private final int address;
}
