package com.plus.mevanspn.bridge;

import java.util.HashMap;
import java.util.Vector;
import com.plus.mevanspn.bridge.Processor.OpCode;

public class Memory {
	public Memory() {
		program = new Vector<OpCode>();
		initFlags();
		initRegisters();
		initMemory();
	}

	public static char getValidByteValue(char value) {
		return (char) ((value < 0) ? (-value) % 256 : value % 256);
	}

	public static int getValidShortValue(short value) {
		return (int) ((value < 0) ? (-value) % 65536 : value % 65536);
	}

	public char getValueAt(char offset) {
		return memory[offset];
	}

	public char getValueAt(int offset, OpCode.AddressMode addressMode)
		throws InvalidAddressModeException, InvalidAddressException  {
		if (offset < 0 || offset > 32767) throw new InvalidAddressException();
		switch (addressMode) {
			case Immediate: throw new InvalidAddressModeException();
			case ZeroPage: return memory[offset % 256];
			case ZeroPageX: return memory[(offset % 256) + registers.get('X')];
			case Absolute: return memory[offset];
			case AbsoluteX: {
				int xOffset = offset + registers.get('X');
				if (xOffset > 32767) throw new InvalidAddressException();
				return memory[xOffset];
			}
			case AbsoluteY: {
				int yOffset = offset + registers.get('Y');
				if (yOffset > 32767) throw new InvalidAddressException();
				return memory[yOffset];
			}
			case PreIndirectX: {
				// 16 bit adress is stored in (base + x, base + 1 + x) where base < &100
				int baseOffset = offset + registers.get('X');
				int indirectAddress =
								(memory[baseOffset] & 255) + ((memory[baseOffset + 1] & 255) << 8);
				return memory[indirectAddress];
			}
			case PostIndirectY: {
				// 16 bit address is a combination of the indirect 16 bit address taken
				// from (base, base + 1) which is then incremented by the value stored in
				// Y
				int indirectAddress =
								(memory[offset] & 255) + ((memory[offset + 1] & 255) << 8) +
												registers.get('Y');

				return memory[indirectAddress];
			}
			default: throw new InvalidAddressException();
		}
	}

	private void initRegisters() {
		registers = new HashMap<Character, Character>();
		registers.clear();
		registers.put('A', (char) 0); // Accumulator
		registers.put('X', (char) 0); // X register
		registers.put('Y', (char) 0); // Y register
	}

	private void initFlags() {
		flags = new HashMap<Character, Boolean>();
		flags.clear();
		flags.put('C', false);  // Carry flag
		flags.put('O', false);  // Overflow flag
		flags.put('N', false);  // Negative flag
	}

	private void initMemory() {
		memory = new char[32768];
		for (int i = 0; i < 32768; i++)
			memory[i] = 0;
	}

	private Vector<OpCode> program;
	private HashMap<Character, Character> registers;
	private HashMap<Character, Boolean> flags;
	private char[] memory;
}
