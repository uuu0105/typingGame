import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.io.*;
import java.util.ArrayList;
import java.util.Vector;

public class TypingGamePanel extends JPanel{
	private JGameGroundPanel groundPanel = new JGameGroundPanel(); //�ܾ� �������� ��ũ��
	private JInputPanel inputPanel = new JInputPanel(); //�Է�â�� �Է��ϴ� ��ũ��
	//private String newWord;
	//private Words words = new Words("words.txt");
	
	private String fallingWord = null;
	private ArrayList<String> answerVector = new ArrayList<String>();
	
	public TypingGamePanel() { //setLayout
		setLayout(new BorderLayout());
		add(groundPanel, BorderLayout.CENTER);
		add(inputPanel, BorderLayout.SOUTH);
	}

	class JGameGroundPanel extends JPanel { //�ܾ� �������� ��ũ��.
		
		private JLabel label = new JLabel(); //�ܾ�
		private JLabel resLabel = new JLabel(); //�ܾ� oX �Ǵܷ��̺�
		private Words words = null;
		
		private FallingThread thread = null; //���̺� ������
		private RandomThread randomThread = null;
		private boolean gameOn = false;
		
		
		public JGameGroundPanel() {
			setBackground(Color.PINK);
			setLayout(null);
			add(label);
			
			resLabel.setLocation(0, 0);
			resLabel.setSize(100, 30);
			add(resLabel);
			words = new Words("words.txt");

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
			
			gameOn = true;
		}
		
		public boolean matchWord(String text) {
			
			System.out.println(answerVector);
			for(int i=0; i<answerVector.size(); i++) {
				if(answerVector.get(i).equals(text)) { 
					System.out.println("text is answer");
					return true; 
				} 
			}
	
			return false;

			
		}
		class RandomThread extends Thread{

			private JLabel newLabel; //���� ���ڸ� ����ϴ� ���̺�

			public void run() {
				while(true) {
					try {
						fallingWord = words.getRandomWord();
						answerVector.add(fallingWord);
						
						int x = ((int)(Math.random() * groundPanel.getWidth()));
						int y = 5;
						newLabel = new JLabel(fallingWord);
						newLabel.setSize(200, 30);
						newLabel.setLocation(x, y);
						newLabel.setForeground(Color.MAGENTA);
						newLabel.setFont(new Font("Tahoma", Font.ITALIC, 20));
						groundPanel.add(newLabel);
						//groundPanel.repaint();
						
						
						thread = new FallingThread(groundPanel, newLabel);
						thread.start();
						
						Thread.sleep(15000);
						
						
					}catch(InterruptedException e){
						
					}
				}
			}
		}
		class FallingThread extends Thread {
			private JGameGroundPanel panel;
			private JLabel label; //���� ���ڸ� ����ϴ� ���̺�
			private long delay = 1000; // ���� �ð��� �ʱ갪 = 200
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
						if(y >= panel.getHeight()-label.getHeight()) {//�ٴڿ� ����
							ScorePanel.correct=-1; //���� ����
							ScorePanel.checkSuccess();
							falling = false;
							label.setText("");
							groundPanel.printResult("�ð��ʰ�����");
							//groundPanel.stopSelfAndNewGame();
							this.interrupt();//return;//break; // ������ ����
						}
						else {
							label.setLocation(label.getX(), y);
							TypingGamePanel.this.repaint();
						}
					
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
			JTextField tf = new JTextField(40); //�Է�â
			add(tf);

			tf.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					JTextField t = (JTextField) e.getSource();
					String answer = t.getText();

					if(answer.equals("�׸�"))
						System.exit(0);
					//if(!groundPanel.isGameOn())
					//	return;
					
					boolean match = groundPanel.matchWord(answer);
					
					if(match == true) {
						System.out.println(answer + "is in the answerVector"); 
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