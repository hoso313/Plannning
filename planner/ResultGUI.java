package planner;

import java.util.*;
import java.awt.BorderLayout;
import java.awt.EventQueue;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Panel;
import java.awt.TextArea;
import javax.swing.JEditorPane;
import javax.swing.JTextArea;
import java.awt.Button;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollPane;

public class ResultGUI extends JFrame {

	private JPanel contentPane;
	private JPanel panel;
	private Button button;
	private JScrollPane scrollPane;
	private JTextArea resultArea;
	private Button button_1;

	/**
	 * Launch the application.
	 */
	public static void resultGUI(Planner planner) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ResultGUI frame = new ResultGUI(planner);
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public ResultGUI(Planner planner) {
		setTitle("結果");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(GUI.frame.getX()+440, GUI.frame.getY(), 360, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new JPanel();
		panel.setBounds(12, 227, 320, 24);
		contentPane.add(panel);
		panel.setLayout(null);

		button = new Button("戻る");
		button.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				GUI.enabledControl(true);
				GUI.setOnTop();
			}
		});
		button.setBounds(244, 0, 76, 23);
		panel.add(button);

		button_1 = new Button("見る");
		button_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				ImageResultGUI.getResultMovie(new AttributeEditer().getBlockAttribute());
			}
		});
		button_1.setBounds(162, 0, 76, 23);
		panel.add(button_1);

		scrollPane = new JScrollPane();
		scrollPane.setBounds(12, 10, 320, 216);
		contentPane.add(scrollPane);

		resultArea = new JTextArea();
		resultArea.setText("******** This is a plan! ********\r\n");
		scrollPane.setViewportView(resultArea);
		for(String str : planner.result) {
			resultArea.append(str+"\n");
		}
	}
}
