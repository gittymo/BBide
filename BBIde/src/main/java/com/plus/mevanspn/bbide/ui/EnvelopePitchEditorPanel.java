/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.plus.mevanspn.bbide.ui;

import javax.swing.*;
import java.awt.*;

/**
 *
 * @author win10
 */
public final class EnvelopePitchEditorPanel extends JPanel {
	public EnvelopePitchEditorPanel() {
		super();
		this.setPreferredSize(new Dimension(480,240));
		this.setMinimumSize(new Dimension(480,240));
		this.setMaximumSize(new Dimension(480,240));
	}
	
 @Override
 public void paintComponent(Graphics g) {
	 super.paintComponent(g);
	 
	 Graphics2D g2 = (Graphics2D) g;
	 g.setColor(Color.WHITE);
	 g.fillRect(0, 0, this.getWidth(), this.getHeight());
	 
 }
}
