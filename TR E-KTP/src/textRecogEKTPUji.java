import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Rectangle;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Frame;
import java.awt.Font;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.UIManager;

public class textRecogEKTPUji extends JFrame {
	File file = null;
	BufferedImage newImg;
	BufferedImage newCroppedImg;
	static BufferedImage newBWImg;
	BufferedImage newGreyscalledImg;
	int panjangBaris;
	int batasAtas;
	int batasBawah;
	ArrayList<Integer> arrayPixelHorizontal = new ArrayList<>();
	ArrayList<Integer> arrayPixelVertikal = new ArrayList<>();
	ArrayList<Integer> arrayPixelVertikalPutih = new ArrayList<>();
	PrintStream out;
	private JPanel contentPane;
	private JTextField tfNilaiThreshold;
	private JTextField tfMCX;
	private JTextField tfMCY;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					textRecogEKTP2 frame = new textRecogEKTP2();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public void printPixelARGB(int pixel) {
	    int red = (pixel >> 16) & 0xff;
	    int green = (pixel >> 8) & 0xff;
	    int blue = (pixel) & 0xff;
	    System.out.println("rgb: " + red + ", " + green + ", " + blue);
	  }
	
	public int getRed(int pixel){
		int red = (pixel >> 16) & 0xff;
		return red;
	}

	public int getGreen(int pixel){
		int green = (pixel >> 8) & 0xff;
		return green;
	}
	
	public int getBlue(int pixel){
		int blue = (pixel) & 0xff;
		return blue;
	}
	
	private void cariPixelHorizontal(BufferedImage newImg) {
	    int w = newImg.getWidth();
	    int h = newImg.getHeight();
	    arrayPixelHorizontal.clear();
	    System.out.println("width, height: " + w + ", " + h);

	    for (int i = 0; i < h; i++) {
	    	int jmlPixelHitam = 0;
	      for (int j = 0; j < w; j++) {
	        int pixel = newImg.getRGB(j, i);
	        int red = getRed(pixel);
	        int green = getGreen(pixel);
	        int blue = getBlue(pixel);
	        if(red == 0 && green == 0 && blue == 0){
	        	jmlPixelHitam++;
	        }

	      }
	      arrayPixelHorizontal.add(jmlPixelHitam);
	      System.out.println("Baris ke " + i + " terdapat " + arrayPixelHorizontal.get(i) + " pixel hitam");
	    }
	}
	
	private void cariPixelHorizontal2(BufferedImage newImg) {
	    int w = newImg.getWidth();
	    int h = newImg.getHeight();
	    arrayPixelHorizontal.clear();
	    System.out.println("width, height: " + w + ", " + h);

	    for (int i = 0; i < h; i++) {
	    	int jmlPixelHitam = 0;
	      for (int j = 0; j < w; j++) {
	        int pixel = newImg.getRGB(j, i);
	        int red = getRed(pixel);
	        int green = getGreen(pixel);
	        int blue = getBlue(pixel);
	        if(red == 0 && green == 0 && blue == 255){
	        	jmlPixelHitam++;
	        }

	      }
	      arrayPixelHorizontal.add(jmlPixelHitam);
	      System.out.println("Baris ke " + i + " terdapat " + arrayPixelHorizontal.get(i) + " pixel hitam");
	    }
	}
	
	private void cariPixelVertikal(BufferedImage newImg) {
	    int w = newImg.getWidth();
	    int h = newImg.getHeight();
	    System.out.println("width, height: " + w + ", " + h);
	    arrayPixelVertikal.clear();
	    arrayPixelVertikalPutih.clear();
	    for (int i = 0; i < w; i++) {
	    	int jmlPixelHitam = 0;
	    	int jmlPixelPutih = 0;
	      for (int j = batasAtas + 1; j <= batasAtas+panjangBaris-1; j++) {
	        int pixel = newImg.getRGB(i, j);
	        int red = getRed(pixel);
	        int green = getGreen(pixel);
	        int blue = getBlue(pixel);
	        if(red == 0 && green == 0 && blue == 0){
	        	jmlPixelHitam++;
	        }
	        else {
	        	jmlPixelPutih++;
	        }
	      }
	      arrayPixelVertikal.add(jmlPixelHitam);
	      arrayPixelVertikalPutih.add(jmlPixelPutih);
	      System.out.println("Kolom ke " + i + " terdapat " + arrayPixelVertikal.get(i) + " pixel hitam");
	    }
	}
	
