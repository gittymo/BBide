package com.plus.mevanspn.bridge.Processor;

import com.plus.mevanspn.bridge.Memory;

public interface OpCode {
	public int getASM();
	public int getSize();
	public void perform(Memory memory);

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
