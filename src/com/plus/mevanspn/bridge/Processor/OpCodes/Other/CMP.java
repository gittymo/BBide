package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.MemoryMissingException;
import com.plus.mevanspn.bridge.Memory;
import com.plus.mevanspn.bridge.Processor.OpCode;

/** THe CLC class is used to create objects representing the CLC assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to clear the carry flag which is often
 * set during arithmetic and bit shift operations. If is advisable to do with before
 * performing and ADC operation. No other flags in the status register are affected by this
 * operation. */
public class CMP extends OpCode {
	@Override
	public char[] getASM() {
		return new char[0];
	}

	@Override
	public int getSize() {
		if (addressMode == null) return 0;
		switch (addressMode) {
			case Immediate :
			case ZeroPage:
			case ZeroPageX:
			case PreIndirectX:
			case PostIndirectY: return 2;
			case Absolute:
			case AbsoluteX:
			case AbsoluteY: return 3;
			default: return 0;
		}
	}

	@Override
	public int getBaseCycles() {
		if (addressMode == null) return 0;
		switch (addressMode) {
			case Immediate : return 2;
			case ZeroPage : return 3;
			case ZeroPageX :
			case Absolute :
			case AbsoluteX :
			case AbsoluteY : return 4;
			case PreIndirectX : return 6;
			case PostIndirectY : return 5;
			default : 0;
		}
	}

	public CMP(int addressOrValue, AddressMode addressMode) {
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

		// Get comparison result (A - value (or value at memory address))
		int result;
		switch (addressMode) {
			case Immediate : result = memory.registers.get('A') - addressOrValue; break;
			case ZeroPage:
			case ZeroPageX:
			case Absolute:
			case AbsoluteX:
			case AbsoluteY:
			case PreIndirectX:
			case PostIndirectY: result = memory.registers.get('A') - 
				memory.getValueAt(addressOrValue, addressMode); break;
			default: throw new InvalidAddressModeException();
		}

		// Set Carry, Zero and Negative flags according to the result.
		setFlagsBasedUponResult(result, memory);
	}

	private AddressMode addressMode;
	private int addressOrValue;
}
