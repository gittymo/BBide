package com.plus.mevanspn.bridge.Processor.OpCodes.Other;

import com.plus.mevanspn.bridge.Processor.OpCode;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bridge.Storage.RAM.MemoryMissingException;

/** The LDA class is used to create objects representing the LDA assembler mnemonic in
 * BBide programs.  The purpose of this mnemonic is to load the Accumulator with a value
 * from memory.  The (Z)ero flag is set if the Accumulator is loaded with zero (0) and the
 * (N)egative flag is set if bit 7 of the Accumulator is set.  No other flags in the status
 * register are affected by this operation. */
public class LDA extends OpCode {
	@Override
	public int[] getASM() {
		return new int[0];
	}

	@Override
	public int getSize() {
		switch (addressMode) {
			case Immediate:
			case ZeroPage:
			case ZeroPageX:
			case PreIndirectX:
			case PostIndirectY: return 2;
			case Absolute:
			case AbsoluteX:
			case AbsoluteY: return 3;
		}

		return 0;
	}

	@Override
	public int getBaseCycles() {
		switch (addressMode) {
			case Immediate: return 2;
			case ZeroPage: return 3;
			case ZeroPageX:
			case Absolute:
			case AbsoluteX:
			case AbsoluteY: return 4;
			case PostIndirectY: return 5;
			case PreIndirectX: return 6;
		}

		return 0;
	}

	public LDA(int address, AddressMode addressMode) {
		this.address = address;
		this.addressMode = addressMode;
	}

	@Override
	public void perform(Memory memory) throws InvalidAddressModeException, InvalidAddressException, MemoryMissingException {
		// Make sure we've got a valid memory object
		if (memory == null) throw new MemoryMissingException();
		if (addressMode == null) throw new InvalidAddressModeException();
		memory.registers.replace("A", memory.getValueAt(this.address, this.addressMode));
	}

	private final AddressMode addressMode;
	private final int address;
}
