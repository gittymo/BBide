package com.plus.mevanspn.bridge.Storage.RAM;

import com.plus.mevanspn.bridge.Storage.RAM.StackOverflowException;

/* 	Stack.java
		(C)2018/2019 Morgan Evans */

/** Objects derived from the Stack class are used to interact with the 255 byte stack available
 * to the execution environment.  This class cannot be instantiated directly, but an instance is
 * created when an instance of the Memory class is created.
 * There are two possible interactions with the stack: push and pull.  The structure works in a 
 * last in first out LIFO system of access.
 */

 public class Stack {
	 protected Stack(Memory memory) {
		 // Set the stack pointer to the start of the stack
		 memory.registers.replace("SP",0x100);
		 // Store the memory object reference
		 this.memory = memory;
	 }

	 public void push(int value) throws StackOverflowException {
		 int stackPointer = memory.registers.get("SP");
			while (stackPointer < 0x1FF && value > 0) {
				memory.memory[stackPointer++] = value % 256;
				value = value / 256;
			}
			memory.registers.replace("SP", stackPointer);
			if (value > 0) throw new StackOverflowException();
	 }

	public int pull() {
		int stackPointer = memory.registers.get("SP");
		if (stackPointer > 0x100) {
			memory.registers.replace("SP", stackPointer - 1);
			return memory.memory[stackPointer];
		}
		return 0;
	}

	private Memory memory;
 }