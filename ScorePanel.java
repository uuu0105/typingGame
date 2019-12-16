
import java.awt.Color;
import javax.swing.*;
import java.awt.Font;
class TimerRunnable implements Runnable {
	private TypingGamePanel gamePanel = new TypingGamePanel();
	TypingGamePanel.JGameGroundPanel ground = gamePanel.new JGameGroundPanel();
	public static boolean isFinished = false;
	public static int remainedTime = 0;
	public static int level = 0;
	public static int life = 0;
	private JLabel timerLabel;
	private JLabel levelLabel;
	private JLabel lifeLabel;
	
	public TimerRunnable(JLabel timerLabel, JLabel levelLabel, JLabel lifeLabel) {
		this.timerLabel = timerLabel;
		this.levelLabel = levelLabel;
		this.lifeLabel = lifeLabel;
	}
	@Override
	public void run() {
		remainedTime = 150;
		level = 1;
		life = 10;
		while(remainedTime >= 0 && !Thread.interrupted()) {
			timerLabel.setText(Integer.toString(remainedTime));
			levelLabel.setText(Integer.toString(level));
			lifeLabel.setText(Integer.toString(life));
			remainedTime--;
			try {
				Thread.sleep(1000);
			} catch(InterruptedException e) {
				return;
			}
		}
		isFinished=true;
	}
}
public class ScorePanel extends JPanel {
	static int correct=0;
	static int score=0;
	private JLabel textLabel = new JLabel("����");
	static JLabel scoreLabel=new JLabel("0");
	
	private Thread th;
	private JLabel timeInfoLabel = new JLabel("Remaining Time");
	private JLabel timerLabel = new JLabel();
	private JLabel levelInfoLabel = new JLabel("LEVEL ");
	private JLabel levelLabel = new JLabel();
	private JLabel lifeInfoLabel = new JLabel("Life ");
	private JLabel lifeLabel = new JLabel();
	
	static public void checkSuccess() { //������ �� ���� �ٲ��
		if(ScorePanel.correct==1) {
			ScorePanel.score+=20;
			scoreLabel.setText(Integer.toString(ScorePanel.score));
			//System.out.println(ScorePanel.score);
		}
		else if(ScorePanel.correct ==-1) {
			if(ScorePanel.score<10)
				ScorePanel.score=0;
			else
				ScorePanel.score-=10;
			scoreLabel.setText(Integer.toString(ScorePanel.score));
			//System.out.println(ScorePanel.score);
		}
		else { //sucess �� ����Ʈ�� 0�� ��. �� �ƹ��ϵ� ���� ��
			return;
		}
		ScorePanel.correct=0;
	}

	public ScorePanel() {
		setBackground(Color.PINK);	// ����� �����
		setLayout(null);	// �����ڰ� ���̾ƿ� ����
		textLabel.setSize(150, 70);	// �ؽ�Ʈ ������ 50x30 ����
		textLabel.setFont(new Font("Gothic", Font.PLAIN, 30));
		textLabel.setLocation(10, 10);	// �ؽ�Ʈ ��ġ 10x10 ����
		add(textLabel);	// �ؽ�Ʈ�� panel�� �߰�
		
		scoreLabel.setSize(100, 30);
		scoreLabel.setFont(new Font("Gothic", Font.ITALIC, 30));
		scoreLabel.setLocation(100, 10);
		add(scoreLabel);
		checkSuccess();
		
		timeInfoLabel.setSize(200, 100);
		timeInfoLabel.setFont(new Font("Gothic", Font.ITALIC, 20));
		timeInfoLabel.setLocation(10, 50);
		add(timeInfoLabel);
		
		timerLabel.setSize(100, 100);
		timerLabel.setFont(new Font("Gothic", Font.ITALIC, 50));
		timerLabel.setLocation(170, 50);
		
		levelInfoLabel.setSize(100, 70);
		levelInfoLabel.setFont(new Font("Gothic", Font.ITALIC, 20));
		levelInfoLabel.setLocation(10, 120);
		add(levelInfoLabel);
		
		levelLabel.setSize(100, 50);
		levelLabel.setFont(new Font("Gothic", Font.ITALIC, 30));
		levelLabel.setLocation(100, 120);
		add(levelLabel);
		
		lifeInfoLabel.setSize(100, 50);;
		lifeInfoLabel.setFont(new Font("Gothic", Font.ITALIC, 30));
		lifeInfoLabel.setLocation(10, 200);
		add(lifeInfoLabel);
		
		lifeLabel.setSize(100, 30);
		lifeLabel.setFont(new Font("Gothic", Font.PLAIN, 30));
		lifeLabel.setLocation(100, 200);
		add(lifeLabel);
		
		TimerRunnable runnable = new TimerRunnable(timerLabel, levelLabel, lifeLabel);
		th = new Thread(runnable);
		add(timerLabel);
		th.start();
	}
}
