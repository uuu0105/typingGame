
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

class EditPanel extends JPanel {
	//private JInputPanel inputPanel;// = new JInputPanel();
	private JTextField textField=new JTextField(20);
	
	public EditPanel() {
		setBackground(Color.PINK);
		setLayout(new FlowLayout());
		add(textField);
		JButton btn=new JButton("add");
		btn.addActionListener(new MyActionListener());
		add(btn);
		add(new JButton("save"));
	}
	public void append(String newWord){
	    try
	    {
	        String path = "words.txt";
	        File file = new File(path);
	        FileWriter fileWriter = new FileWriter(file,true);
	        BufferedWriter bufferFileWriter  = new BufferedWriter(fileWriter);
	        fileWriter.append(newWord);
	        bufferFileWriter.close();
	        System.out.println("User Registration Completed");
	    }catch(Exception ex)
	    {
	        System.out.println(ex);
	    }
	}
	class MyActionListener implements ActionListener{
		public void actionPerformed(ActionEvent e) {
			JButton b = (JButton)e.getSource();
			if(b.getText().equals("add")) {
				String newWord = textField.getText();
				append(newWord);
				textField.setText("");
			}
		}
	}

}