	private void drawLineHorizontal(BufferedImage newBWImg, JLabel lblCroppedKTP) {
		int w = newBWImg.getWidth();
		int h = newBWImg.getHeight();
		for(int i = 0; i < h; i++){
			if(arrayPixelHorizontal.get(i) != 0 && arrayPixelHorizontal.get(i+1) != 0 && arrayPixelHorizontal.get(i-1) == 0){
					Graphics g2 = newBWImg.getGraphics();
			        g2.setColor(Color.BLUE);
			        g2.drawLine(0, i-1, w, i-1);
				}
			if(i <= w-4 && arrayPixelHorizontal.get(i) != 0 && arrayPixelHorizontal.get(i+1) == 0 && arrayPixelHorizontal.get(i+2) == 0 && arrayPixelHorizontal.get(i+3) == 0 &&arrayPixelHorizontal.get(i-1) != 0){
				Graphics g2 = newBWImg.getGraphics();
		        g2.setColor(Color.BLUE);
		        g2.drawLine(0, i+1, w, i+1);
			}
			if(i <= w-3 && arrayPixelHorizontal.get(i) != 0 && arrayPixelHorizontal.get(i+1) == 0 && arrayPixelHorizontal.get(i+2) == 0 && arrayPixelHorizontal.get(i-1) != 0){
				Graphics g2 = newBWImg.getGraphics();
		        g2.setColor(Color.BLUE);
		        g2.drawLine(0, i+1, w, i+1);
			}
			}
		lblCroppedKTP.setIcon(new ImageIcon(newBWImg));
		arrayPixelHorizontal.clear();
		}
	
	private void drawLineVertikal(BufferedImage newBWImg, JLabel lblCroppedKTP) {
		int w = newBWImg.getWidth();
		int h = newBWImg.getHeight();
		int lblW = lblCroppedKTP.getWidth();
		int ketemuGaris = 0;
		batasAtas = 0;
		int batasBawah;
		int banyakBarisPutih = 0;
		int barisKe = 0;
		cariPixelHorizontal2(newBWImg);
		for(int i = 0; i < h; i++){
			if(arrayPixelHorizontal.get(i) == lblW){
				ketemuGaris++;
				System.out.println("Baris ke " + i);
				System.out.println("Ketemu garis " + ketemuGaris);
				if(ketemuGaris == 1){
					batasAtas = i;
				}
			}
			if(ketemuGaris == 2){
				barisKe++;
				banyakBarisPutih = 0;
				batasBawah = i;
				panjangBaris = batasBawah - batasAtas;
				System.out.println("Batas atas " + batasAtas + " Batas bawah " + batasBawah + " Panjang baris " + panjangBaris);
				cariPixelVertikal(newBWImg);
				for(int j = 0; j < w; j++){
					if(j < w-1 && arrayPixelVertikalPutih.get(j) == panjangBaris-1 && arrayPixelVertikal.get(j+1) == 0){
						banyakBarisPutih++;
						if(barisKe == 1){
							if(banyakBarisPutih == 15){
								Graphics g2 = newBWImg.getGraphics();
						        g2.setColor(Color.BLUE);
						        g2.drawLine(j, batasAtas+1, j, batasBawah);
						        g2.setColor(Color.BLUE);
						        g2.drawLine(j-13, batasAtas+1, j-13, batasBawah);
						        banyakBarisPutih = 0;
							}
						}
						else{
							if(banyakBarisPutih == 5){
								if(j != 4 && arrayPixelVertikal.get(j-5) != 0){
									Graphics g2 = newBWImg.getGraphics();
							        g2.setColor(Color.BLUE);
							        g2.drawLine(j, batasAtas+1, j, batasBawah);
							        g2.setColor(Color.BLUE);
							        g2.drawLine(j-3, batasAtas+1, j-3, batasBawah);
							        banyakBarisPutih = 0;
								}
								else{
									Graphics g2 = newBWImg.getGraphics();
							        g2.setColor(Color.BLUE);
							        g2.drawLine(j, batasAtas+1, j, batasBawah);
							        g2.setColor(Color.BLUE);
							        g2.drawLine(j-4, batasAtas+1, j-4, batasBawah);
							        banyakBarisPutih = 0;
								}
							}
						}
					}
					if(arrayPixelVertikal.get(j) != 0 && arrayPixelVertikal.get(j-1) == 0){
						Graphics g2 = newBWImg.getGraphics();
				        g2.setColor(Color.BLUE);
				        g2.drawLine(j-1, batasAtas+1, j-1, batasBawah);
				        banyakBarisPutih = 0;
					}
					if(arrayPixelVertikal.get(j) != 0 && arrayPixelVertikal.get(j+1) == 0){
						Graphics g2 = newBWImg.getGraphics();
				        g2.setColor(Color.BLUE);
				        g2.drawLine(j+1, batasAtas+1, j+1, batasBawah);
					}
				}
				ketemuGaris = 0;
			}
			}
		lblCroppedKTP.setIcon(new ImageIcon(newBWImg));
		}
	
