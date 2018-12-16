package com.plus.mevanspn.bridge.Processor.OpCodes.Branching;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;
import com.plus.mevanspn.bridge.Processor.OpCode;

public class BEQ extends OpCode {

	@Override
	public int getASM() {
		return 0;
	}

	@Override
	public int getSize() {
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
