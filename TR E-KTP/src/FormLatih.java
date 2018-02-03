import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;
import javax.swing.border.TitledBorder;
import javax.swing.JButton;
import java.awt.Font;
import java.awt.Color;
import java.awt.SystemColor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.SwingConstants;
import javax.swing.border.LineBorder;

public class FormLatih extends JFrame {

	int batasKiri;
	JLabel [] labelcropV = new  JLabel[1000] ;
	JTextField [] tfInputChar = new JTextField[1000];
	JLabel [] labelnomor = new  JLabel[1000] ;
	int jmlPixelHitam = 0;
	int noLabel = 0;
	ArrayList<Integer> arrayPixelLeftDiagonal = new ArrayList<>();
	ArrayList<Double> stdHori=new ArrayList<Double>();
    ArrayList<Double> meanHori=new ArrayList<Double>();
    ArrayList<Double> stdVert=new ArrayList<Double>();
    ArrayList<Double> meanVert=new ArrayList<Double>();
    ArrayList<Double> stdDiag=new ArrayList<Double>();
    ArrayList<Double> meanDiag=new ArrayList<Double>();
    ArrayList<String> hasilChar=new ArrayList<String>(); 
	
	private JPanel contentPane;
	private JTextField tfGambarKe;


	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					FormLatih frame = new FormLatih();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
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
	
