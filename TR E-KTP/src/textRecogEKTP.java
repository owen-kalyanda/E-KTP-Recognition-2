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
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.Frame;

public class textRecogEKTP extends JFrame {
	File file = null;
	BufferedImage newImg;
	BufferedImage newCroppedImg;
	static BufferedImage newBWImg;
	BufferedImage newGreyscalledImg;
	int panjangBaris;
	int batasAtas;
	int batasBawah;
	int[] arrayPixelHorizontal = new int[500];
	int[] arrayPixelVertikal = new int[501];
	int[] arrayPixelVertikalPutih = new int[501];
	PrintStream out;
	private JPanel contentPane;
	private JTextField tfNilaiThreshold;
	private JTextField tfMCX;
	private JTextField tfMCY;




	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					textRecogEKTP frame = new textRecogEKTP();
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
	
	private void marchThroughImage(BufferedImage newImg) {
	    int w = newImg.getWidth();
	    int h = newImg.getHeight();
	    System.out.println("width, height: " + w + ", " + h);

	    for (int i = 0; i < h; i++) {
	      for (int j = 0; j < w; j++) {
	        System.out.println("x,y: " + j + ", " + i);
	        int pixel = newImg.getRGB(j, i);
	        printPixelARGB(pixel);
	        System.out.println("");
	        
	      }
	    }
	}
	
