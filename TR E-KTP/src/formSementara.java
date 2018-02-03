import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.border.LineBorder;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import java.awt.Font;

public class formSementara extends JFrame {
	
	
	int batasAtas;
	int batasKiri;
	static int bariske = 0;
	JLabel lblAutocrop = new JLabel();
	JLabel [] labelcropV = new  JLabel[1000] ;
	static JLabel [] labelcrop = new  JLabel[14] ;
	JTextField [] tfInputChar = new JTextField[1000];
	int[] arrayPixelHorizontal = new int[400];
	static int[] arrayPixelVertikal = new int[500];
	int[] arrayPixelLeftDiagonal = new int[50];
    int jmlPixelHitam = 0;
    int z = 0;
    ArrayList<Double> stdHori=new ArrayList<Double>();
    ArrayList<Double> meanHori=new ArrayList<Double>();
    ArrayList<Double> stdVert=new ArrayList<Double>();
    ArrayList<Double> meanVert=new ArrayList<Double>();
    ArrayList<Double> stdDiag=new ArrayList<Double>();
    ArrayList<Double> meanDiag=new ArrayList<Double>();
    ArrayList<String> hasilChar=new ArrayList<String>(); 
    
	
	/*public formSementara(BufferedImage para){
		BufferedImage autocroppedImg = para;
		BufferedImage newAutocroppedImg = new BufferedImage(315, 215, BufferedImage.TYPE_INT_ARGB);
		Graphics g1 = newAutocroppedImg.createGraphics();
		g1.drawImage(autocroppedImg, 0, 0, 315, 215, null);
		g1.dispose();
		lblAutocrop.setIcon(new ImageIcon(newAutocroppedImg));
	}*/
	
	private JPanel contentPane;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					formSementara frame = new formSementara();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static int getRed(int pixel){
		int red = (pixel >> 16) & 0xff;
		return red;
	}

	public static int getGreen(int pixel){
		int green = (pixel >> 8) & 0xff;
		return green;
	}
	
	public static int getBlue(int pixel){
		int blue = (pixel) & 0xff;
		return blue;
	}
	
	private void cariPixelHorizontal(BufferedImage newImg) {
	    int w = newImg.getWidth();
	    int h = newImg.getHeight();
	    System.out.println("Cari pixel secara horizontal");
	    System.out.println("width, height: " + w + ", " + h);

	    for (int i = 0; i < h; i++) {
	    	int jmlPixelBiru = 0;
	      for (int j = 0; j < w; j++) {
	        int pixel = newImg.getRGB(j, i);
	        int red = getRed(pixel);
	        int green = getGreen(pixel);
	        int blue = getBlue(pixel);
	        if(red == 0 && green == 0 && blue == 255){
	        	jmlPixelBiru++;
	        }

	      }
	      arrayPixelHorizontal[i] = jmlPixelBiru;
	      System.out.println("Baris ke " + i + " terdapat " + arrayPixelHorizontal[i] + " pixel biru");
	    }
	}
	
	public static void cariPixelVertikal(BufferedImage newImg) {
	    int w = newImg.getWidth();
	    int h = newImg.getHeight();
	    System.out.println("Cari pixel secara vertikal");
	    System.out.println("width, height: " + w + ", " + h);

	    for (int i = 0; i < w; i++) {
	    	int jmlPixelBiru = 0;
	      for (int j = 0; j < h; j++) {
	        int pixel = newImg.getRGB(i, j);
	        int red = getRed(pixel);
	        int green = getGreen(pixel);
	        int blue = getBlue(pixel);
	        if(red == 0 && green == 0 && blue == 255){
	        	jmlPixelBiru++;
	        }
	      }
	      arrayPixelVertikal[i] = jmlPixelBiru;
	      System.out.println("Kolom ke " + i + " terdapat " + arrayPixelVertikal[i] + " pixel Biru");
	    }
	}
	
	private void croppingHorizontal(BufferedImage newBWImg, JLabel [] labelcrop) {
		int w = newBWImg.getWidth();
		int h = newBWImg.getHeight();
		int lblW = lblAutocrop.getWidth();
		int ketemuGaris = 0;
		batasAtas = 0;
		int batasBawah;
		cariPixelHorizontal(newBWImg);
		for(int i = 0; i < h; i++){
			if(arrayPixelHorizontal[i] == lblW){
				ketemuGaris++;
				System.out.println("Baris ke " + i);
				System.out.println("Ketemu garis " + ketemuGaris);
				if(ketemuGaris == 1){
					batasAtas = i;
				}
			}
			if(ketemuGaris == 2){
				batasBawah = i;
				int panjangBaris = batasBawah - batasAtas;
				int lblCW = labelcrop[bariske].getWidth();
				int lblCH = labelcrop[bariske].getHeight();
				BufferedImage croppedImage = newBWImg.getSubimage(0, batasAtas + 1, w, panjangBaris - 1);
				BufferedImage newCroppedImg = new BufferedImage(lblCW, lblCH, BufferedImage.TYPE_INT_RGB);
				Graphics g = newCroppedImg.createGraphics();
				g.drawImage(croppedImage, 0, 0, lblCW, lblCH, null);
				g.dispose();
				labelcrop[bariske].setIcon(new ImageIcon(newCroppedImg));
				ketemuGaris = 0;
				bariske++;
			}
			}
		}

	/**
	 * Create the frame.
	 */
	public formSementara() {
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1173, 520);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lblAutocrop.setBorder(new LineBorder(new Color(0, 0, 0)));

		lblAutocrop.setBounds(46, 39, 500, 280);
		contentPane.add(lblAutocrop);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(10, 311, 500, 18);
		contentPane.add(lblNewLabel);
		
		int sum=11;
		for (int i = 0; i < 14; i++) {
			labelcrop[i] = new JLabel();
			labelcrop[i].setBounds(600 , sum ,500, 18);
			Border border = BorderFactory.createLineBorder(Color.GREEN);
			labelcrop[i].setBorder(border);
			sum+=30;
			contentPane.add(labelcrop[i]);
		}
		
		JButton btnNext = new JButton("Next");
		btnNext.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnNext.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				FormLatih fl = new FormLatih();
				fl.setVisible(true);
			}
		});
		btnNext.setBounds(0, 416, 1157, 66);
		contentPane.add(btnNext);
		
		croppingHorizontal(textRecogEKTP2.newBWImg, labelcrop);

	}
}
