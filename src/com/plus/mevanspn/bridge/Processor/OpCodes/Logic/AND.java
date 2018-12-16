package com.plus.mevanspn.bridge.Processor.OpCodes.Logic;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;

public class AND extends com.plus.mevanspn.bridge.Processor.OpCode {

	@Override
	public int getASM() {
		return 0;
	}

	@Override
	public int getSize() {
		return 0;
	}

	public AND(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException {
		// We don't allow direct access to the accumulator with this opcode, so throw an exception.
		if (addressMode == AddressMode.Accumulator)
			throw new InvalidAddressModeException();
		// Get the accumulator value
		int accumulator = memory.registers.get("A");
		// Get the value stored in the given memory location
		int mem = memory.getValueAt(address, addressMode);
		// AND the two together
		int result = (char) (mem & accumulator);
		// Store the result back into the accumulator.
		memory.registers.replace("A", result);
		// Update the negative and zero flags accordingly.
		setNegativeZeroFlags(memory);
	}

	private AddressMode addressMode;
	private int address;
}
