package com.plus.mevanspn.bridge.Processor.OpCodes.Mathematical;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;

public class ADC extends com.plus.mevanspn.bridge.Processor.OpCode {
	@Override
	public char[] getASM() {
		return null;
	}

	@Override
	public int getSize() {
		return addressMode.getSize();
	}

	@Override
	public int getBaseCycles() {
		return 0;
	}

	@Override
	public void perform(Memory memory)
					throws InvalidAddressException, InvalidAddressModeException
	{
		// We don't allow direct access to the accumulator with this opcode, so throw an exception.
		if (addressMode == AddressMode.Accumulator)
			throw new InvalidAddressModeException();

		// Get the value from the accumulator.
		int accumulator = memory.registers.get("A");
		// Get the value from the memory location given.
		int mem = memory.getValueAt(address, addressMode);
		// Add the memory and accumulator values together.
		int total = accumulator + mem;
		// Add 1 if carry flag set.
		if (memory.flags.get('C')) total += 1;

		// Check if total > 255
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
		memory.setNegativeZeroFlags();

		// Store the total in the accumulator.
		memory.registers.replace("A", total);
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
