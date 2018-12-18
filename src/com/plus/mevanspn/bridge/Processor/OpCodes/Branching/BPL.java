package com.plus.mevanspn.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;
import com.plus.mevanspn.bridge.Processor.OpCode;

/* The BPL class allows for the creation of BPL (Branch if PLus) mnemonic objects within a
	* BBIDE pseudo program.  The BPL allows us to perform a relative branch - or jump - from
	* the current execution address of the program if the negative flag is not set (0)
	  * As a relative address this can be anything in the range of -128 to 127 bytes from the
	  * current point of execution.  Brnach commands nly affect the position of the Program
	  * Counter.  No other register or flag is affected by a branching command.
 */
public class BPL extends OpCode {

	@Override
	public char[] getASM() {
		return null;
	}

	@Override
	public int getSize() {
		return 2;
	}

	@Override
	public int getBaseCycles() {
		return 0;
	}

	public BPL(int address) {
		this.address = address;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException {
		// Make sure address value is within relative range.
		if (address < -128 || address > 127) throw new InvalidAddressException();
		// Get the new program counter address
		int newPCAddress = memory.registers.get("PC") + address;
		// If the negative flag is set (1) we can move the program counter to new address.
		if (!memory.flags.get('N')) memory.registers.replace("PC", newPCAddress);
	}

	private int address;
}