	public BufferedImage getNewImg() {
        return newImg;
    }
	
	/**
	 * Create the frame.
	 */
	public textRecogEKTPUji() {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1386, 799);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnOpenFile.setBounds(1130, 65, 158, 40);
		contentPane.add(btnOpenFile);
		
		JLabel lblFirstImage = new JLabel("");
		lblFirstImage.setBounds(160, 25, 817, 391);
		contentPane.add(lblFirstImage);
		
		JLabel lblCroppedKTP = new JLabel("");
		lblCroppedKTP.setBounds(160, 422, 500, 270);
		contentPane.add(lblCroppedKTP);
		
		JButton btnManualCrop = new JButton("Manual Crop");
		btnManualCrop.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnManualCrop.setBounds(1130, 230, 158, 40);
		contentPane.add(btnManualCrop);
		
		JButton btnThresholding2 = new JButton("Thresholding");
		btnThresholding2.setFont(new Font("Tahoma", Font.BOLD, 14));
		btnThresholding2.setBounds(1130, 383, 158, 40);
		contentPane.add(btnThresholding2);
		
		JButton btnProses = new JButton("Proses");
		btnProses.setFont(new Font("Tahoma", Font.BOLD, 14));
		
		btnProses.setBounds(1130, 434, 158, 40);
		contentPane.add(btnProses);
		
		tfNilaiThreshold = new JTextField();
		tfNilaiThreshold.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tfNilaiThreshold.setText("100");
		tfNilaiThreshold.setBounds(1130, 332, 70, 30);
		contentPane.add(tfNilaiThreshold);
		tfNilaiThreshold.setColumns(10);
		
		JLabel lblNilaiThreshold = new JLabel("Nilai Threshold");
		lblNilaiThreshold.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblNilaiThreshold.setBounds(1130, 291, 158, 30);
		contentPane.add(lblNilaiThreshold);
		
		tfMCX = new JTextField();
		tfMCX.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tfMCX.setText("25");
		tfMCX.setBounds(1174, 152, 70, 30);
		contentPane.add(tfMCX);
		tfMCX.setColumns(10);
		
		tfMCY = new JTextField();
		tfMCY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tfMCY.setText("55");
		tfMCY.setColumns(10);
		tfMCY.setBounds(1174, 189, 70, 30);
		contentPane.add(tfMCY);
		
		JLabel lblX = new JLabel("x :");
		lblX.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblX.setBounds(1140, 152, 48, 27);
		contentPane.add(lblX);
		
		JLabel lblY = new JLabel("y :");
		lblY.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblY.setBounds(1140, 191, 38, 23);
		contentPane.add(lblY);
		
