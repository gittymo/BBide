package com.plus.mevanspn.bridge.Storage.RAM;

import com.plus.mevanspn.bridge.*;

class MemoryArea {
	MemoryArea(String name, Model model, int start, int end, boolean reserved) 
		throws InvalidAddressException {
		// Make sure start and end are sensible, i.e. not negative...
		if (start < 0 || end < 0) throw new InvalidAddressException();
		// ... and start comes before end.
		if (start > end) {
			int temp = end;
			end = start;
			start = temp;
		}
		// Also make sure start and end addresses are within limits of memory
		if (start > model.getRAMSize() || end > model.getRAMSize())
			throw new InvalidAddressException();
		// If we've got here, everything is fine, so set object properties
		this.name = name;
		this.start = start;
		this.end = end;
		this.model = model;
		this.reserved = reserved;
	}

	public boolean addressIsOkay(int address) {
		// If given address does not fit model, say false.
		if (address < 0 || address > model.getRAMSize()) return false;
		// If this memory area is not a reserved area we can automatically say true...
		if (!reserved) return true;
		// ... otherwise, if the address falls within range, say false.
		if (address >= start && address <= end) return false;
		// Finally, the address is outside the reserved space and sensible so say true.
		return true;
	}
	
	private boolean reserved;
	private int start, end;
	private Model model;
	private String name;
}