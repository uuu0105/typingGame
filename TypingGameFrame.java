
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

class TypingGameFrame extends JFrame {
	private TypingGamePanel gamePanel = new TypingGamePanel();
	TypingGamePanel.JGameGroundPanel ground = gamePanel.new JGameGroundPanel();
	ScorePanel scorePanel = new ScorePanel();
	private EditPanel editPanel = new EditPanel();
	
	public TypingGameFrame() {
		setTitle("타이핑 게임");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(makeToolBar(), BorderLayout.NORTH);
		setJMenuBar(makeMenu());
		setSize(800,600);
		setResizable(false);
		splitPane();
		setVisible(true);
		ground.startGame();
		//gamePanel.run();
	}
	public JMenuBar makeMenu() {
		JMenuBar menuBar = new JMenuBar();
		
		JMenu accountMenu = new JMenu("Account");
		//accountMenu.addActionListener(new MenuActionListener());
		accountMenu.add("Sign in");
		accountMenu.add("Sign out");
		accountMenu.addSeparator();
		accountMenu.add("Remove");
		menuBar.add(accountMenu);
		
		JMenu gameMenu = new JMenu("Game");
		//gameMenu.addActionListener(new MenuActionListener());
		gameMenu.add("New Game");
		gameMenu.add("Continue");
		gameMenu.add("Save");
		gameMenu.addSeparator();
		gameMenu.add("Records");
		gameMenu.add("Rank");
		gameMenu.add("Exit");
		menuBar.add(gameMenu);
		
		JMenu settingMenu = new JMenu("Settings");
		//settingMenu.addActionListener(new MenuActionListener());
		settingMenu.add("Color");
		settingMenu.add("Font");
		settingMenu.addSeparator();
		settingMenu.add("Background Music");
		settingMenu.add("Sound Effect");
		settingMenu.addSeparator();
		settingMenu.add("Language");
		menuBar.add(settingMenu);
		
		JMenu infoMenu = new JMenu("Info");
		//infoMenu.addActionListener(new MenuActionListener());
		infoMenu.add("How to play");
		infoMenu.add("About");
		menuBar.add(infoMenu);
		
		return menuBar;
	}
	
	private void splitPane() {
		JSplitPane hPane = new JSplitPane();
		getContentPane().add(hPane, BorderLayout.CENTER);
		
		hPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		hPane.setDividerLocation(500);
		hPane.setEnabled(false);
		hPane.setLeftComponent(gamePanel);
		
		JSplitPane pPane = new JSplitPane();
		hPane.setRightComponent(pPane);
		pPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
		pPane.setDividerLocation(200);
		pPane.setTopComponent(scorePanel);
		pPane.setBottomComponent(editPanel);
	}
	
	
	public JToolBar makeToolBar() {
		JToolBar toolBar = new JToolBar();
		JButton playBtn = new JButton("Play");
		playBtn.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				TypingGamePanel.playStatus="Playing";
			}
		});
		toolBar.add(playBtn);
		
		toolBar.add(new JButton("Pause"));
		toolBar.add(new JButton("Resume"));
		
		return toolBar;
	}
	
	/*
	 * class MyDialog extends JDialog{ private JTextField tf=new JTextField(10);
	 * private JButton okButton = new JButton("OK");
	 * 
	 * public MyDialog(TypingGamePanel frame, String title) { super(); setLayout(new
	 * FlowLayout()); add(tf); add(okButton); setSize(200,100);
	 * 
	 * okButton.addActionListener(new ActionListener() {
	 * 
	 * @Override public void actionPerformed(ActionEvent e) { setVisible(false);
	 * 
	 * } }); } }
	 * 
	 * class MenuActionListener implements ActionListener{ //private MyDialog
	 * dialog; public void actionPerformed(ActionEvent e) { String
	 * cmd=e.getActionCommand(); switch(cmd) { case "How to play" : case "about" :
	 * case "sign in" : case "sign out" : case "Color" : case "Font" : case
	 * "Background Music": case "Sound Effect": case "Language": //dialog=new
	 * MyDialog(gamePanel,"test dialog"); //dialog.setVisible(true); //더 있음 } } }
	 */
	
}