		JLabel lblPengaturanManualCropping = new JLabel("Pengaturan Manual Cropping");
		lblPengaturanManualCropping.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblPengaturanManualCropping.setBounds(1130, 116, 201, 25);
		contentPane.add(lblPengaturanManualCropping);
		
		JPanel panel = new JPanel();
		panel.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "", TitledBorder.LEADING, TitledBorder.TOP, null, Color.GRAY));
		panel.setBounds(1086, 36, 284, 488);
		contentPane.add(panel);
		
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				JFileChooser jfc = new JFileChooser(); 
				if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
					file = jfc.getSelectedFile() ; 
		        }
				try {
					int lblW = lblFirstImage.getWidth();
					int lblH = lblFirstImage.getHeight();
					BufferedImage img = ImageIO.read(file);
					newImg = new BufferedImage(lblW, lblH, BufferedImage.TYPE_INT_RGB);
					Graphics g = newImg.createGraphics();
					g.drawImage(img, 0, 0, lblW, lblH, null);
					g.dispose();
					lblFirstImage.setIcon(new ImageIcon(newImg));
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		btnManualCrop.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int lblW = lblCroppedKTP.getWidth();
				int lblH = lblCroppedKTP.getHeight();
				int x = Integer.parseInt(tfMCX.getText());
				int y = Integer.parseInt(tfMCY.getText());
				BufferedImage croppedImage = newImg.getSubimage(x, y, 520, 300);
				newCroppedImg = new BufferedImage(lblW, lblH, BufferedImage.TYPE_INT_RGB);
				Graphics g = newCroppedImg.createGraphics();
				g.drawImage(croppedImage, 0, 0, lblW, lblH, null);
				g.dispose();
				lblCroppedKTP.setIcon(new ImageIcon(newCroppedImg));
				
			}
		});
		
		
		
		btnThresholding2.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int lblW = lblCroppedKTP.getWidth();
				int lblH = lblCroppedKTP.getHeight();
				  BufferedImage greyscalledImg = newCroppedImg;
				  BufferedImage newGreyscalledImg = new BufferedImage(lblW, lblH, BufferedImage.TYPE_4BYTE_ABGR);
		    	  Graphics g = newGreyscalledImg.createGraphics();
		    	  g.drawImage(greyscalledImg, 0, 0, lblW, lblH, null);
		    	  g.dispose();
		    	  lblCroppedKTP.setIcon(new ImageIcon(newGreyscalledImg));
		    	  
				  int w = newGreyscalledImg.getWidth();
				  int h = newGreyscalledImg.getHeight();
				  int nilaiThreshold = Integer.parseInt(tfNilaiThreshold.getText());
				  for (int i = 0; i < h; i++) {
				      for (int j = 0; j < w; j++) {
						  Color c = new Color(newGreyscalledImg.getRGB(j, i));
					      if (c.getRed() < nilaiThreshold && c.getGreen() < nilaiThreshold && c.getBlue() < nilaiThreshold) {
					    	  newGreyscalledImg.setRGB(j, i, Color.black.getRGB());
					      }
					      else
					      {
					    	  newGreyscalledImg.setRGB(j, i, Color.white.getRGB());
					      }
				      }
				  }
				  newBWImg = new BufferedImage(lblW, lblH, BufferedImage.TYPE_INT_ARGB);
		    	  Graphics g1 = newBWImg.createGraphics();
		    	  g1.drawImage(newGreyscalledImg, 0, 0, lblW, lblH, null);
		    	  g1.dispose();
		    	  lblCroppedKTP.setIcon(new ImageIcon(newBWImg));
		    	  
		    	  //marchThroughImage(newBWImg);
			}
		});
		
		btnProses.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cariPixelHorizontal(newBWImg);
				drawLineHorizontal(newBWImg, lblCroppedKTP);
				drawLineVertikal(newBWImg, lblCroppedKTP);
				formProses fp;
				try {
					fp = new formProses();
					fp.lblAutocrop.setIcon(lblCroppedKTP.getIcon());
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}			
			}
		});
	}
}
