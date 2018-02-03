
import java.awt.EventQueue;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;

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

public class formUji extends JFrame {

	int batasKiri;
	JLabel [] labelcropV = new  JLabel[1000] ;
	JTextField [] tfInputChar = new JTextField[1000];
	JLabel [] labelnomor = new  JLabel[1000] ;
	int jmlPixelHitam = 0;
	int noLabel = 0;
	static String[] arrayString = null;
	ArrayList<Integer> arrayPixelLeftDiagonal = new ArrayList<>();
	ArrayList<Double> stdHori=new ArrayList<Double>();
    ArrayList<Double> meanHori=new ArrayList<Double>();
    ArrayList<Double> stdVert=new ArrayList<Double>();
    ArrayList<Double> meanVert=new ArrayList<Double>();
    ArrayList<Double> stdDiag=new ArrayList<Double>();
    ArrayList<Double> meanDiag=new ArrayList<Double>();
    ArrayList<String> hasilChar=new ArrayList<String>(); 

	private JPanel contentPane;
	private JTextField tfRange1;
	private JTextField tfRange2;
	private JButton btnLevenshtein;


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
		int y = 50;
		for(int i = 0; i < 13; i++){
			Icon icon = formProses.labelcrop[i].getIcon();
			BufferedImage croppedH = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_RGB);
			Graphics g = croppedH.getGraphics();
			icon.paintIcon(null, g, 0, 0);
			g.dispose();
			
			formProses.cariPixelVertikal(croppedH);
			int w = formProses.labelcrop[i].getWidth();
			int h = formProses.labelcrop[i].getHeight();
			int ketemuGaris = 0;
			int batasKanan = 0;
			int x = 10;
			batasKiri = 0;
			
			for(int j = 0; j < w; j++){
				if(formProses.arrayPixelVertikal[j] == 18){
					ketemuGaris++;
					//System.out.println("Kolom ke " + j);
					//System.out.println("Ketemu garis " + ketemuGaris);
					if(ketemuGaris == 1){
						batasKiri = j;
					}
				}
				if(ketemuGaris == 2){
					batasKanan = j;
					int panjangKolom = batasKanan - batasKiri;
					labelnomor[noLabel] = new JLabel(String.valueOf(noLabel));
					labelnomor[noLabel].setFont(labelnomor[noLabel].getFont().deriveFont(10.0f));
					labelnomor[noLabel].setBounds(x, y-15 , 30, 18);
					labelcropV[noLabel] = new JLabel();
					labelcropV[noLabel].setBounds(x , y , panjangKolom, 18);
					//Border border = BorderFactory.createLineBorder(Color.GREEN);
					//labelcropV[noLabel].setBorder(border);
					x+=20;
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
	    //System.out.println("Gambar ke " + nomorLabel + " hori ");

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
	      //System.out.println(jmlPixelHitam);
	    }
	    //System.out.println("");
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
	    //System.out.println("Sd1: " + sd1 + ", Sd2 : " + sd2 + ", sum : " + sum);
	    //System.out.println(sdfinal);
	    //System.out.println(mean);
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
	    //System.out.print("Gambar ke " + nomorLabel + " vert ");
	    
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
	      //System.out.println(jmlPixelHitam);
	    }
	    //System.out.println("");
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
	    //System.out.println("Sd1: " + sd1 + ", Sd2 : " + sd2 + ", sum : " + sum);
	    //System.out.println(sdfinal);
	    //System.out.println(mean);
	    
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
	    //System.out.print("Gambar ke " + nomorLabel + " diag ");
	
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
	      //System.out.println(jmlPixelHitam);
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
	      //System.out.println(jmlPixelHitam);
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
	    //System.out.println("Sd1: " + sd1 + ", Sd2 : " + sd2 + ", sum : " + sum);
	    //System.out.println(sdfinal);
	    //System.out.println(mean);
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
	
		 
	    public static int distance(String a, String b) {
	        a = a.toLowerCase();
	        b = b.toLowerCase();
	        // i == 0
	        int [] costs = new int [b.length() + 1];
	        for (int j = 0; j < costs.length; j++)
	            costs[j] = j;
	        for (int i = 1; i <= a.length(); i++) {
	            // j == 0; nw = lev(i - 1, j)
	            costs[0] = i;
	            int nw = i - 1;
	            for (int j = 1; j <= b.length(); j++) {
	                int cj = Math.min(1 + Math.min(costs[j], costs[j - 1]), a.charAt(i - 1) == b.charAt(j - 1) ? nw : nw + 1);
	                nw = costs[j];
	                costs[j] = cj;
	            }
	        }
	        return costs[b.length()];
	    }
	 
