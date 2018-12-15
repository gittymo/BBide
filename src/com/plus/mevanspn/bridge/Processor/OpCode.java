package com.plus.mevanspn.bridge.Processor;

import com.plus.mevanspn.bridge.InvalidAddressException;
import com.plus.mevanspn.bridge.InvalidAddressModeException;
import com.plus.mevanspn.bridge.Memory;

public abstract class OpCode {
	public abstract int getASM();
	public abstract int getSize();
	public abstract void perform(Memory memory)
					throws InvalidAddressModeException, InvalidAddressException;
	public void setNegativeZeroFlags(Memory memory) {
		char currentAccumulator = memory.registers.get('A');
		if (currentAccumulator > 127)
			memory.flags.replace('N', Boolean.TRUE);
		else
			memory.flags.replace('N', Boolean.FALSE);

		if (currentAccumulator == 0)
			memory.flags.replace('Z', Boolean.TRUE);
		else
			memory.flags.replace('Z', Boolean.FALSE);

	}
	public enum AddressMode {
		Immediate(2,2),
		ZeroPage(2,3),
		ZeroPageX(2,4),
		Absolute(3,4),
		AbsoluteX(3,4),
		AbsoluteY(3,4),
		PreIndirectX(2,6),
		PostIndirectY(2,5);

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
