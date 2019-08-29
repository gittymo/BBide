package com.plus.mevanspn.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bridge.Storage.RAM.*;
import com.plus.mevanspn.bridge.Processor.OpCode;

/* The BVC class allows for the creation of BVC (Branch if oVerflow Clear) mnemonic objects within a
	* BBIDE pseudo program.  The BVC mnemonic allows us to perform a relative branch - or jump - from
	* the current execution address of the program if the overflow flag is not set (0)
	  * As a relative address this can be anything in the range of -128 to 127 bytes from the
	  * current point of execution.  Branch commands only affect the position of the Program
	  * Counter.  No other register or flag is affected by a branching command.
 */
public class BVC extends OpCode {

	@Override
	public int[] getASM() {
		return new int[] { 0x50, address };
	}

	@Override
	public int getSize() {
		return 2;
	}

	@Override
	public int getBaseCycles() {
		return 3;
	}

	public BVC(int address) {
		if (address < -128) address = -128;
		if (address > 127) address = 127;
		this.address = address;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException {
		// Get the new program counter address
		int newPCAddress = memory.registers.get("PC") + address;
		// If the negative flag is set (1) we can move the program counter to new address.
		if (!memory.flags.get('V')) memory.registers.replace("PC", newPCAddress);
	}

	private int address;
}
