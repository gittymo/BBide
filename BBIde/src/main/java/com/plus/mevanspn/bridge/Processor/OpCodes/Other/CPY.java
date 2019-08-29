package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Storage.RAM.*;
import com.plus.mevanspn.bridge.Processor.OpCode;

/** THe CPY class is used to create objects representing the CPY assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to compare the contents of the
 * Y register with the value at a given address in memory (or if immediate addressing
 * is used, the value itself that is given) by substracting the memory value from the
 * register. Depening on the result, the following flags
 * are set:
 * (Z)ero if Y register equals the value in memory
 * (N)egative if bit 7 is set in the result
 * (C)arry if Y register is greater than value in memory.
 * 
 * The value of the X register and the memory location are preserved after the operation 
 * and no other flags are affected. */
public class CPY extends OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Immediate: return new int[] { 0xC0, addressOrValue & 0xFF };
			case ZeroPage: return new int[] { 0xC4, addressOrValue & 0xFF };
			case Absolute:
				return new int[] { 0xCC, addressOrValue & 0xFF, (addressOrValue & 0xFF00) >> 8 };
			default: return null;
		}
	}

	@Override
	public int getSize() {
		if (addressMode == null) return 0;
		switch (addressMode) {
			case Immediate :
			case ZeroPage: return 2;
			case Absolute: return 3;
			default: return 0;
		}
	}

	@Override
	public int getBaseCycles() {
		if (addressMode == null) return 0;
		switch (addressMode) {
			case Immediate : return 2;
			case ZeroPage : return 3;
			case Absolute : return 4;
			default : return 0;
		}
	}

	public CPY(int addressOrValue, AddressMode addressMode) {
		this.addressOrValue = addressOrValue;
		this.addressMode = addressMode;
	}

	private void setFlagsBasedUponResult(int result, Memory memory) {
		if (result == 0) {
			memory.flags.replace('C', false);
			memory.flags.replace('Z', true);
			memory.flags.replace('N', false);
		} else if (result > 0) {
			memory.flags.replace('C', true);
			memory.flags.replace('Z', false);
			if (result >= 128) memory.flags.replace('N', true);
		} else {
			memory.flags.replace('C', false);
			memory.flags.replace('Z', false);
			memory.flags.replace('N', false);
		}
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException {
		// Make sure we've got a viable memory object
		if (memory == null) throw new MemoryMissingException();
		// Make sure we're using a valid addressing mode
		if (addressMode != AddressMode.Absolute &&
				addressMode != AddressMode.Immediate &&
				addressMode != AddressMode.ZeroPage) throw new InvalidAddressModeException();
		// Get comparison result (A - value (or value at memory address))
		int result;
		switch (addressMode) {
			case Immediate : result = memory.registers.get("Y") - addressOrValue; break;
			case ZeroPage:
			case Absolute: result = memory.registers.get("Y") - 
				memory.getValueAt(addressOrValue, addressMode); break;
			default: throw new InvalidAddressModeException();
		}

		// Set Carry, Zero and Negative flags according to the result.
		setFlagsBasedUponResult(result, memory);
	}

	private AddressMode addressMode;
	private int addressOrValue;
}
