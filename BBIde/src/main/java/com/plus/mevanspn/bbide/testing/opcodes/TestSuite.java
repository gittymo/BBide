package com.plus.mevanspn.bbide.testing.opcodes;

import com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Branching.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Logic.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Mathematical.*;
import com.plus.mevanspn.bbide.bridge.Processor.OpCodes.Other.*;
import com.plus.mevanspn.bbide.bridge.Storage.RAM.*;
import javax.swing.*;
import java.awt.*;
import javax.swing.event.*;
import java.awt.event.*;

public class TestSuite extends JFrame {
	public TestSuite() {
		super("OpCodes Test Suite - (C)2020 Morgan Evans");
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setPreferredSize(new Dimension(540,540));
		this.pack();
	}

	public static void main(String args[]) {
		SwingUtilities.invokeLater(new Runnable() {
			public void run() {
				new TestSuite().setVisible(true);
			}
		});
	}
}
