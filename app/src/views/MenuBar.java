package views;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.swing.JFileChooser;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;

import models.Game;

import controllers.Controller;

/*
 * The menu bar for the game.
 */
public class MenuBar extends JMenuBar
{
	private Controller controller;
	
	public MenuBar(Controller controller)
	{
		super();
		this.controller = controller;
		
		JMenu fileMenu = new JMenu("File");
		fileMenu.setMnemonic('F');

		JMenuItem saveGameItem = new JMenuItem("Save game");
		saveGameItem.setMnemonic('S');
		fileMenu.add(saveGameItem);
		saveGameItem.addActionListener(new SaveGameListener());
		
		JMenuItem loadGameItem = new JMenuItem("Load game");
		loadGameItem.setMnemonic('L');
		fileMenu.add(loadGameItem);
		loadGameItem.addActionListener(new LoadGameListener());
		
		 
		JMenuItem newGameItem = new JMenuItem("New game");
		newGameItem.setMnemonic('N');
		fileMenu.add(newGameItem);
		newGameItem.addActionListener(new NewGameListener());
		  
		JMenuItem exitItem = new JMenuItem("Exit");
		exitItem.setMnemonic('E');
		fileMenu.add(exitItem);
		exitItem.addActionListener(new ExitGameListener());
			   
		add( fileMenu );
	}
	
	/*
	 * Handles the saving of a users game.
	 */
	private class SaveGameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			File fileName = getFile();
			if (fileName == null) return;
			
			writeGameToFile(fileName);
		}
		
		public File getFile()
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
			int result = fileChooser.showSaveDialog(MenuBar.this.getParent());
			
			if (result == JFileChooser.CANCEL_OPTION)
				return null;
			
			File fileName = fileChooser.getSelectedFile();
			
			if ((fileName == null) || (fileName.getName().equals("")))
			{
				JOptionPane.showMessageDialog(MenuBar.this.getParent(), "Invalid File Name",
						"Invalid File Name", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			return fileName;
		}
		
		public void writeGameToFile(File fileName)
		{
			try {
				controller.getBoard().clearBoardStates();
				
				ObjectOutputStream output = new ObjectOutputStream(new FileOutputStream(fileName));
				output.writeObject(controller.getGame());
				output.close();
				
			} catch (IOException e) {
				System.err.println( "Error saving file." );
			}
		}
	}
	
	private class LoadGameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			File fileName = getFile();
			if (fileName == null) return;
			
			Game game = loadGameFromFile(fileName);
			controller.handleMessage(Controller.MESSAGE_LOAD_GAME, game);
		}
		
		public File getFile()
		{
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
			
			int result = fileChooser.showOpenDialog(MenuBar.this.getParent());
			
			if (result == JFileChooser.CANCEL_OPTION)
				return null;
			
			File fileName = fileChooser.getSelectedFile();
			
			if ((fileName == null) || (fileName.getName().equals("")))
			{
				JOptionPane.showMessageDialog(MenuBar.this.getParent(), "Invalid File Name",
						"Invalid File Name", JOptionPane.ERROR_MESSAGE);
				return null;
			}
			
			return fileName;
		}
		
		public Game loadGameFromFile(File fileName)
		{
			try {
				ObjectInputStream input = new ObjectInputStream(new FileInputStream(fileName));
				Game game = (Game) input.readObject();
				input.close();
				
				return game;
			} catch (IOException | ClassNotFoundException e) {
				JOptionPane.showMessageDialog(
						MenuBar.this.getParent(), 
						"Error loading Game",
						"Error!", 
						JOptionPane.ERROR_MESSAGE);
			}
			return null;
		}
	}
	
	/*
	 * Handles the starting a new game.
	 */
	private class NewGameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
			controller.handleMessage(Controller.MESSAGE_NEW_GAME, null);
		}
	}
	
	/*
	 * Handles the users exiting the game
	 */
	private class ExitGameListener implements ActionListener
	{
		@Override
		public void actionPerformed(ActionEvent e)
		{
            System.exit( 0 );
		}
	}
}
