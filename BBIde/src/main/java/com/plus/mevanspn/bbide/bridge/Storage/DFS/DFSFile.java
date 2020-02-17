/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.bbide.bridge.Storage.DFS;

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
    String getFileName();
    void setFileName(String file_name);
    int getLoadAddress();
    void setLoadAddress(int load_address);
    int getExecAddress();
    void setExecAddress(int exec_address);
    short getStartSector();
    void setStartSection(short start_sector);
    char getDirectory();
    void setDirectory(char directory);
    DFSDisk getParentDisk();
    void setParentDisk(DFSDisk parent_disk);
    int getLength();
    char[] getData();
}
