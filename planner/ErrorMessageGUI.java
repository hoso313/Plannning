package planner;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Panel;
import javax.swing.JEditorPane;
import java.awt.Label;
import javax.swing.JButton;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Font;

public class ErrorMessageGUI extends JFrame {

	private JPanel contentPane;
	private Panel panel;
	private JEditorPane errorMessageText;
	private Label label;
	private JButton btnNewButton;

	/**
	 * Launch the application.
	 */
	public static void errorMessageGUI(String error) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ErrorMessageGUI frame = new ErrorMessageGUI(error);
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
	public ErrorMessageGUI(String error) {
		setTitle("Error Message");
		setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
		setBounds(StateEditGUI.frame.getX()+20, StateEditGUI.frame.getY()+20, 360, 173);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		panel = new Panel();
		panel.setBackground(Color.WHITE);
		panel.setBounds(0, 0, 344, 102);
		contentPane.add(panel);
		panel.setLayout(null);

		errorMessageText = new JEditorPane();
		errorMessageText.setFont(new Font("メイリオ", Font.PLAIN, 14));
		errorMessageText.setForeground(Color.BLACK);
		errorMessageText.setText(error);
		errorMessageText.setBackground(Color.WHITE);
		errorMessageText.setEditable(false);
		errorMessageText.setBounds(20, 29, 300, 73);
		panel.add(errorMessageText);

		label = new Label("#error");
		label.setBounds(10, 0, 39, 23);
		panel.add(label);

		btnNewButton = new JButton("OK");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setVisible(false);
				StateEditGUI.frame.setEnabled(true);
			}
		});
		btnNewButton.setBounds(241, 108, 91, 21);
		contentPane.add(btnNewButton);
	}
}