	private void cariPixelHorizontal(BufferedImage newImg) {
	    int w = newImg.getWidth();
	    int h = newImg.getHeight();
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
	      arrayPixelHorizontal[i] = jmlPixelHitam;
	      System.out.println("Baris ke " + i + " terdapat " + arrayPixelHorizontal[i] + " pixel hitam");
	    }
	}
	
	private void cariPixelHorizontal2(BufferedImage newImg) {
	    int w = newImg.getWidth();
	    int h = newImg.getHeight();
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
	      arrayPixelHorizontal[i] = jmlPixelHitam;
	      System.out.println("Baris ke " + i + " terdapat " + arrayPixelHorizontal[i] + " pixel biru");
	    }
	}
	
	private void cariPixelVertikal(BufferedImage newImg) {
	    int w = newImg.getWidth();
	    int h = newImg.getHeight();
	    System.out.println("width, height: " + w + ", " + h);

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
	      arrayPixelVertikal[i] = jmlPixelHitam;
	      arrayPixelVertikalPutih[i] = jmlPixelPutih;
	      System.out.println("Kolom ke " + i + " terdapat " + arrayPixelVertikal[i] + " pixel hitam " +  arrayPixelVertikalPutih[i] + " pixel putih");
	    }
	}
	
	private void drawLineHorizontal(BufferedImage newBWImg, JLabel lblCroppedKTP) {
		int w = newBWImg.getWidth();
		int h = newBWImg.getHeight();
		for(int i = 0; i < h; i++){
			if(arrayPixelHorizontal[i] != 0 && arrayPixelHorizontal[i+1] != 0 && arrayPixelHorizontal[i-1] == 0){
					Graphics g2 = newBWImg.getGraphics();
			        g2.setColor(Color.BLUE);
			        g2.drawLine(0, i-1, w, i-1);
				}
			if(i <= 346 && arrayPixelHorizontal[i] != 0 && arrayPixelHorizontal[i+1] == 0 && arrayPixelHorizontal[i+2] == 0 && arrayPixelHorizontal[i+3] == 0 && arrayPixelHorizontal[i-1] != 0){
				Graphics g2 = newBWImg.getGraphics();
		        g2.setColor(Color.BLUE);
		        g2.drawLine(0, i+1, w, i+1);
			}
			if(i <= 347 && arrayPixelHorizontal[i] != 0 && arrayPixelHorizontal[i+1] == 0 && arrayPixelHorizontal[i+2] == 0 && arrayPixelHorizontal[i-1] != 0){
				Graphics g2 = newBWImg.getGraphics();
		        g2.setColor(Color.BLUE);
		        g2.drawLine(0, i+1, w, i+1);
			}
			}
		lblCroppedKTP.setIcon(new ImageIcon(newBWImg));
		}
	
	private void drawLineVertikal(BufferedImage newBWImg, JLabel lblCroppedKTP) {
		int w = newBWImg.getWidth();
		int h = newBWImg.getHeight();
		int lblW = lblCroppedKTP.getWidth();
		int ketemuGaris = 0;
		batasAtas = 0;
		int barisKe = 0;
		int batasBawah;
		int banyakBarisPutih = 0;
		cariPixelHorizontal2(newBWImg);
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
				barisKe++;
				banyakBarisPutih = 0;
				batasBawah = i;
				panjangBaris = batasBawah - batasAtas;
				System.out.println("Batas atas " + batasAtas + " Batas bawah " + batasBawah + " Panjang baris " + panjangBaris);
				cariPixelVertikal(newBWImg);
				for(int j = 0; j < w; j++){
					/*if(arrayPixelVertikalPutih[j] == panjangBaris-1 && arrayPixelVertikal[j+1] == 0){
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
								if(j != 4 && arrayPixelVertikal[j-5] != 0){
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
					}*/
					if(arrayPixelVertikal[j] != 0 && arrayPixelVertikal[j-1] == 0){
						Graphics g2 = newBWImg.getGraphics();
				        g2.setColor(Color.BLUE);
				        g2.drawLine(j-1, batasAtas+1, j-1, batasBawah);
				        banyakBarisPutih = 0;
					}
					if(arrayPixelVertikal[j] != 0 && arrayPixelVertikal[j+1] == 0){
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
	

	public textRecogEKTP() {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1386, 799);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnOpenFile = new JButton("Open File");
		btnOpenFile.setBounds(0, 11, 122, 23);
		contentPane.add(btnOpenFile);
		
		JLabel lblFirstImage = new JLabel("");
		lblFirstImage.setBounds(150, 11, 817, 391);
		contentPane.add(lblFirstImage);
		
		JButton btnThresholding1 = new JButton("Thresholding 1");
		btnThresholding1.setBounds(0, 165, 122, 23);
		contentPane.add(btnThresholding1);
		
		JLabel lblCroppedKTP = new JLabel("");
		lblCroppedKTP.setBounds(150, 422, 500, 300);
		contentPane.add(lblCroppedKTP);
		
		JButton btnManualCrop = new JButton("Manual Crop");
		btnManualCrop.setBounds(0, 94, 122, 23);
		contentPane.add(btnManualCrop);
		
		JButton btnThresholding2 = new JButton("Thresholding 2");
		btnThresholding2.setBounds(0, 194, 122, 23);
		contentPane.add(btnThresholding2);
		
		JButton btnCariPixelHitamHorizontal = new JButton("Cari Pixel Hitam H");
		
		btnCariPixelHitamHorizontal.setBounds(0, 252, 122, 23);
		contentPane.add(btnCariPixelHitamHorizontal);
		
		JButton btnCariPixelHitamVertikal = new JButton("Cari Pixel Hitam");
		btnCariPixelHitamVertikal.setBounds(0, 281, 122, 23);
		contentPane.add(btnCariPixelHitamVertikal);
		
		tfNilaiThreshold = new JTextField();
		tfNilaiThreshold.setText("100");
		tfNilaiThreshold.setBounds(0, 137, 86, 20);
		contentPane.add(tfNilaiThreshold);
		tfNilaiThreshold.setColumns(10);
		
		JLabel lblNilaiThreshold = new JLabel("Nilai Threshold");
		lblNilaiThreshold.setBounds(0, 122, 122, 14);
		contentPane.add(lblNilaiThreshold);
		
		tfMCX = new JTextField();
		tfMCX.setText("25");
		tfMCX.setBounds(33, 45, 86, 20);
		contentPane.add(tfMCX);
		tfMCX.setColumns(10);
		
		tfMCY = new JTextField();
		tfMCY.setText("55");
		tfMCY.setColumns(10);
		tfMCY.setBounds(33, 70, 86, 20);
		contentPane.add(tfMCY);
		
		JLabel lblX = new JLabel("x :");
		lblX.setBounds(10, 48, 32, 14);
		contentPane.add(lblX);
		
		JLabel lblY = new JLabel("y :");
		lblY.setBounds(10, 73, 32, 14);
		contentPane.add(lblY);
		
		JButton btnThresholding3 = new JButton("Thresholding 3");
		btnThresholding3.setBounds(0, 224, 122, 23);
		contentPane.add(btnThresholding3);
		
		btnOpenFile.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				/*JFileChooser jfc = new JFileChooser(); 
				if(jfc.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) { 
					file = jfc.getSelectedFile() ; 
		        }*/
				try {
					int lblW = lblFirstImage.getWidth();
					int lblH = lblFirstImage.getHeight();
					BufferedImage img = ImageIO.read(new File("D:/Topic/contoh ektp1.png"));
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
		
		btnThresholding1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e2) {
				  BufferedImage greyscalledImg = newCroppedImg;
				  newGreyscalledImg = new BufferedImage(315, 215, BufferedImage.TYPE_BYTE_BINARY);
		    	  Graphics g = newGreyscalledImg.createGraphics();
		    	  g.drawImage(greyscalledImg, 0, 0, 315, 215, null);
		    	  g.dispose();
		    	  lblCroppedKTP.setIcon(new ImageIcon(newGreyscalledImg));
				  //marchThroughImage(newGreyscalledImg);
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
		
		btnThresholding3.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				BufferedImage greyscalledImg = newCroppedImg;
				int w = greyscalledImg.getWidth();
				int h = greyscalledImg.getHeight();
				int nilaiThreshold = Integer.parseInt(tfNilaiThreshold.getText());
				for (int i = 0; i < h; i++) {
				      for (int j = 0; j < w; j++) {
				    	  greyscalledImg.getRGB(j,i);
				    	  Color c = new Color(greyscalledImg.getRGB(j, i));
				    	  int average = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
				    	  if(average > nilaiThreshold) {
				    		  greyscalledImg.setRGB(j, i, Color.white.getRGB());
				    	  }
				    	  else {
				    		  greyscalledImg.setRGB(j, i, Color.black.getRGB());
				    	  }
				      }
				}
				newBWImg = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
		    	Graphics g1 = newBWImg.createGraphics();
		    	g1.drawImage(greyscalledImg, 0, 0, w, h, null);
		    	g1.dispose();
		    	lblCroppedKTP.setIcon(new ImageIcon(newBWImg));
			}
		});
		
		btnCariPixelHitamHorizontal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				cariPixelHorizontal(newBWImg);
				drawLineHorizontal(newBWImg, lblCroppedKTP);
			}
		});
		
		btnCariPixelHitamVertikal.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				drawLineVertikal(newBWImg, lblCroppedKTP);
				formSementara fs = new formSementara();
				fs.lblAutocrop.setIcon(lblCroppedKTP.getIcon());
				fs.setVisible(true);
			}
		});
	}
}
