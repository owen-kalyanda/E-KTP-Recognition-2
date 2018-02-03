import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JButton;

public class GaussianKernel extends JFrame {
	ArrayList<Double> arrayKernel = new ArrayList<>();
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GaussianKernel frame = new GaussianKernel();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	private void CariGaussianKernel() {
		int y = -1;
		int sigma = 1;
	    for (int i = 0; i <= 2; i++) {
	    	int x = -1;
	      for (int j = 0; j <= 2; j++) {
	    	  double kernel = Math.exp(-0.5*(Math.pow(x, 2)+Math.pow(y, 2)/Math.pow(sigma, 2)))/(2*Math.PI*Math.pow(sigma, 2));
	    	  arrayKernel.add(kernel);
	    	  x++;
	      }
	      y++;
	    }
	    
	    int a = 0;
	    for (int i = 0; i < 3; i++) {
	        for (int j = 0; j <3; j++) {
	            System.out.print(arrayKernel.get(a) + " ");
	            a++;
	        }
	        System.out.println();
	    }
	}
	/**
	 * Create the frame.
	 */
	public GaussianKernel() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnBoom = new JButton("Boom");
		btnBoom.setBounds(106, 74, 89, 23);
		contentPane.add(btnBoom);
		
		CariGaussianKernel();
	}
}
