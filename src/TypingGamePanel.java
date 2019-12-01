import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.Vector;

public class TypingGamePanel extends JPanel{
	private JGameGroundPanel groundPanel = new JGameGroundPanel();
	private JInputPanel inputPanel = new JInputPanel();
	//private String newWord;
	//private Words words = new Words("words.txt");
	

	public TypingGamePanel() {
		setLayout(new BorderLayout());
		add(groundPanel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
	}

	class JGameGroundPanel extends JPanel {
		//private JLabel label;
		private JLabel label = new JLabel();
		private JLabel resLabel = new JLabel();
		private Words words = null;
		private String fallingWord = null;
		private FallingThread thread = null;
		private RandomThread randomThread = null;
		private boolean gameOn = false;
		private Vector<String> answerVector = new Vector<String>();
		
		public JGameGroundPanel() {
			setBackground(Color.PINK);
			setLayout(null);
			add(label);
			
			resLabel.setLocation(0, 0);
			resLabel.setSize(100, 30);
			add(resLabel);
			words = new Words("words.txt");
			//label = new JLabel("Ÿ�����غ�����");
			//label.setLocation(100, 50);
			//label.setSize(100, 15);
			//add(label);
		}
		public boolean isGameOn() {
			return gameOn;
		}
		
		public void stopGame() {
			if(thread == null)
				return; // �����尡 ���� 
			thread.interrupt(); // ������ ���� ����
			thread = null;
			gameOn = false;
		}
		
		public void stopSelfAndNewGame() { // �����尡 �ٴڿ� ��Ƽ� ������ �� ȣ��
			startGame();			
		}
		
		public void printResult(String text) {
			resLabel.setText(text);
		}
		public void startGame() {
			randomThread = new RandomThread();
			randomThread.start();
			/*
			 * fallingWord = words.getRandomWord(); int x = ((int)(Math.random() *
			 * this.getWidth())); int y = 5;//((int)(Math.random() * (this.getHeight()/3)));
			 * label.setText(fallingWord); label.setSize(200, 30); label.setLocation(x, y);
			 * label.setForeground(Color.MAGENTA); label.setFont(new Font("Tahoma",
			 * Font.ITALIC, 20));
			 * 
			 * thread = new FallingThread(this, label); thread.start();
			 */
			gameOn = true;
		}
		
		public boolean matchWord(String text) {
			//for(int i=0; i<answerVector.size(); i++) {
				if(answerVector.contains(text)) {
					System.out.println(text+"is in the answerVector");       
					return true;
				}		
			//}
			if(fallingWord != null && fallingWord.equals(text))
				return true;
			else
				return false;
		}
		class RandomThread extends Thread{
			private JGameGroundPanel panel;
			private JLabel randomLabel; //���� ���ڸ� ����ϴ� ���̺�
			//private long delay = 200; // ���� �ð��� �ʱ갪 = 200
			
			//public RandomThread(JGameGroundPanel panel) {
			//	this.panel = panel;
				//this.randomLabel = label;
			//}
			public void run() {
				while(true) {
					try {
						fallingWord = words.getRandomWord();
						answerVector.add(fallingWord);
						System.out.println(answerVector);
						
						int x = ((int)(Math.random() * groundPanel.getWidth()));
						int y = 5;//((int)(Math.random() * (this.getHeight()/3)));
						randomLabel = new JLabel(fallingWord);
						randomLabel.setSize(200, 30);
						randomLabel.setLocation(x, y);
						randomLabel.setForeground(Color.MAGENTA);
						randomLabel.setFont(new Font("Tahoma", Font.ITALIC, 20));
						groundPanel.add(randomLabel);
						//groundPanel.repaint();
						
						
						thread = new FallingThread(groundPanel, randomLabel);
						thread.start();
						
						Thread.sleep(3000);
						
						
						//int x = ((int)(Math.random()*groundPanel.getWidth()));
						//int y = 5;
						//randomLabel = new JLabel();
						//randomLabel.setText(words.getRandomWord());
						//randomLabel.setSize(200,30);
						//randomLabel.setLocation(x,y);
						
						//randomThread = new FallingThread(groundPanel,randomLabel);
						
					}catch(InterruptedException e){
						
					}
				}
			}
		}
		class FallingThread extends Thread {
			private JGameGroundPanel panel;
			private JLabel label; //���� ���ڸ� ����ϴ� ���̺�
			private long delay = 200; // ���� �ð��� �ʱ갪 = 200
			private boolean falling = false; // �������� �ִ���. �ʱ갪 = false
			
			public FallingThread(JGameGroundPanel panel, JLabel label) {
				this.panel = panel;
				this.label = label;
			}
			
			public boolean isFalling() {
				return falling; 
			}	
			
			@Override
			public void run() {
				falling = true;
				while(true) {
					try {
						
						sleep(delay);
						int y = label.getY() + 5; //5�ȼ� �� �Ʒ��� �̵�
						if(y >= panel.getHeight()-label.getHeight()) {
							ScorePanel.correct=-1; //���� ����
							ScorePanel.checkSuccess();
							falling = false;
							label.setText("");
							groundPanel.printResult("�ð��ʰ�����");
							groundPanel.stopSelfAndNewGame();
							break; // ������ ����
						}
						label.setLocation(label.getX(), y);
						TypingGamePanel.this.repaint();
					} catch (InterruptedException e) {
						falling = false;
						return; // ������ ����
					}
				}
			}	
		}
	}

	class JInputPanel extends JPanel {
		
		public JInputPanel() {
			
			setLayout(new FlowLayout());
			JTextField tf = new JTextField(40);
			add(tf);

			tf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField) e.getSource();
					String answer = t.getText();

					if(answer.equals("�׸�"))
						System.exit(0);
					if(!groundPanel.isGameOn())
						return;
					
					boolean match = groundPanel.matchWord(answer);
					
					if(match == true) {
						setBackground(Color.GREEN);
						ScorePanel.correct=1;
						ScorePanel.checkSuccess();
						groundPanel.printResult("����");
						groundPanel.stopGame();
						groundPanel.startGame();
						tf.setText("");
					}
					else {
						setBackground(Color.RED);
						ScorePanel.correct=-1;
						ScorePanel.checkSuccess();
						groundPanel.printResult(tf.getText() + " Ʋ��");
						tf.setText("");
					}
				}
			});
		}
	}
}

//words.txt ������ �а� ���Ϳ� �����ϰ� ���ͷκ��� �����ϰ� �ܾ �����ϴ� Ŭ����
class Words {
	Vector<String> wordVector = new Vector<String>();

	public Words(String fileName) {
		try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
			String line;
			while ((line = br.readLine()) != null) {
				wordVector.add(line);
			}
		} catch (FileNotFoundException e) {
			System.out.println("file not found error");
			System.exit(0);
		} catch (IOException e) {
			System.out.print("input output error");
		}
	}

	public String getRandomWord() {
		final int WORDMAX = wordVector.size(); // �� �ܾ��� ����
		int index = (int) (Math.random() * WORDMAX);
		return wordVector.get(index);
	}
}