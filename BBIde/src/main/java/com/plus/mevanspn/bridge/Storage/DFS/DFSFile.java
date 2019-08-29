/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.bridge.Storage.DFS;

/**
 * A class that implements the DFSFile interface must provide the basic methods 
 * that control the flow of catalogue information and data between a real-world 
 * file and the DFS file system.
 * 
 * @author win10
 */
public interface DFSFile {
    /**
     * Returns the file name of the file.
     * @return A reflection of the filename stored within a DFS Disk catalogue.
     */
    public String getFileName();
    public void setFileName(String file_name);
    public int getLoadAddress();
    public void setLoadAddress(int load_address);
    public int getExecAddress();
    public void setExecAddress(int exec_address);
    public short getStartSector();
    public void setStartSection(short start_sector);
    public char getDirectory();
    public void setDirectory(char directory);
    public DFSDisk getParentDisk();
    public void setParentDisk(DFSDisk parent_disk);
    public int getLength();
    public char[] getData();
}
