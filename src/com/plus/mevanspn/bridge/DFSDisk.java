/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.bridge;

import java.util.*;
import java.io.*;
import java.util.logging.*;

/**
 *
 * @author win10
 */
public class DFSDisk {

	public DFSDisk(DiskFormat format, String name, BootOption boot_opt) {
		this.format = format;
		this.name = (name.length() > 12) ? name.substring(0, 12) : name;
		this.boot_opt = boot_opt;
		this.files = new LinkedList<>();
	}

	public void writeImage(String filename) {
		try {
			FileOutputStream fos = new FileOutputStream(new File(filename));
			DataOutputStream dos = new DataOutputStream(fos);
			try {
				int name_len = this.name.length();
				// WRITE SECTOR 00
				// Write first part of disk name (or all if <8 chars)
				if (name_len < 9) {
					dos.write(this.name.getBytes());
					if (name_len < 8) {
						// Pad any space where disk name < 8 chars
						dos.write(new byte[8 - name_len]);
					}
				} else {
					dos.write(this.name.substring(0, 8).getBytes());
				}
				// Write file and directory details for all files
				for (DFSFile file : this.files) {
					dos.write(file.name.getBytes());
					if (file.name.length() < 7) {
						dos.write(new byte[7 - file.name.length()]);
					}
					dos.writeByte(file.directory);
				}
				// Now pad and remaining space in the sector
				for (int i = 31; i > files.size(); i--) {
					dos.write(new byte[8]);
				}

				// WRITE SECTOR 01
				// Write rest of disk name if name >8 chars.
				if (name_len > 8) {
					dos.write(this.name.substring(8, name_len).getBytes());
					if (name_len < 12) {
						// Pad name space if <12 chars
						dos.write(new byte[12 - name_len]);
					}
				}
				// Write seqeunce no. 0
				dos.writeByte(0);
				// Write number of catalogue entries * 8
				dos.writeByte(this.files.size() * 8);
				// Write number of sectors on disk (2 high bits) & boot opts
				dos.writeByte(
								((this.format.getValue() & 0x300) >> 8)
								+ (this.boot_opt.getValue() << 4)
				);
				// Write remaining sector count (low 8 bits)
				dos.writeByte(this.format.getValue() & 0xFF);
				// Write file length, start sector, load & exec data
				for (DFSFile file : this.files) {
					dos.writeByte(file.load_addr & 0xFF);
					dos.writeByte((file.load_addr & 0xFF00) >> 8);
					dos.writeByte(file.exec_addr & 0xFF);
					dos.writeByte((file.exec_addr & 0xFF00) >> 8);
					dos.writeByte(file.length & 0xFF);
					dos.writeByte((file.length & 0xFF00) >> 8);
					dos.writeByte(((file.start_sector & 0x300) >> 8)
									+ ((file.load_addr & 0x30000) >> 14)
									+ ((file.length & 0x30000) >> 12)
									+ ((file.exec_addr & 0x30000) >> 10)
					);
					dos.writeByte(file.start_sector & 0xFF);
				}
				// Now pad and remaining space in the sector
				for (int i = 31; i > files.size(); i--) {
					dos.write(new byte[8]);
				}
				// TODO: WRITE FILE DATA!

				// Flush the buffer to disk and close the stream.
				dos.flush();
				dos.close();
			} catch (IOException ex) {
				Logger.getLogger(DFSDisk.class.getName()).log(Level.SEVERE, null, ex);
			}
		} catch (FileNotFoundException ex) {
			Logger.getLogger(DFSDisk.class.getName()).log(Level.SEVERE, null, ex);
		}
	}

	public int getFreeSpace(FreeSpaceFormat free_format) {
		double free = (format.getValue() - 2) * free_format.getValue();
		if (files.size() > 0) {
			for (DFSFile file : files) {
				double file_sectors = Math.ceil((double) file.length /
								(256.0d / free_format.getValue()));
				free -= (int) file_sectors;
			}
		}
		return (int) free;
	}
	
	public DFSFile createFile(String name, char dir, int length) throws CatalogueFullException, DiskFullException {
		if (files.size() == 31) throw new CatalogueFullException();
		if (this.getFreeSpace(FreeSpaceFormat.SECTORS) * 256 < length)
			throw new DiskFullException();
		DFSFile new_file = null;
		short next_start_sector = 2;
		if (files.size() > 1) {
			// If there are files in the catalogue, sort by start_sector, ascending.
			Collections.sort(files, new Comparator<DFSFile>() {
				@Override
				public int compare(DFSFile file1, DFSFile file2) {
					return file1.start_sector - file2.start_sector;
				}
			});
		}
		// Now look for any space between files where the new file may fit.
		for (int i = 0; i < files.size(); i++) {
			short file_sector_len = (short) Math.ceil(files.get(i).length / 256.0);
			next_start_sector = (short) (files.get(i).start_sector + file_sector_len);
			if (i + 1 != files.size() &&
							256 * (files.get(i+1).start_sector - next_start_sector) >= length) break;
		}
		
		new_file = new DFSFile(name, dir, length, (short) next_start_sector);
		files.add(new_file);
		return new_file;
	}

	public enum DiskFormat {
		SSSD(400), SSDD(800);
		
		private final int sector_count;
		DiskFormat(int sector_count) {
			if (sector_count != 400 || sector_count !=800 || sector_count !=1600)
				sector_count = 400;
			this.sector_count = sector_count; }
		public int getValue() { return this.sector_count; }
	};
	
	public enum BootOption {
		IGNORE(0), LOAD(1), RUN(2), EXEC(3);
		
		public final int opt_value;
		BootOption(int opt_value) {
			if (opt_value < 0 || opt_value > 3) opt_value = 0;
			this.opt_value = opt_value;
		}
		public int getValue() { return this.opt_value; }
	}
	
	public enum FreeSpaceFormat {
		BYTES(256.0d), SECTORS(1.0d), KILOBYTES(0.25d);
		
		private final double format;
		FreeSpaceFormat(double format) {
			if (format <= 0 || format > 256) format = 1.0d;
			this.format = format;
		}
		
		public double getValue() { return this.format; }
	}
	
	private DiskFormat format;
	private String name;						// 12 characters max!
	private BootOption boot_opt;
	private LinkedList<DFSFile> files;
}