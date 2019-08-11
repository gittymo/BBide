package com.plus.mevanspn.bridge.Processor;

import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressException;
import com.plus.mevanspn.bridge.Storage.RAM.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Storage.RAM.Memory;
import com.plus.mevanspn.bridge.Storage.RAM.MemoryMissingException;
import com.plus.mevanspn.bridge.Storage.RAM.StackOverflowException;

public abstract class OpCode {
	/**
	 * Returns the assembler code for this instruction as an array of byte values
	 * @return The assembler code including immediate values and addresses
	 */
	public abstract int[] getASM();

	/**
	 * Returns the size in bytes of the code for this instruction
	 * @return Size of instruction in bytes (including immediate values and addresses)
	 */
	public abstract int getSize();

	/**
	 * Returns the base execution cycles for this instruction.  Some instructions will take longer depending upon whether
	 * the instruction performs a branch or crosses a page boundary.
	 * @return The minimum number of cycles required to perform this instruction.
	 */
	public abstract int getBaseCycles();

	/**
	 * Performs the purpose of the instruction on the given memory address.  If an invalid address or address mode is
	 * given, this method will throw the appropriate exception.
	 * @param memory The target memory object to perform the instruction on.
	 * @throws InvalidAddressException
	 * @throws MemoryMissingException
	 * @throws StackOverflowException
	 */
	public abstract void perform(Memory memory) throws
					InvalidAddressException, MemoryMissingException,
					StackOverflowException, InvalidAddressModeException;

	public enum AddressMode {
		Immediate(2,2),
		ZeroPage(2,3),
		ZeroPageX(2,4),
		ZeroPageY(2, 4),
		Absolute(3,4),
		AbsoluteX(3,4),
		AbsoluteY(3,4),
		PreIndirectX(2,6),
		PostIndirectY(2,5),
		Accumulator( 1, 2),
		Indirect(3, 5);

		private int size;
		private int time;
		AddressMode(int size, int time) {
			this.size = size;
			this.time = time;
		}

		public int getSize() { return size; }
		public int getBaseTime() { return time; }
	}
}
