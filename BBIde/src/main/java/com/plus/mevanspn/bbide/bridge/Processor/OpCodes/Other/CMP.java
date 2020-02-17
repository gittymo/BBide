package com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCode;

/** THe CMP class is used to create objects representing the CMP assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to compare the contents of the
 * Accumulator with the value at a given address in memory (or if immediate addressing
 * is used, the value itself that is given) by substracting the memory value from the
 * accumulator. Depening on the result, the following flags
 * are set:
 * (Z)ero if Accumulator equals the value in memory
 * (N)egative if bit 7 is set in the result
 * (C)arry if Accumulator is greater than value in memory.
 * 
 * The value of the accumulator is preserved after the operation and no other flags are 
 * affected. */
public class CMP extends OpCode {
	@Override
	public int[] getASM() {
		switch (addressMode) {
			case Immediate: return new int[] { 0xC9 };
			case ZeroPage: return new int[] { 0xC5, addressOrValue & 0xFF };
			case ZeroPageX: return new int[] { 0xD5, addressOrValue & 0xFF };
			case Absolute: return new int[] { 0xCD, addressOrValue & 0xFF, (addressOrValue & 0xFF00) >> 8 };
			case AbsoluteX: return new int[] { 0xDD, addressOrValue & 0xFF, (addressOrValue & 0xFF00) >> 8 };
			case AbsoluteY: return new int[] { 0xD9, addressOrValue & 0xFF, (addressOrValue & 0xFF00) >> 8 };
			case PreIndirectX: return new int[] { 0xC1, addressOrValue & 0xFF };
			case PostIndirectY: return new int[] { 0xD1, addressOrValue & 0xFF };
			default: return null;
		}
	}

	@Override
	public int getSize() {
		if (addressMode == null) return 0;
		switch (addressMode) {
			case Immediate:
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
			default : return 0;
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
			case Immediate : result = memory.registers.get("A") - addressOrValue; break;
			case ZeroPage:
			case ZeroPageX:
			case Absolute:
			case AbsoluteX:
			case AbsoluteY:
			case PreIndirectX:
			case PostIndirectY: result = memory.registers.get("A") - 
				memory.getValueAt(addressOrValue, addressMode); break;
			default: throw new InvalidAddressModeException();
		}

		// Set Carry, Zero and Negative flags according to the result.
		setFlagsBasedUponResult(result, memory);
	}

	private final AddressMode addressMode;
	private final int addressOrValue;
}
