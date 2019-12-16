import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;

class TypeDialog extends JDialog{
	private JTextField tf = new JTextField(10);
	private JButton okButton = new JButton("OK");
	
	public TypeDialog(JFrame frame, String title) {
		super(frame, title, true);
		setLayout(new FlowLayout());
		add(tf);
		add(okButton);
		setSize(200,100);
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
	}
}
class InfoDialog extends JDialog{
	private JLabel text1 = new JLabel();
	private JLabel text2 = new JLabel();
	private JButton okButton = new JButton("OK");
	
	public InfoDialog(JFrame frame, String title) {
		super(frame, title, true);
		setLayout(new BorderLayout());

		add(BorderLayout.NORTH,text1);
		add(BorderLayout.CENTER,text2);
		add(BorderLayout.SOUTH,okButton);
		
		setSize(300,160);
		
		okButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
			}
		});
	}
	public void setText(String text1,String text2) {
		this.text1.setText(text1);
		this.text2.setText(text2);
	}
}
public class TypingGameFrame extends JFrame {
	private TypingGamePanel gamePanel = new TypingGamePanel();
	TypingGamePanel.JGameGroundPanel ground = gamePanel.new JGameGroundPanel();
	ScorePanel scorePanel = new ScorePanel();
	private EditPanel editPanel = new EditPanel();
	private InfoDialog htpDialog;
	private InfoDialog aboutDialog;
	public static Color selectedColor; // 선택한 글씨 색
	
	public static void main(String[] args) {
		new TypingGameFrame();
	}
	
	public TypingGameFrame() {
		setTitle("raining game");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		Container container = getContentPane();
		container.setLayout(new BorderLayout());
		container.add(makeToolBar(), BorderLayout.NORTH);
		setJMenuBar(makeMenu());
		setSize(800, 600);
		setResizable(false);
		splitPane();
		setVisible(true);
		ground.startGame();
		// gamePanel.run();
	}
	
	class MenuActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			String cmd = e.getActionCommand();
			if(cmd.contentEquals("Color")) {
				selectedColor = JColorChooser.showDialog(null,"Color",Color.YELLOW);
				if(selectedColor != null) {
					Component[] children = gamePanel.groundPanel.getComponents();
					for(int i=0; i<children.length; i++) {
						children[i].setForeground(selectedColor);
					}
				}
			}
		}
	}
	
	public JMenuBar makeMenu() {
		htpDialog = new InfoDialog(this, "How to play???");
		aboutDialog = new InfoDialog(this, "About");
		
		JMenuBar menuBar = new JMenuBar();

		JMenu accountMenu = new JMenu("Account");
		accountMenu.add("Sign in");
		accountMenu.add("Sign out");
		accountMenu.addSeparator();
		accountMenu.add("Remove");
		menuBar.add(accountMenu);

		JMenu gameMenu = new JMenu("Game");
		gameMenu.add("New Game");
		gameMenu.add("Continue");
		gameMenu.add("Save");
		gameMenu.addSeparator();
		gameMenu.add("Records");
		gameMenu.add("Rank");
		gameMenu.add("Exit");
		menuBar.add(gameMenu);

		JMenu settingMenu = new JMenu("Settings");
		
		JMenuItem colorItem = new JMenuItem("Color");
		colorItem.addActionListener(new MenuActionListener());
		settingMenu.add(colorItem);
		
		settingMenu.add("Font");
		settingMenu.addSeparator();
		settingMenu.add("Background Music");
		settingMenu.add("Sound Effect");
		settingMenu.addSeparator();
		settingMenu.add("Language");
		menuBar.add(settingMenu);

		JMenu infoMenu = new JMenu("Info"); //info 메뉴
		
		JMenuItem htpItem = new JMenuItem("How to play");
		htpItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				htpDialog.setText("Push Play => Start",
						"Type as fast as you can !!! ");
				htpDialog.setVisible(true);
			}
		});infoMenu.add(htpItem);
		
		
		JMenuItem aboutItem = new JMenuItem("About");
		aboutItem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				aboutDialog.setText("Lee Jeong Hyeon / Yuk Seo Jeong",
									"BGM : hisa_FizzyPixel / hisa_");
				aboutDialog.setVisible(true);
			}
		});infoMenu.add(aboutItem);
		
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
				TypingGamePanel.playStatus = "Playing";
			}
		});
		toolBar.add(playBtn);

		return toolBar;
	}
	 
}
