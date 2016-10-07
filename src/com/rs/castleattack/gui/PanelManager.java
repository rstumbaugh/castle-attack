package com.rs.castleattack.gui;

import javax.swing.JPanel;

public class PanelManager 
{
	public static void switchPanels(JPanel oldPanel, JPanel newPanel)
	{
		oldPanel.setVisible(false);
		newPanel.setVisible(true);
	}
}
