package com.plus.mevanspn.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bridge.Storage.RAM.*;
import com.plus.mevanspn.bridge.Processor.OpCode;
/** The BEQ class allows for the creation of BCS (Branch if EQual) mnemonic objects within a BBIDE pseudo program.
 * The BEQ mnemonic allows us to perform a relative branch - or jump - from the current execution address of the
 * program if the zero flag is set (1)  As a relative address this can be anything in the range of -128 to 127
 * bytes from the current point of execution.  Branch commands only affect the position of the Program Counter.  No
 * other register or flag is affected by a branching command.
 */
public class BEQ extends OpCode {

	@Override
	public char[] getASM() {
		return null;
	}

	@Override
	public int getSize() {
		return 0;
	}

	@Override
	public int getBaseCycles() {
		return 0;
	}

	public BEQ(int address) {
		this.address = address;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException {
		// Make sure address value is within relative range.
		if (address < -128 || address > 127) throw new InvalidAddressException();
		// Get the new program counter address
		int newPCAddress = memory.registers.get("PC") + address;
		// If the zero flag is set (1) we can move the program counter to new address.
		if (memory.flags.get('Z')) memory.registers.replace("PC", newPCAddress);
	}

	private int address;
}
