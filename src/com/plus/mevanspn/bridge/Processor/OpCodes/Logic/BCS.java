package com.plus.mevanspn.bridge.Processor.OpCodes.Logic;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;
import com.plus.mevanspn.bridge.Processor.OpCode;

public class BCS extends OpCode {

	@Override
	public int getASM() {
		return 0;
	}

	@Override
	public int getSize() {
		return 0;
	}

	public BCS(int address) {
		this.address = address;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException {
		// Make sure address value is within relative range.
		if (address < -128 || address > 100) throw new InvalidAddressException();
		// Get the new program counter address
		int newPCAddress = memory.registers.get("PC") + address;
		// If the carry flag is set (1) we can move the program counter to new address.
		if (memory.flags.get('C')) memory.registers.replace("PC", newPCAddress);
	}

	private int address;
}
