import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.Color;
import javax.swing.JLabel;
import java.awt.Font;
import java.awt.Frame;

import javax.swing.SwingConstants;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class Home extends JFrame {

	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Home frame = new Home();
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
	public Home() {
		setTitle("Main Menu");
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBackground(Color.ORANGE);
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel lblProgramAutomaticText = new JLabel("Program Automatic Text Recognition");
		lblProgramAutomaticText.setHorizontalAlignment(SwingConstants.CENTER);
		lblProgramAutomaticText.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 50));
		lblProgramAutomaticText.setBounds(180, 100, 1017, 101);
		contentPane.add(lblProgramAutomaticText);
		
		JLabel lblEktp = new JLabel("E-KTP");
		lblEktp.setHorizontalAlignment(SwingConstants.CENTER);
		lblEktp.setFont(new Font("Tahoma", Font.BOLD | Font.ITALIC, 55));
		lblEktp.setBounds(489, 200, 386, 57);
		contentPane.add(lblEktp);
		
		JButton btnPengujian = new JButton("Pengujian");
		btnPengujian.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textRecogEKTPUji uji = new textRecogEKTPUji();
				uji.setVisible(true);
			}
		});
		btnPengujian.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnPengujian.setBounds(567, 327, 218, 74);
		contentPane.add(btnPengujian);
		
		JButton btnPelatihan = new JButton("Pelatihan");
		btnPelatihan.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				textRecogEKTP2 latih = new textRecogEKTP2();
				latih.setVisible(true);
			}
		});
		btnPelatihan.setFont(new Font("Tahoma", Font.BOLD, 20));
		btnPelatihan.setBounds(567, 415, 218, 74);
		contentPane.add(btnPelatihan);
	}

}
