import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.border.LineBorder;
import javax.swing.text.AbstractDocument;
import javax.swing.text.BadLocationException;
import javax.swing.text.DocumentFilter;
import javax.swing.text.PlainDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import java.awt.Frame;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;

public class formSementara2 extends JFrame {
	
	
	int batasAtas;
	int batasKiri;
	JLabel lblAutocrop = new JLabel();
	JLabel [] labelcropV = new  JLabel[1000] ;
	JTextField [] tfInputChar = new JTextField[1000];
	int noLabel = 0;
	int[] arrayPixelHorizontal = new int[400];
	int[] arrayPixelVertikal = new int[500];
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
	
	//textRecogEKTP tre = new textRecogEKTP(); 
	private JTextField tfGambar;
	
	public class JTextFieldLimit extends PlainDocument {
		  private int limit;
		  JTextFieldLimit(int limit) {
		    super();
		    this.limit = limit;
		  }

		  JTextFieldLimit(int limit, boolean upper) {
		    super();
		    this.limit = limit;
		  }

		  public void insertString(int offset, String str, AttributeSet attr) throws BadLocationException {
		    if (str == null)
		      return;

		    if ((getLength() + str.length()) <= limit) {
		      super.insertString(offset, str, attr);
		    }
		  }
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
	
	private void euclideanDistance() throws ClassNotFoundException, SQLException{
		int nomorgambar = Integer.parseInt(tfGambar.getText());
		cariPixelHorizontal2(nomorgambar);
		cariPixelVertikal2(nomorgambar);
		cariPixelLeftDiagonal(nomorgambar);
		
		Database db = new Database();
		int count = db.selectCount(db.getConnection());
		ArrayList<Integer> idChar = db.selectId(db.getConnection());
		ArrayList<Double> arrayEuclid = new ArrayList<>();
		for (int i = 0; i < count; i++){
			double latHoriSD = db.selectHoriSD(db.getConnection(), idChar.get(i));
			double latHoriM = db.selectHoriM(db.getConnection(), idChar.get(i));
			double latVertiSD = db.selectVertiSD(db.getConnection(), idChar.get(i));
			double latVertiM = db.selectVertiM(db.getConnection(), idChar.get(i));
			double latDiagSD = db.selectDiagSD(db.getConnection(), idChar.get(i));
			double latDiagM = db.selectDiagM(db.getConnection(), idChar.get(i));
			double euclid = Math.sqrt(Math.pow((stdHori.get(0) - latHoriSD), 2) + Math.pow((meanHori.get(0) - latHoriM), 2) + Math.pow((stdVert.get(0) - latVertiSD), 2) 
			+ Math.pow((meanVert.get(0) - latVertiM), 2) + Math.pow((stdDiag.get(0) - latDiagSD), 2) + Math.pow((meanDiag.get(0) - latDiagM), 2));
			System.out.println(euclid);
			arrayEuclid.add(euclid);
		}
	}
	
