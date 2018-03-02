/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.bridge;

/**
 *
 * @author win10
 */
public class DFSFile {
	String name;											// 7 chars max
	int load_addr, exec_addr, length; // All 18 bits
	short start_sector;								// 10 bits
	char directory;										// Just one character!
	
	DFSFile(String name, char dir, int length, short start_sector) {
		this.name = (name.length() > 7) ? name.substring(0, 7) : name;
		this.directory = dir;
		this.load_addr = this.exec_addr = 0;
		this.length = length;
		this.start_sector = start_sector;
	}
}