	private void croppingVertikal() {
		JLabel [] labelnomor = new  JLabel[1000] ;
		int y = 40;
		for(int i = 0; i < formSementara.bariske; i++){
			Icon icon = formSementara.labelcrop[i].getIcon();
			BufferedImage croppedH = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = croppedH.getGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			
			formSementara.cariPixelVertikal(croppedH);
			int w = formSementara.labelcrop[i].getWidth();
			int h = formSementara.labelcrop[i].getHeight();
			int ketemuGaris = 0;
			int batasKanan = 0;
			int x = 30;
			batasKiri = 0;
			
			for(int j = 0; j < w; j++){
				if(formSementara.arrayPixelVertikal[j] == 18){
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
					tfInputChar[noLabel] = new JTextField();
					tfInputChar[noLabel].setDocument(new JTextFieldLimit(1));
					tfInputChar[noLabel].setBounds(x, y-30 , 15, 18);
					labelnomor[noLabel] = new JLabel(String.valueOf(noLabel));
					labelnomor[noLabel].setFont(labelnomor[noLabel].getFont().deriveFont(10.0f));
					labelnomor[noLabel].setBounds(x, y-15 , 30, 18);
					labelcropV[noLabel] = new JLabel();
					labelcropV[noLabel].setBounds(x , y , panjangKolom, 18);
					//Border border = BorderFactory.createLineBorder(Color.GREEN);
					//labelcropV[noLabel].setBorder(border);
					x+=30;
					contentPane.add(tfInputChar[noLabel]);
					contentPane.add(labelnomor[noLabel]);
					contentPane.add(labelcropV[noLabel]);
					int lblCW = labelcropV[noLabel].getWidth();
					int lblCH = labelcropV[noLabel].getHeight();
					BufferedImage croppedV = croppedH.getSubimage(batasKiri + 1, 0, panjangKolom, lblCH);
					BufferedImage newCroppedV = new BufferedImage(lblCW, lblCH, BufferedImage.TYPE_INT_RGB);
					Graphics g1 = newCroppedV.createGraphics();
					g1.drawImage(croppedV, 0, 0, lblCW, lblCH, null);
					g1.dispose();
					labelcropV[noLabel].setIcon(new ImageIcon(newCroppedV));
					ketemuGaris = 0;
					noLabel++;
				}
				}
			y+=50;
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
	    ArrayList<Integer> arrayPixelHorizontal2 = new ArrayList<>();
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
	      arrayPixelHorizontal2.add(jmlPixelHitam);
	      System.out.println(jmlPixelHitam);
	    }
	    System.out.println("");
	    double sd1 = 0;
	    int sd2 = 0;
	    int sum = 0;
	    double sdfinal = 0;
	    double mean = 0;
	    for (int k = 0; k < arrayPixelHorizontal2.size(); k++){
	    	sd1 = (sd1 + Math.pow(arrayPixelHorizontal2.get(k), 2));
	    	sd2 = sd2 + arrayPixelHorizontal2.get(k);
	    	if (arrayPixelHorizontal2.get(k) > 0){
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
	    arrayPixelHorizontal2.clear();
	}
	
	private void cariPixelVertikal2(int nomorLabel) {
		Icon icon = labelcropV[nomorLabel].getIcon();
		BufferedImage img = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
		Graphics g = img.getGraphics();
		icon.paintIcon(null, g, 0, 0);
		g.dispose();
		int w = img.getWidth();
	    int h = img.getHeight();
	    ArrayList<Integer> arrayPixelVertikal2 = new ArrayList<>();
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
	      arrayPixelVertikal2.add(jmlPixelHitam);
	      System.out.println(jmlPixelHitam);
	    }
	    System.out.println("");
	    double sd1 = 0;
	    int sd2 = 0;
	    int sum = 0;
	    double sdfinal = 0;
	    double mean = 0;
	    for (int k = 0; k < arrayPixelVertikal2.size(); k++){
	    	sd1 = (sd1 + Math.pow(arrayPixelVertikal2.get(k), 2));
	    	sd2 = sd2 + arrayPixelVertikal2.get(k);
	    	if (arrayPixelVertikal2.get(k) > 0){
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
	    arrayPixelVertikal2.clear();
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
	      arrayPixelLeftDiagonal.add(jmlPixelHitam);
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
	      arrayPixelLeftDiagonal.add(jmlPixelHitam);
	      //System.out.println("Diagonal ke " + diagonalKe + " terdapat " + arrayPixelLeftDiagonal[diagonalKe] + " pixel Hitam");
	      System.out.println(jmlPixelHitam);
	      diagonalKe++;
	    }
	    double sd1 = 0;
	    int sd2 = 0;
	    int sum = 0;
	    double sdfinal = 0;
	    double mean = 0;
	    for (int k = 0; k < arrayPixelLeftDiagonal.size(); k++){
	    	sd1 = (sd1 + Math.pow(arrayPixelLeftDiagonal.get(k), 2));
	    	sd2 = sd2 + arrayPixelLeftDiagonal.get(k);
	    	if (arrayPixelLeftDiagonal.get(k) > 0){
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
	    arrayPixelLeftDiagonal.clear();
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
			//System.out.println(euclid);
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
		//System.out.println("min = " + min + ", Index ke- " + minIndex + " berupa karakter: " + db.latKarakter);
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

	public FormLatih() {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1173, 744);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JPanel panel_1 = new JPanel();
		panel_1.setBackground(SystemColor.inactiveCaptionBorder);
		panel_1.setBorder(new TitledBorder(null, "Latih Semua Karakter", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_1.setBounds(1165, 214, 187, 93);
		contentPane.add(panel_1);
		panel_1.setLayout(null);
		
		JButton btnLatihSemuaKarakter = new JButton("Latih Karakter");
		btnLatihSemuaKarakter.setBounds(23, 31, 139, 39);
		panel_1.add(btnLatihSemuaKarakter);
		
		
		JPanel panel = new JPanel();
		panel.setBackground(SystemColor.inactiveCaptionBorder);
		panel.setBorder(new TitledBorder(null, "Latih 1 Karakter", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel.setBounds(1165, 45, 187, 158);
		contentPane.add(panel);
		panel.setLayout(null);
		
		JButton btnLatih1Karakter = new JButton("Latih Karakter");
		btnLatih1Karakter.setBounds(23, 103, 140, 35);
		panel.add(btnLatih1Karakter);
		
		JLabel lblGambarKe = new JLabel("Gambar ke-");
		lblGambarKe.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblGambarKe.setBounds(23, 24, 92, 27);
		panel.add(lblGambarKe);
		
		tfGambarKe = new JTextField();
		tfGambarKe.setHorizontalAlignment(SwingConstants.CENTER);
		tfGambarKe.setFont(new Font("Tahoma", Font.PLAIN, 20));
		tfGambarKe.setBounds(67, 58, 51, 35);
		panel.add(tfGambarKe);
		tfGambarKe.setColumns(10);
		
		JPanel panel_2 = new JPanel();
		panel_2.setBorder(new LineBorder(Color.LIGHT_GRAY, 2));
		panel_2.setBounds(1151, -14, 222, 767);
		contentPane.add(panel_2);
		panel_2.setLayout(null);
		
		JButton btnUjiDataLatih = new JButton("Uji Data Latih");
		btnUjiDataLatih.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnUjiDataLatih.setBounds(984, 11, 157, 38);
		contentPane.add(btnUjiDataLatih);
		
		croppingVertikal();
		
		btnLatih1Karakter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {

				int nomorgambar = Integer.parseInt(tfGambarKe.getText());
				cariPixelHorizontal2(nomorgambar);
				cariPixelVertikal2(nomorgambar);
				cariPixelLeftDiagonal(nomorgambar);

				Database db = new Database();
				String karakter=JOptionPane.showInputDialog("Masukan Karakter");
						try {
							db.insert(db.getConnection(),karakter ,stdHori.get(0), meanHori.get(0), stdVert.get(0), meanVert.get(0), stdDiag.get(0), meanDiag.get(0));
						} catch (ClassNotFoundException | SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
				stdHori.clear();
				meanHori.clear();
				stdVert.clear();
				meanVert.clear();
				stdDiag.clear();
				meanDiag.clear();
			}
		});
		

		btnLatihSemuaKarakter.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				for(int i = 0; i < noLabel; i++){
					cariPixelHorizontal2(i);
					cariPixelVertikal2(i);
					cariPixelLeftDiagonal(i);
					
					String karakter = tfInputChar[i].getText();
					
					Database db = new Database();
					try {
						db.insert(db.getConnection(),karakter ,stdHori.get(i), meanHori.get(i), stdVert.get(i), meanVert.get(i), stdDiag.get(i), meanDiag.get(i));
					} catch (ClassNotFoundException | SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				stdHori.clear();
				meanHori.clear();
				stdVert.clear();
				meanVert.clear();
				stdDiag.clear();
				meanDiag.clear();
			}
		});
		
		btnUjiDataLatih.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				int y1 = labelcropV[0].getY();
				int x = 0;
				try {
						for(int i = 0; i <= noLabel; i++){
							euclideanDistance2(i);
						}
						for(int i = 0; i <= noLabel; i++){
							int y = labelcropV[i].getY();
							if(y1 == y){
								System.out.print(hasilChar.get(x));
								x++;
							}
							else{
								System.out.println(hasilChar.get(x));
								x++;
							}
							y1 = y;
						}
					}	
				 catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				hasilChar.clear();
			}
		});
	}
}