	private void euclideanDistance2(int nomorgambar) throws ClassNotFoundException, SQLException{
		cariPixelHorizontal2(nomorgambar);
		cariPixelVertikal2(nomorgambar);
		cariPixelLeftDiagonal(nomorgambar);
		
		Database db = new Database();
		int count = db.selectCount(db.getConnection());
		ArrayList<Integer> idChar = db.selectId(db.getConnection());
		ArrayList<Double> arrayEuclid = new ArrayList<>();
		for (int i = 0; i < count; i++){
			db.selectAll(db.getConnection(), idChar.get(i));
			double euclid = Math.sqrt(Math.pow((stdHori.get(0) - db.latStdHori), 2) + Math.pow((meanHori.get(0) - db.latMeanHori), 2) + Math.pow((stdVert.get(0) - db.latStdVert), 2) 
			+ Math.pow((meanVert.get(0) - db.latMeanVert), 2) + Math.pow((stdDiag.get(0) - db.latStdDiag), 2) + Math.pow((meanDiag.get(0) - db.latMeanDiag), 2));
			System.out.println(euclid);
			arrayEuclid.add(euclid);
		}
		double min = arrayEuclid.get(0);
		int minIndex = 0;
		for(int i = 0; i < arrayEuclid.size(); i++) {
		    double number = arrayEuclid.get(i);
		    if(number < min) {
		    	min = number;
		    	minIndex = i;
		    }
		}
		db.selectAll(db.getConnection(), idChar.get(minIndex));
		System.out.println("min = " + min + ", Index ke- " + minIndex + " berupa karakter: " + db.latKarakter);
		hasilChar.add(db.latKarakter);
		idChar.clear();
		arrayEuclid.clear();
		stdHori.clear();
		meanHori.clear();
		stdVert.clear();
		meanVert.clear();
		stdDiag.clear();
		meanDiag.clear();
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
	
	private void cariPixelVertikal(BufferedImage newImg) {
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

	private void cariPixelHorizontal2(int nomorLabel) {
		Icon icon = labelcropV[nomorLabel].getIcon();
		BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		icon.paintIcon(null, g, 0, 0);
		g.dispose();
		int w = img.getWidth();
	    int h = img.getHeight();
	    int arrayPixelHorizontal2[] = new int[20];
	    System.out.print("Gambar ke " + nomorLabel + " nilai pixel hitam secara horizontal : ");

	    for (int i = 0; i < h; i++) {
	    	int jmlPixelHitam = 0;
	      for (int j = 0; j < w; j++) {
	        int pixel = img.getRGB(j, i);
	        int red = getRed(pixel);
	        int green = getGreen(pixel);
	        int blue = getBlue(pixel);
	        if(red == 0 && green == 0 && blue == 0){
	        	jmlPixelHitam++;
	        }
	      }
	      arrayPixelHorizontal2[i] = jmlPixelHitam;
	      System.out.println(jmlPixelHitam);
	    }
	    System.out.println("");
	    double sd1 = 0;
	    int sd2 = 0;
	    int sum = 0;
	    double sdfinal = 0;
	    double mean = 0;
	    for (int k = 0; k < arrayPixelHorizontal2.length; k++){
	    	sd1 = (sd1 + Math.pow(arrayPixelHorizontal2[k], 2));
	    	sd2 = sd2 + arrayPixelHorizontal2[k];
	    	if (arrayPixelHorizontal2[k] > 0){
	    		sum++;
	    	}
	    }
	    if(sd1 == 0 && sd2 == 0 && sum == 0){
	    	sdfinal = 0;
	    	mean = 0;
	    }
	    else{
	    	sdfinal = Math.sqrt((sd1 - (Math.pow(sd2, 2) / sum)) / (sum - 1));
		    mean = (double) sd2 / sum;
	    }
	    System.out.println("Sd1: " + sd1 + ", Sd2 : " + sd2 + ", sum : " + sum);
	    System.out.println("Standard deviasi: " + sdfinal);
	    System.out.println("Mean: " + mean);
	    stdHori.add(sdfinal);
	    meanHori.add(mean);
	}
	
	private void cariPixelVertikal2(int nomorLabel) {
		Icon icon = labelcropV[nomorLabel].getIcon();
		BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		icon.paintIcon(null, g, 0, 0);
		g.dispose();
		int w = img.getWidth();
	    int h = img.getHeight();
	    int arrayPixelVertikal2[] = new int[20];
	    System.out.print("Gambar ke " + nomorLabel + " nilai pixel hitam secara vertikal : ");
	    
	    for (int i = 0; i < w; i++) {
	    	int jmlPixelHitam = 0;
	      for (int j = 0; j < h; j++) {
	        int pixel = img.getRGB(i, j);
	        int red = getRed(pixel);
	        int green = getGreen(pixel);
	        int blue = getBlue(pixel);
	        if(red == 0 && green == 0 && blue == 0){
	        	jmlPixelHitam++;
	        }
	      }
	      arrayPixelVertikal2[i] = jmlPixelHitam;
	      System.out.println(jmlPixelHitam);
	    }
	    System.out.println("");
	    double sd1 = 0;
	    int sd2 = 0;
	    int sum = 0;
	    double sdfinal = 0;
	    double mean = 0;
	    for (int k = 0; k < arrayPixelVertikal2.length; k++){
	    	sd1 = (sd1 + Math.pow(arrayPixelVertikal2[k], 2));
	    	sd2 = sd2 + arrayPixelVertikal2[k];
	    	if (arrayPixelVertikal2[k] > 0){
	    		sum++;
	    	}
	    }
	    if(sd1 == 0 && sd2 == 0 && sum == 0){
	    	sdfinal = 0;
	    	mean = 0;
	    }
	    else{
	    	sdfinal = Math.sqrt((sd1 - (Math.pow(sd2, 2) / sum)) / (sum - 1));
		    mean = (double) sd2 / sum;
	    }
	    System.out.println("Sd1: " + sd1 + ", Sd2 : " + sd2 + ", sum : " + sum);
	    System.out.println("Standard deviasi: " + sdfinal);
	    System.out.println("Mean: " + mean);
	    
	    stdVert.add(sdfinal);
	    meanVert.add(mean);
	}
	
	private void cariPixelLeftDiagonal(int nomorLabel) {
		Icon icon = labelcropV[nomorLabel].getIcon();
		BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		icon.paintIcon(null, g, 0, 0);
		g.dispose();
	    int w = img.getWidth();
	    int h = img.getHeight();
	    System.out.print("Gambar ke " + nomorLabel + " nilai pixel hitam secara diagonal : ");
	
	    int diagonalKe = 1;
	    for (int i = 0; i < w; i++) {
	    	int a = i;
	    	jmlPixelHitam = 0;
	      for (int j = 0; j < w; j++) {
	    	if(a == -1 || j > h-1){
	    		break;
	    	}
	        int pixel = img.getRGB(a, j);
	        int red = getRed(pixel);
	        int green = getGreen(pixel);
	        int blue = getBlue(pixel);
	        if(red == 0 && green == 0 && blue == 0){
	        	jmlPixelHitam++;
	        }
	        a--;
	      }
	      arrayPixelLeftDiagonal[diagonalKe] = jmlPixelHitam;
	      //System.out.println("Diagonal ke " + diagonalKe + " terdapat " + arrayPixelLeftDiagonal[diagonalKe] + " pixel Hitam");
	      System.out.println(jmlPixelHitam);
	      diagonalKe++;
	    }
	    for (int i = 1; i < h; i++) {
	    	int a = w-1;
	    	int b = i;
	    	jmlPixelHitam = 0;
	      for (int j = 0; j < w; j++) {
	    	if(a == -1 || b > h-1){
	    		break;
	    	}
	        int pixel = img.getRGB(a, b);
	        int red = getRed(pixel);
	        int green = getGreen(pixel);
	        int blue = getBlue(pixel);
	        if(red == 0 && green == 0 && blue == 0){
	        	jmlPixelHitam++;
	        }
	        a--;
	        b++;
	      }
	      arrayPixelLeftDiagonal[diagonalKe] = jmlPixelHitam;
	      //System.out.println("Diagonal ke " + diagonalKe + " terdapat " + arrayPixelLeftDiagonal[diagonalKe] + " pixel Hitam");
	      System.out.println(jmlPixelHitam);
	      diagonalKe++;
	    }
	    double sd1 = 0;
	    int sd2 = 0;
	    int sum = 0;
	    double sdfinal = 0;
	    double mean = 0;
	    for (int k = 0; k < arrayPixelLeftDiagonal.length; k++){
	    	sd1 = (sd1 + Math.pow(arrayPixelLeftDiagonal[k], 2));
	    	sd2 = sd2 + arrayPixelLeftDiagonal[k];
	    	if (arrayPixelLeftDiagonal[k] > 0){
	    		sum++;
	    	}
	    }
	    if(sd1 == 0 && sd2 == 0 && sum == 0){
	    	sdfinal = 0;
	    	mean = 0;
	    }
	    else{
	    	sdfinal = Math.sqrt((sd1 - (Math.pow(sd2, 2) / sum)) / (sum - 1));
		    mean = (double) sd2 / sum;
	    }
	    System.out.println("Sd1: " + sd1 + ", Sd2 : " + sd2 + ", sum : " + sum);
	    System.out.println("Standard deviasi: " + sdfinal);
	    System.out.println("Mean: " + mean);
	    stdDiag.add(sdfinal);
	    meanDiag.add(mean);
	}
	
	private void croppingHorizontal(BufferedImage newBWImg, JLabel [] labelcrop) {
		int w = newBWImg.getWidth();
		int h = newBWImg.getHeight();
		int lblW = lblAutocrop.getWidth();
		int bariske = 0;
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
	
	private void croppingVertikal(JLabel [] labelcrop) {
		FormLatih fl = new FormLatih();
		int y = 330;
		
		for(int i = 0; i < 13; i++){
			Icon icon = labelcrop[i].getIcon();
			BufferedImage croppedH = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = croppedH.getGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			
			cariPixelVertikal(croppedH);
			int w = labelcrop[i].getWidth();
			int h = labelcrop[i].getHeight();
			int ketemuGaris = 0;
			int batasKanan = 0;
			int x = 10;
			batasKiri = 0;
			
			for(int j = 0; j < w; j++){
				if(arrayPixelVertikal[j] == 18){
					ketemuGaris++;
					System.out.println("Kolom ke " + j);
					System.out.println("Ketemu garis " + ketemuGaris);
					if(ketemuGaris == 1){
						batasKiri = j;
					}
				}
				if(ketemuGaris == 2){
					batasKanan = j;
					int panjangKolom = batasKanan - batasKiri;
					fl.tfInputChar[noLabel] = new JTextField();
					fl.tfInputChar[noLabel].setDocument(new JTextFieldLimit(1));
					fl.tfInputChar[noLabel].setBounds(x, y-30 , 15, 18);
					fl.labelnomor[noLabel] = new JLabel(String.valueOf(noLabel));
					fl.labelnomor[noLabel].setFont(fl.labelnomor[noLabel].getFont().deriveFont(10.0f));
					fl.labelnomor[noLabel].setBounds(x, y-15 , 30, 18);
					fl.labelcropV[noLabel] = new JLabel();
					fl.labelcropV[noLabel].setBounds(x , y , panjangKolom, 18);
					//Border border = BorderFactory.createLineBorder(Color.GREEN);
					//labelcropV[noLabel].setBorder(border);
					x+=25;
					int lblCW = fl.labelcropV[noLabel].getWidth();
					int lblCH = fl.labelcropV[noLabel].getHeight();
					BufferedImage croppedV = croppedH.getSubimage(batasKiri + 1, 0, panjangKolom, lblCH);
					BufferedImage newCroppedV = new BufferedImage(lblCW, lblCH, BufferedImage.TYPE_INT_RGB);
					Graphics g1 = newCroppedV.createGraphics();
					g1.drawImage(croppedV, 0, 0, lblCW, lblCH, null);
					g1.dispose();
					fl.labelcropV[noLabel].setIcon(new ImageIcon(newCroppedV));
					ketemuGaris = 0;
					noLabel++;
				}
				}
			y+=50;
			}
		fl.setVisible(true);
		}
	
	private void glblHistogram(JLabel [] labelcrop, JLabel label) {
		
		}
		
	/**
	 * Create the frame.
	 */
	public formSementara2() {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1173, 744);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		lblAutocrop.setBorder(new LineBorder(new Color(0, 0, 0)));

		lblAutocrop.setBounds(10, 11, 500, 280);
		contentPane.add(lblAutocrop);
		
		JLabel lblNewLabel = new JLabel("");
		lblNewLabel.setBounds(10, 311, 500, 18);
		contentPane.add(lblNewLabel);
		
		JLabel [] labelcrop = new  JLabel[13] ;
		int sum=11;
		for (int i = 0; i < 13; i++) {
			labelcrop[i] = new JLabel();
			labelcrop[i].setBounds(800 , sum ,500, 18);
			Border border = BorderFactory.createLineBorder(Color.GREEN);
			labelcrop[i].setBorder(border);
			sum+=30;
			contentPane.add(labelcrop[i]);
		}

		JButton btnNewButton = new JButton("Latih");
		btnNewButton.setBounds(898, 537, 89, 23);
		contentPane.add(btnNewButton);
		
		JLabel lblGambarKe = new JLabel("Gambar ke-");
		lblGambarKe.setBounds(824, 509, 64, 14);
		contentPane.add(lblGambarKe);
		
		tfGambar = new JTextField();
		tfGambar.setBounds(898, 506, 43, 20);
		contentPane.add(tfGambar);
		tfGambar.setColumns(10);
		
		JButton btnEuclid = new JButton("Euclidean Distance");
		btnEuclid.setBounds(867, 571, 148, 23);
		contentPane.add(btnEuclid);
		
		croppingHorizontal(textRecogEKTP.newBWImg, labelcrop);
		croppingVertikal(labelcrop);
		
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				//for(int i = 0; i < 20; i++){
				int nomorgambar = Integer.parseInt(tfGambar.getText());
				cariPixelHorizontal2(nomorgambar);
				cariPixelVertikal2(nomorgambar);
					cariPixelLeftDiagonal(nomorgambar);
				//}
					Database db = new Database();
				
					//for (int i = 0; i < 1; i++) {
						String karakter=JOptionPane.showInputDialog("Masukan Huruf");
						try {
							db.insert(db.getConnection(),karakter ,stdHori.get(z), meanHori.get(z), stdVert.get(z), meanVert.get(z), stdDiag.get(z), meanDiag.get(z));
						} catch (ClassNotFoundException | SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					//}
						z++;
			}
		});
		
		btnEuclid.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Database db = new Database();
				int count = 0;
				try {
					count = db.selectCount(db.getConnection());
				} catch (ClassNotFoundException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				} catch (SQLException e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}
				try {
					for(int i = 0; i < 19; i++){
						euclideanDistance2(i);
					}
					for(int i = 0; i < 19; i++){
						System.out.print(hasilChar.get(i));
					}
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
		});
		
		/*Database db = new Database();
		try {
			int count = db.selectCount(db.getConnection());
			ArrayList<Integer> idChar = db.selectId(db.getConnection());
			for(int i = 0; i < count; i++){
				System.out.println(idChar.get(i));
			}
			System.out.println(db.selectDiagM(db.getConnection(), 1));
		} catch (ClassNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}*/
	}
}
