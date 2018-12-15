package com.plus.mevanspn.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;
import com.plus.mevanspn.bridge.Processor.OpCode;

import java.util.concurrent.atomic.DoubleAccumulator;

public class ASL extends OpCode {

	@Override
	public int getASM() {
		return 0;
	}

	@Override
	public int getSize() {
		return 0;
	}

	public ASL(AddressMode addressMode, int address) {
		this.addressMode = addressMode;
		this.address = address;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException {
		// Get the base value
		char baseValue = (addressMode == AddressMode.Accumulator) ?
						memory.registers.get('A') : memory.getValueAt(address, addressMode);
		// Get the value shifted one place to the left (i.e. value x 2)
		char shiftedValue = (char) (memory.registers.get('A') << 1);
		// Determine if the 9th bit is set.
		boolean carry = (shiftedValue > 255) ? true : false;
		// Set the carry flag accoringly.
		memory.flags.replace('C', carry);
		// Is the address mode Accumulator?
		if (addressMode == AddressMode.Accumulator) {
			// Set the negative and zero flags accordingly
			setNegativeZeroFlags(memory);
			// Store the shifted value less the 9th bit in the accumulator
			memory.registers.replace('A', (char) (shiftedValue & 255));
		} else {
			// Set negative flag accordingly
			if ((shiftedValue & 128) > 0)
				memory.flags.replace('N', true);
			else
				memory.flags.replace('N', false);
			// Set zero flag accorindingly
			if ((shiftedValue & 255) == 0)
				memory.flags.replace('Z', true);
			else
				memory.flags.replace('Z', false);
			// Store the shifted value less the 9th bit in the required memory location
			memory.setValueAt(((char) (shiftedValue & 255)), address, addressMode);
		}
	}

	private AddressMode addressMode;
	private int address;
}
