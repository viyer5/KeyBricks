package code.gui;
import java.io.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFrame;

import code.fileIO.FileIO;
import code.model.KeyBricksModel;

public class SaveEventHandler implements ActionListener {
	private KeyBricksModel _km;
	private JFrame _win;
	
	public SaveEventHandler(KeyBricksModel w){
		_km = w;
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		// TODO Auto-generated method stub
		_km.dataToString();
		
		FileIO.writeStringToFile("viyer5.kbr", _km._save);
		

	}

}