	    public static void hitung() {
	    	String stringFinal = "";
	    	ArrayList<Integer> cost = new ArrayList<>();
	        for (int i = 0; i < arrayString.length; i++){
	        	if(!arrayString[i].equals("") && !arrayString[i].equals(":") && !arrayString[i].equals("/")){
	        	if(!Character.isDigit(arrayString[i].charAt(0))){
	        		for(int j = 0; j < Database.stringLatih.size(); j++){
		        		 cost.add(distance(arrayString[i],  Database.stringLatih.get(j)));   		 
		        	}
	        	
	        	int minIndex = cost.indexOf(Collections.min(cost));
	        	//System.out.println(Database.stringLatih.get(minIndex));	
	        	arrayString[i] = Database.stringLatih.get(minIndex);
	        	cost.clear();
	        	}
	        }  
	        	stringFinal += arrayString[i] + " ";
	        }
	        System.out.println(stringFinal);
	    }

	public formUji() throws ClassNotFoundException, SQLException {
		setExtendedState(Frame.MAXIMIZED_BOTH);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1173, 744);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JButton btnEuclidean = new JButton("Proses");
		btnEuclidean.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnEuclidean.setBounds(1067, 11, 139, 39);
		contentPane.add(btnEuclidean);
		
		JLabel lblRangeGambarYang = new JLabel("Range gambar yang ingin diproses");
		lblRangeGambarYang.setFont(new Font("Tahoma", Font.PLAIN, 15));
		lblRangeGambarYang.setBounds(804, 11, 231, 20);
		contentPane.add(lblRangeGambarYang);
		
		tfRange1 = new JTextField();
		tfRange1.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tfRange1.setBounds(849, 36, 58, 20);
		contentPane.add(tfRange1);
		tfRange1.setColumns(10);
		
		tfRange2 = new JTextField();
		tfRange2.setFont(new Font("Tahoma", Font.PLAIN, 15));
		tfRange2.setColumns(10);
		tfRange2.setBounds(944, 36, 58, 20);
		contentPane.add(tfRange2);
		
		JLabel label = new JLabel("-");
		label.setFont(new Font("Tahoma", Font.PLAIN, 20));
		label.setBounds(922, 37, 46, 14);
		contentPane.add(label);
		
		btnLevenshtein = new JButton("Levenshtein");
		btnLevenshtein.setFont(new Font("Tahoma", Font.BOLD, 15));
		btnLevenshtein.setBounds(1213, 11, 139, 39);
		contentPane.add(btnLevenshtein);
		
		btnEuclidean.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				String hasilUji = "";
				int k = Integer.parseInt(tfRange1.getText());
				int y1 = labelcropV[k].getY();
				int x = 0;
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
					if(tfRange2.getText().isEmpty()){
						int range1 = Integer.parseInt(tfRange1.getText());
						euclideanDistance2(range1);
						hasilUji = hasilChar.get(0);
						JOptionPane.showMessageDialog(null, hasilUji);
						//System.out.println(hasilChar.get(0));
					}
					else{
						int range1 = Integer.parseInt(tfRange1.getText());
						int range2 = Integer.parseInt(tfRange2.getText());
						for(int i = range1; i <= range2; i++){
							//System.out.println(i);
							euclideanDistance2(i);
							hasilUji += hasilChar.get(x);
							x++;
						}
						for(int i = range1; i <= range2; i++){
							int y = labelcropV[i].getY();
							if(y1 == y){
								//System.out.print(hasilChar.get(x));
								x++;
							}
							else{
								//System.out.println(hasilChar.get(x));
								x++;
							}
							y1 = y;
						}
						arrayString = hasilUji.split(" ");
						for (int i = 0; i < arrayString.length; i++) {
							if(!arrayString[i].equals("")){
								System.out.println(arrayString[i]);
							}
						}
					}	
				} catch (ClassNotFoundException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				hasilChar.clear();
			}
		});
		
		btnLevenshtein.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				hitung();
			}
		});
		
		Database.selectIdLeven(Database.getConnection());
		Database.selectString(Database.getConnection());
		croppingVertikal();
		
	}
}
