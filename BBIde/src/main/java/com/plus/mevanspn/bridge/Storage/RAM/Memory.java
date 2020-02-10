package com.plus.mevanspn.bridge.Storage.RAM;

import java.util.HashMap;
import java.util.Vector;
import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.*;

public class Memory {
	public Memory(Model model) {
		program = new Vector<>();
		this.model = model;
		initFlags();
		initRegisters();
		initMemory();
	}

	public static char getValidByteValue(char value) {
		return (char) ((value < 0) ? (-value) % 256 : value % 256);
	}

	public static int getValidShortValue(int value) {
		return (value < 0) ? (-value) % 65536 : value % 65536;
	}

	public int getValueAt(int offset, OpCode.AddressMode addressMode)
		throws InvalidAddressException
	{
		if (offset < 0 || offset >= model.getRAMSize()) throw new InvalidAddressException();
		if (offset > 255 && (
						addressMode != OpCode.AddressMode.Absolute && 
						addressMode != OpCode.AddressMode.AbsoluteX &&
						addressMode != OpCode.AddressMode.AbsoluteY
				)) throw new InvalidAddressException();
		switch (addressMode) {
			case Immediate: return offset;
			case ZeroPage:
			case Absolute: return memory[offset];
			case ZeroPageX: {
				return memory[offset + registers.get("X")];
			}
			case ZeroPageY: {
				return memory[offset + registers.get("Y")];
			}
			case AbsoluteX: {
				int xOffset = offset + registers.get("X");
				if (xOffset >= model.getRAMSize()) throw new InvalidAddressException();
				return memory[xOffset];
			}
			case AbsoluteY: {
				int yOffset = offset + registers.get("Y");
				if (yOffset >= model.getRAMSize()) throw new InvalidAddressException();
				return memory[yOffset];
			}
			case PreIndirectX: {
				// 16 bit address is stored in (base + x, base + 1 + x) where base < &100
				int baseOffset = offset + registers.get("X");
				if (baseOffset + 1 >= model.getRAMSize()) throw new InvalidAddressException();
				int indirectAddress =
								(memory[baseOffset] & 255) + ((memory[baseOffset + 1] & 255) << 8);
				return memory[indirectAddress];
			}
			case PostIndirectY: {
				// 16 bit address is a combination of the indirect 16 bit address taken
				// from (base, base + 1) which is then incremented by the value stored in
				// Y
				if (offset + 1 >= model.getRAMSize()) throw new InvalidAddressException();
				int indirectAddress =
								(memory[offset] & 255) + ((memory[offset + 1] & 255) << 8) +
												registers.get("Y");
				if (indirectAddress >= model.getRAMSize()) throw new InvalidAddressException();
				return memory[indirectAddress];
			}
			case Accumulator: {
				return registers.get("A");
			}
			case XRegister: {
				return registers.get("X");
			}
			case YRegister: {
				return registers.get("Y");
			}
			case Indirect : {
				if (offset + 1 >= model.getRAMSize()) throw new InvalidAddressException();
				int indirectAddress =
								(memory[offset] & 255) + ((memory[offset + 1] & 255) << 8);
				if (indirectAddress >= model.getRAMSize()) throw new InvalidAddressException();
				return indirectAddress;
			}
			default: throw new InvalidAddressException();
		}
	}
	
	public void setValueAt(int value, int offset, OpCode.AddressMode addressMode)
		throws InvalidAddressModeException, InvalidAddressException
	{
		if (offset < 0 || offset >= model.getRAMSize()) throw new InvalidAddressException();
		if (offset > 255 && (
						addressMode != OpCode.AddressMode.Absolute && 
						addressMode != OpCode.AddressMode.AbsoluteX &&
						addressMode != OpCode.AddressMode.AbsoluteY
				)) throw new InvalidAddressException();
		switch (addressMode) {
			case Immediate : throw new InvalidAddressModeException();
			case ZeroPage :
			case Absolute : {
				memory[offset] = value;
			} break;
			
			case ZeroPageX : {
				memory[offset + registers.get("X")] = value;
			} break;

			case ZeroPageY: {
				memory[offset + registers.get("Y")] = value;
			}

			case AbsoluteX : {
				if (offset + registers.get("X") >= model.getRAMSize())
					throw new InvalidAddressException();
				memory[offset + registers.get("X")] = value;
			} break;
			
			case AbsoluteY : {
				if (offset + registers.get("Y") >= model.getRAMSize())
					throw new InvalidAddressException();
				memory[offset + registers.get("Y")] = value;
			} break;
			
			case PreIndirectX : {
				int baseOffset = offset + registers.get("X");
				if (baseOffset + 1 >= model.getRAMSize()) throw new InvalidAddressException();
				int indirectAddress =
								(memory[baseOffset] & 255) + ((memory[baseOffset + 1] & 255) << 8);
				if (indirectAddress >= model.getRAMSize()) throw new InvalidAddressException();
				memory[indirectAddress] = value;
			} break;
			
			case PostIndirectY : {
				if (offset + 1 >= model.getRAMSize()) throw new InvalidAddressException();
				int indirectAddress =
								(memory[offset] & 255) + ((memory[offset + 1] & 255) << 8) +
												registers.get("Y");
				if (indirectAddress >= model.getRAMSize()) throw new InvalidAddressException();
				memory[indirectAddress] = value;
			} break;

			case Accumulator : {
				registers.replace("A", value);
			} break;

			case XRegister : {
				registers.replace("X", value);
			} break;

			case YRegister : {
				registers.replace("Y", value);
			}

			default : throw new InvalidAddressModeException();
		}
	}

	/**
	 * Performs an update of the negative and zero flags.
	 */
	public void setNegativeZeroFlags(int address, OpCode.AddressMode addressMode) throws InvalidAddressException {
		int value = this.getValueAt(address, addressMode);
		if (value > 127)
			flags.replace('N', Boolean.TRUE);
		else
			flags.replace('N', Boolean.FALSE);

		if (value == 0)
			flags.replace('Z', Boolean.TRUE);
		else
			flags.replace('Z', Boolean.FALSE);
	}


	private void initRegisters() {
		registers = new HashMap<>();
		registers.clear();
		registers.put("A", 0); // Accumulator
		registers.put("X", 0); // X register
		registers.put("Y", 0); // Y register
		registers.put("PC", 0); // Program counter
		registers.put("SP", 0x100); // Stack pointer
	}

	private void initFlags() {
		flags = new HashMap<>();
		flags.clear();
		flags.put('C', false);  // Carry flag
		flags.put('V', false);  // Overflow flag
		flags.put('N', false);  // Negative flag
		flags.put('B', false);  // Break flag
		flags.put('D', false);  // Decimal flag (BCD arithmetic mode)
		flags.put('Z', false);  // Zero flag
		flags.put('I', false);  // Interrupt disable
	}

	private void initMemory() {
		memory = new int[model.getRAMSize()];
		for (int i = 0; i < model.getRAMSize(); i++)
			memory[i] = 0;
		stack = new Stack(this);
	}

	public int getSize() {
		return memory.length;
	}
	
	private final Vector<OpCode> program;
	private final Model model;
	public HashMap<String, Integer> registers;
	public HashMap<Character, Boolean> flags;
	public int[] memory;
	public Stack stack;
}
