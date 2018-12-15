package com.plus.mevanspn.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;

public class ADC extends com.plus.mevanspn.bridge.Processor.OpCode {
	@Override
	public int getASM() {
		return 0;
	}

	@Override
	public int getSize() {
		return addressMode.getSize();
	}

	@Override
	public void perform(Memory memory)
					throws InvalidAddressException, InvalidAddressModeException
	{
		char accumulator = memory.registers.get('A');
		char mem = memory.getValueAt(address, addressMode);
		char total = (char) (accumulator + mem);
		if (memory.flags.get('C')) total += 1; // Add 1 if carry flag set.

		if (total > 255) {
			// Total is greater value that a byte can hold, so set carry flag.
			memory.flags.replace('C', Boolean.TRUE);
			// Remove 256 from the total.
			total -= 256;
		} else if (total > 127 && accumulator < 128) {
			// Has a carry into bit 7, so set oVerflow flag.
			memory.flags.replace('V', Boolean.TRUE);
		}

		// Set the negative and zero flags appropriately.
		setNegativeZeroFlags(memory);

		// Store the total in the accumulator.
		memory.registers.replace('A', total);
	}

	public ADC(char value, AddressMode addressMode, int address) {
		this.value = Memory.getValidByteValue(value);
		this.addressMode = addressMode;
		this.address = address;
	}

	private AddressMode addressMode;
	private char value;
	private int address;
}
