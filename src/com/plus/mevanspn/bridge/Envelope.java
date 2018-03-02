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
public class Envelope {
	public Envelope(String name, byte step_length, byte auto_repeat, 
					byte[] pitch_changes, short[] step_counts, byte[] amp_changes, 
					byte attack_target, byte decay_target) throws InvalidEnvelopeException {
		if (pitch_changes.length != 3)
			throw new InvalidEnvelopeException("Wrong pitch count: " + pitch_changes.length);
		if (step_counts.length != 3)
			throw new InvalidEnvelopeException("Wrong step count: " + step_counts.length);
		for (int i = 0; i < step_counts.length; i++) {
			if (step_counts[i] < 0) step_counts[i] = (short) -step_counts[i];
			step_counts[i] = (short) (step_counts[i] % 255);
		}
		if (amp_changes.length != 4)
			throw new InvalidEnvelopeException("Wrong amp change count: " + amp_changes.length);
		
		this.name = (name != null && name.length() > 0) ? name : "Envelope" + (id++);
		this.step_length = (step_length < 0) ? 33 : step_length;
		this.auto_repeat = (auto_repeat > 1 || auto_repeat < 0) ? 0 : auto_repeat;
		this.pitch_changes = pitch_changes;
		this.step_counts = step_counts;
		this.amp_changes = amp_changes;
		this.attack_target = (byte) (((attack_target < 0) ? -attack_target : attack_target) % 126);
		this.decay_target = (byte) (((decay_target < 0) ? -decay_target : decay_target) % 126);
	}
	
	String name;
	byte step_length;
	byte auto_repeat;
	byte pitch_changes[];
	short step_counts[];
	byte amp_changes[];
	byte attack_target, decay_target;
	
	static int id = 0;
}
