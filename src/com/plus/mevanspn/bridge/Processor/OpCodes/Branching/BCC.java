package com.plus.mevanspn.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bridge.Storage.RAM.*;

import com.plus.mevanspn.bridge.Processor.OpCode;

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
		this.address = address & 0xFF;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException {
		// Make sure address value is within relative range.
		if (address < -128 || address > 127) throw new InvalidAddressException();
		// Get the new program counter address
		int newPCAddress = memory.registers.get("PC") + address;
		// If the carry flag is clear (0) we can move the program counter to new address.
		if (!memory.flags.get('C')) memory.registers.replace("PC", newPCAddress);
	}

	private int address;
}
