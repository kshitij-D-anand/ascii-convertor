import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.Image;
import java.awt.Insets;
import java.awt.RenderingHints;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;
import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.UIManager;
import javax.swing.border.EmptyBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

@SuppressWarnings("serial")
public class GUI extends JFrame implements ActionListener, FocusListener, DocumentListener, ItemListener{
	
	private JPanel selection, changes, status;
	private JLabel path, output, name, line, line2, statusLabel, errorStatus, emptyStatus, approvedStatus, fontLabel, fontSizeLabel, fontColorLabel, canvasColorLabel, cellsPerGridLabel;
	private JButton browseButton, outputBrowseButton, approveButton, fontColorButton, canvasColorButton;
	private JTextField browseField, outputBrowseField, nameField;
	private ImageIcon completedIcon, warningIcon, errorIcon;
	private BufferedImage icon;
	private ButtonGroup selectionGroup, nameGroup;
	private JRadioButton merge, single, multiple, conicalName, series;
	private JCheckBox statusChecker;
	private JComboBox<String> font;
	private JSpinner fontSize, pixelateBy;
	private File inputFile, inputFolder, outputFolder, outputFile, outputImage;
	private Font[] fonts;
	private FontRenderContext frc; 
	private JColorChooser fontColor, canvasColor;
	private List<Font> monospacedFonts;
	private boolean emptyBrowseField, emptyOutputField, emptyNameField, wrongPath, wrongOutputPath, allFeildsCorrect, singleFile;
	private int universalX;
	private String[] monospacedFontNames;
	
	public GUI() {
		initialize();
		setSize();
		setLocation();
		addListeners();
		setFocusability();
		adjust();
		add();
		
		setTitle("Ascii convertor");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		setResizable(false);
		setVisible(true);
	}

	private void initialize(){
		selection = new JPanel();
		changes = new JPanel();
		status = new JPanel();
		
		//Selection panel
		selectionGroup = new ButtonGroup();
		single = new JRadioButton("Single");
		multiple = new JRadioButton("Multiple");
		
		path = new JLabel("Input:");
		browseField = new JTextField(16);
		browseButton = new JButton("Browse");
		
		output = new JLabel("Output:");
		outputBrowseField = new JTextField(16);
		outputBrowseButton = new JButton("Browse");
		
		name = new JLabel("Name:");
		nameField = new JTextField(10);
		nameGroup = new ButtonGroup();
		conicalName = new JRadioButton("Name");
		series = new JRadioButton("Series");
		
		approveButton = new JButton("Convert");
		
		line = new JLabel("|");
		merge = new JRadioButton();
		line2 = new JLabel("|");
		
		//Changes panel
		universalX = 145;
		
		fonts = GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts();
		fontLabel = new JLabel("Font:");
		font = new JComboBox<>(getMonospacedFonts());
		
		fontSizeLabel = new JLabel("Font Size:");
		fontSize = new JSpinner(new SpinnerNumberModel(1, 1, 16, 1));
		
		fontColorLabel = new JLabel("Font Color:");
		fontColorButton = new JButton("");
		
		fontColor = new JColorChooser();
		canvasColorLabel = new JLabel("Canvas color:");
		
		canvasColorButton = new JButton();
		canvasColor = new JColorChooser();
		
		cellsPerGridLabel = new JLabel("Pixelate by:");
		pixelateBy = new JSpinner(new SpinnerNumberModel(1, 1, 16, 1));
		
		//Status panel
		statusChecker = new JCheckBox("Status");
		statusLabel = new JLabel("Status:");
		errorStatus = new JLabel("");
		emptyStatus = new JLabel("");
		approvedStatus = new JLabel("");
		
		try{
			icon = ImageIO.read(this.getClass().getResourceAsStream("Icon.png"));
			completedIcon = new ImageIcon(ImageIO.read(this.getClass().getResourceAsStream("completed-icon.jpg")));
			warningIcon = new ImageIcon(ImageIO.read(this.getClass().getResourceAsStream("warning-icon.jpg")));
			errorIcon = new ImageIcon(ImageIO.read(this.getClass().getResourceAsStream("error-icon.png")));
		}catch(IOException e){System.out.println("File not found LOL!!");}
	}
	
	private void setSize(){
		setSize(300, 380);
		
		//Selection panel
		selection.setSize(300, 150);
		
		single.setSize(75, 20);
		multiple.setSize(75, 20);
		
		path.setSize(38,20);
		browseField.setSize(120,20);
		browseButton.setSize(60,19);
		
		output.setSize(45,20);
		outputBrowseField.setSize(120,20);
		outputBrowseButton.setSize(60,19);
		
		name.setSize(40,20);
		nameField.setSize(120,20);
		conicalName.setSize(60, 20);
		series.setSize(60, 20);
		
		approveButton.setSize(70,25);
		
		line.setSize(5,12);
		merge.setSize(20,20);
		line2.setSize(5,12);
		
		//Changes panel
		changes.setSize(300,150);

		fontLabel.setSize(32,20);
		font.setSize(90,22);
		
		fontSizeLabel.setSize(60,20);
		fontSize.setSize(40,18);
		
		fontColorLabel.setSize(65,20);
		fontColorButton.setSize(40,20);
		
		canvasColorLabel.setSize(82,20);
		canvasColorButton.setSize(40,20);
		
		cellsPerGridLabel.setSize(71,20);
		pixelateBy.setSize(40,20);
		
		//Status
		status.setSize(300, 40);
		statusLabel.setSize(70, 20);
		statusChecker.setSize(75, 20);
	}
	
	private void setLocation() {
		//Selection panel
		selection.setLocation(0, 0);
		
		single.setLocation(0, 0);
		multiple.setLocation(single.getX() + single.getWidth(), single.getY());
		
		path.setLocation((int)((selection.getWidth()/2)-((browseField.getWidth()+path.getWidth()+browseButton.getWidth())/2)-7), (int) ((selection.getHeight()/4)));
		browseField.setLocation((int)(path.getX()+path.getWidth()),(int)path.getY());
		browseButton.setLocation((int)(browseField.getX()+browseField.getWidth()),(int)browseField.getY());
		
		output.setLocation((int) ((selection.getWidth()/2)-((output.getWidth()+outputBrowseField.getWidth()+outputBrowseButton.getWidth())/2)-7),(int) (path.getHeight()+6+path.getY()));
		outputBrowseField.setLocation((int) (output.getX()+output.getWidth()),(int) (output.getY()));
		outputBrowseButton.setLocation((int) (outputBrowseField.getX()+outputBrowseField.getWidth()),(int) (output.getY()));
		
		name.setLocation((int) ((selection.getWidth()/2)-((name.getWidth()+nameField.getWidth())/2)-9), (int) (output.getHeight()+6+output.getY()));
		nameField.setLocation((int) (name.getX()+name.getWidth()),(int)name.getY());
		conicalName.setLocation((int) (series.getX() + series.getWidth()), name.getY());
		series.setLocation((int) (name.getX()+name.getWidth()), (int) (name.getY()));
		
		approveButton.setLocation((int) ((selection.getWidth()/2) - (approveButton.getWidth()/2)-5), (int) (name.getHeight()+7+name.getY()));
		
		line.setLocation((int) (selection.getWidth()-30-(line.getWidth()/2)),(int) (browseField.getY()+1));
		merge.setLocation((int) (line.getX()-(merge.getWidth()/2)+6),(int) (line.getY()+line.getHeight()));
		line2.setLocation((int) (selection.getWidth()-30-(line2.getWidth()/2)),(int) (merge.getY()+merge.getHeight()-1));
		
		//Changes panel
		changes.setLocation(0, selection.getHeight());
		
		fontLabel.setLocation((int) (universalX - fontLabel.getWidth()), (int) (changes.getHeight() / 5));
		font.setLocation(universalX, (int) (fontLabel.getY()));
		
		fontSizeLabel.setLocation((int) (universalX - fontSizeLabel.getWidth()), (int) (fontLabel.getY() + fontLabel.getHeight() + 5));
		fontSize.setLocation(universalX, (int) (fontSizeLabel.getY() + 2));
		
		fontColorLabel.setLocation((int) (universalX - fontColorLabel.getWidth()), (int) (fontSizeLabel.getY() + fontSizeLabel.getHeight() + 5));
		fontColorButton.setLocation(universalX, (int) (fontColorLabel.getY()));
		
		canvasColorLabel.setLocation((int) (universalX - canvasColorLabel.getWidth()), (int) (fontColorLabel.getY() + fontColorLabel.getHeight() + 5));
		canvasColorButton.setLocation(universalX, (int) (canvasColorLabel.getY()));
		
		cellsPerGridLabel.setLocation((int) (universalX - cellsPerGridLabel.getWidth()), (int) (canvasColorLabel.getY() + canvasColorLabel.getHeight() + 5));
		pixelateBy.setLocation(universalX, (int) (cellsPerGridLabel.getY()));
		
		//Status panel
		status.setLocation(0, changes.getHeight() + changes.getY());
		statusLabel.setLocation(0, 0);
		statusChecker.setLocation(selection.getWidth() - statusChecker.getWidth(), 0);
	}
	
	public void addListeners(){
		//ActionListeners
		browseButton.addActionListener(this);
		outputBrowseButton.addActionListener(this);
		approveButton.addActionListener(this);
		fontColorButton.addActionListener(this);
		canvasColorButton.addActionListener(this);
		//FocusListeners
		browseField.addFocusListener(this);
		outputBrowseField.addFocusListener(this);
		nameField.addFocusListener(this);
		//DocumentListeners
		browseField.getDocument().addDocumentListener(this);
		outputBrowseField.getDocument().addDocumentListener(this);
		nameField.getDocument().addDocumentListener(this);
		//ItemListeners
		merge.addItemListener(this);
		statusChecker.addItemListener(this);
		single.addItemListener(this);
		multiple.addItemListener(this);
	}
	
	public void setFocusability(){
		statusChecker.setFocusable(false);
		browseButton.setFocusable(false);
		outputBrowseButton.setFocusable(false);
		approveButton.setFocusable(false);
	}
	
	public void adjust(){
		EmptyBorder emptyBorder = new EmptyBorder(1, 1, 1, 1);
		//Set
		setIconImage(icon);
		font.setSelectedIndex(1);
		//Margin
		path.setBorder(emptyBorder);
		browseButton.setMargin(new Insets(1, 1, 1, 1));
		output.setBorder(emptyBorder);
		outputBrowseButton.setMargin(new Insets(1, 1, 1, 1));
		name.setBorder(emptyBorder);
		conicalName.setBorder(emptyBorder);
		series.setBorder(emptyBorder);
		approveButton.setMargin(new Insets(5, 5, 5, 5));
		line.setBorder(emptyBorder);
		merge.setBorder(emptyBorder);
		line2.setBorder(emptyBorder);
		
		fontLabel.setBorder(emptyBorder);
		font.setBorder(emptyBorder);
		fontSizeLabel.setBorder(emptyBorder);
		cellsPerGridLabel.setBorder(emptyBorder);
		//pixelateBy.setBorder(emptyBorder);
		//fontSize.setBorder(emptyBorder);
		//Color
		fontColorButton.setBackground(Color.BLACK);
		canvasColorButton.setBackground(Color.WHITE);
		//Font
		line.setFont(new Font("Arial", Font.PLAIN, 12));
		line2.setFont(new Font("Arial", Font.PLAIN, 12));
		//Layout
		setLayout(null);
		selection.setLayout(null);
		changes.setLayout(null);
		status.setLayout(null);
		//Selection
		conicalName.setSelected(true);
		single.setSelected(true);
		try{
			UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
		} catch(Exception e){}
	}
	
	public void update(){
		updateFields();
		checkAllFields();
		statusChecker();
		repaint();
	}

	public void updateFields(){
		if(merge.isSelected()){
			outputBrowseField.getDocument().removeDocumentListener(this);
			outputBrowseField.setEditable(false);
			outputBrowseField.setText(getDirectory());
			outputBrowseField.setForeground(Color.GRAY);
			outputBrowseButton.setEnabled(false);
		}else if(!merge.isSelected()){
			outputBrowseField.setEditable(true);
			outputBrowseField.setForeground(outputBrowseField.getForeground());
			outputBrowseField.getDocument().removeDocumentListener(this);
			outputBrowseField.getDocument().addDocumentListener(this);
			outputBrowseButton.setEnabled(true);
		}
	}
	
	public void checkAllFields(){
		inputFolder = new File(browseField.getText());
		inputFile = new File(browseField.getText());
		outputFolder = new File(outputBrowseField.getText());
		outputFile = new File(outputBrowseField.getText() + "\\" + nameField.getText() + ".txt");
		outputImage = new File(outputBrowseField.getText() + "\\" + nameField.getText() + ".png");
		
		singleFile = single.isSelected();
		
		wrongPath = single.isSelected() ? !inputFile.isFile() : !inputFolder.isDirectory();
		
		wrongOutputPath = !outputFolder.isDirectory();
		
		emptyBrowseField = browseField.getText().equals("") || browseField.getText().equals("Browse");
		emptyOutputField = outputBrowseField.getText().equals("") || outputBrowseField.getText().equals("Browse");
		emptyNameField = single.isSelected()?(nameField.getText().equals("") || nameField.getText().equals("FileName")):false;
		
		allFeildsCorrect = !wrongPath && outputFolder.isDirectory() && (single.isSelected()? !emptyNameField : true);
	}
	
	public String getDirectory(){
		File f = new File(browseField.getText());
		if(f.exists()){
			return f.getAbsoluteFile().getParent();
		}
		return browseField.getText();
	}
	
	public void statusChecker(){
		if(statusChecker.isSelected()){
			updateStatus();	
		} else if(!statusChecker.isSelected()){
			clearStatus("clearAll");
		}
	}
	
	public void updateStatus(){
		boolean errorBrowseField = wrongPath && !emptyBrowseField;
		boolean errorOutputField = wrongOutputPath && !emptyOutputField;
		if(emptyBrowseField || emptyOutputField || emptyNameField){
			emptyStatus.setIcon(new ImageIcon(warningIcon.getImage().getScaledInstance(12, 12, Image.SCALE_AREA_AVERAGING)));
			emptyStatus.setText("<html>Empty Fields at" + (emptyBrowseField?" <b>Input</b>":"") + (emptyOutputField?" <b>Output</b>":"") + (emptyNameField?" <b>Name</b></html>":""));
			emptyStatus.setSize(250, 12);
			emptyStatus.setForeground(new Color(226,121,0).brighter());
			emptyStatus.setFont(new Font("Arial", Font.PLAIN, 12));
			emptyStatus.setLocation(0, status.getHeight() - emptyStatus.getHeight());
		} else {
			clearStatus("clearWarnings");
		}
		
		if(errorBrowseField || errorOutputField){
			errorStatus.setIcon(new ImageIcon(errorIcon.getImage().getScaledInstance(12, 12, Image.SCALE_AREA_AVERAGING)));
			errorStatus.setText("<html>Please Enter Valid Path at" + (errorBrowseField?" <b>Input</b>":"") + (errorOutputField?" <b>Output</b></html>":""));
			errorStatus.setSize(250, 12);
			errorStatus.setForeground(Color.RED);
			errorStatus.setFont(new Font("Arial", Font.PLAIN, 12));
			errorStatus.setLocation(0, (emptyStatus.getHeight() == 0)? status.getHeight() - errorStatus.getHeight() : emptyStatus.getY() - errorStatus.getHeight());
		} else {
			clearStatus("clearError");
		}
		
		if(allFeildsCorrect){
			approvedStatus.setIcon(new ImageIcon(completedIcon.getImage().getScaledInstance(12, 12, Image.SCALE_AREA_AVERAGING)));
			approvedStatus.setText("All fields correctly filled");
			approvedStatus.setSize(250, 12);
			approvedStatus.setForeground(Color.GREEN);
			approvedStatus.setFont(new Font("Arial", Font.PLAIN, 12));
			approvedStatus.setLocation(0, status.getHeight() - approvedStatus.getHeight());
		} else {
			clearStatus("clearApproved");
		}
	}
	
	public void clearStatus(String toBeCleared){
		switch(toBeCleared){
			case "clearError" : errorStatus.setText("");
								errorStatus.setSize(0,0);
								errorStatus.setIcon(null);
								break;
			case  "clearWarnings" : emptyStatus.setText("");
									emptyStatus.setSize(0,0);
									emptyStatus.setIcon(null);
									break;
			case  "clearApproved" : approvedStatus.setText("");
									approvedStatus.setSize(0,0);
									approvedStatus.setIcon(null);
									break;
			case "clearAll" : clearStatus("clearError");
							  clearStatus("clearWarnings");
							  clearStatus("clearApproved");
							  break;
			default : System.out.println("No KeyWord found!!");
		}
		
	}
	
	private void checkSelectionType() {
		if(single.isSelected()) {
			merge.setEnabled(true);
			
			selection.remove(conicalName);
			selection.remove(series);
			name.setLocation((int) ((selection.getWidth()/2)-((name.getWidth()+nameField.getWidth())/2)-9), (int) (output.getHeight()+5+output.getY()));
			selection.add(nameField);
		} else if(multiple.isSelected()) {
			merge.setSelected(false);
			merge.setEnabled(false);
			browseField.setText("");
			outputBrowseField.setText("");
			
			name.setLocation((int) ((selection.getWidth()/2)-((name.getWidth()+series.getWidth()+conicalName.getWidth())/2)-5), (int) (output.getHeight()+5+output.getY()));
			conicalName.setLocation((int) (name.getX()+name.getWidth()+3), (int)(name.getY()) );
			series.setLocation((int) (conicalName.getX() + conicalName.getWidth()), (int) (name.getY()));
			selection.remove(nameField);
			selection.add(series);
			selection.add(conicalName);
		}
	}
	
	private String[] getMonospacedFonts() {
		monospacedFonts = new ArrayList<Font>();
		frc = new FontRenderContext(null, RenderingHints.VALUE_TEXT_ANTIALIAS_ON, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
	    
		for (Font font : fonts) {
	        Rectangle2D iBounds = font.getStringBounds("i", frc);
	        Rectangle2D mBounds = font.getStringBounds("m", frc);
	        if (iBounds.getWidth() == mBounds.getWidth()) {
	            monospacedFonts.add(font);
	        }
	    }
		
		monospacedFontNames = new String[monospacedFonts.size()];
		for(int i = 1;i < monospacedFonts.size();i++){
			monospacedFontNames[i] = monospacedFonts.get(i).getName();
		}
		return monospacedFontNames;
	}
	
	private void add() {
		selectionGroup.add(single);
		selectionGroup.add(multiple);
		
		nameGroup.add(conicalName);
		nameGroup.add(series);
		
		selection.add(single);
		selection.add(multiple);
		selection.add(statusChecker);
		selection.add(line);
		selection.add(merge);
		selection.add(line2);
		selection.add(path);
		selection.add(browseField);
		selection.add(browseButton);
		selection.add(output);
		selection.add(outputBrowseField);
		selection.add(outputBrowseButton);
		selection.add(name);
		selection.add(nameField);
		selection.add(approveButton);
		
		changes.add(fontLabel);
		changes.add(font);
		changes.add(fontSizeLabel);
		changes.add(fontSize);
		changes.add(fontColorLabel);
		changes.add(fontColorButton);
		changes.add(canvasColorLabel);
		changes.add(canvasColorButton);
		changes.add(cellsPerGridLabel);
		changes.add(pixelateBy);
		
		status.add(statusLabel);
		status.add(emptyStatus);
		status.add(errorStatus);
		status.add(approvedStatus);
		
		add(selection);
		add(changes);
		add(status);
	}

	@Override
	public void itemStateChanged(ItemEvent ie) {
		update();
		checkSelectionType();
	}

	@Override
	public void changedUpdate(DocumentEvent de) {}
	
	@Override
	public void removeUpdate(DocumentEvent de){
		update();
		if(de.getDocument()==outputBrowseField.getDocument())
			outputBrowseField.setForeground(Color.BLACK);
	}
	
	@Override
	public void insertUpdate(DocumentEvent de){
		update();
		if(de.getDocument()==outputBrowseField.getDocument())
			outputBrowseField.setForeground(Color.BLACK);
	}
	
	@Override
	public void focusGained(FocusEvent evt){
		if(browseField.getText().equals("Browse") && evt.getSource()==browseField){
			browseField.setText("");
			browseField.setForeground(Color.BLACK);
		}
		
		if(outputBrowseField.getText().equals("Browse") && evt.getSource()==outputBrowseField){
			outputBrowseField.setText("");
			outputBrowseField.setForeground(Color.BLACK);
		}
		
		if(nameField.getText().equals("FileName") && evt.getSource()==nameField){
			nameField.setText("");
			nameField.setForeground(Color.BLACK);
		}
	}

	@Override
	public void focusLost(FocusEvent evt){
		if(browseField.getText().equals("") && evt.getSource()==browseField){
			browseField.setForeground(Color.GRAY);
			browseField.setText("Browse");
		}
		
		if(outputBrowseField.getText().equals("") && evt.getSource()==outputBrowseField){
			outputBrowseField.setText("Browse");
			outputBrowseField.setForeground(Color.GRAY);
		}
		
		if(nameField.getText().equals("") && evt.getSource()==nameField){
			nameField.setForeground(Color.GRAY);
			nameField.setText("FileName");
		}
	}
	
	@SuppressWarnings("static-access")
	@Override
	public void actionPerformed(ActionEvent e){
		//Selection
		if(e.getSource()==browseButton){
			if(single.isSelected()) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.addChoosableFileFilter(new ImageFilter());
	            fileChooser.setAcceptAllFileFilterUsed(false);
				
				int response = fileChooser.showOpenDialog(null);
				
				if(response==JFileChooser.APPROVE_OPTION) {
					browseField.setForeground(Color.BLACK);
					browseField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			} else if(multiple.isSelected()) {
				JFileChooser fileChooser = new JFileChooser();
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				fileChooser.setAcceptAllFileFilterUsed(false);
				
				int response = fileChooser.showOpenDialog(null);
				
				if(response==JFileChooser.APPROVE_OPTION) {
					browseField.setForeground(Color.BLACK);
					browseField.setText(fileChooser.getSelectedFile().getAbsolutePath());
				}
			}
		}
		
		if(e.getSource()==outputBrowseButton){
			JFileChooser fileChooser = new JFileChooser();
			fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			fileChooser.setAcceptAllFileFilterUsed(false);
			
			int response = fileChooser.showOpenDialog(null);
			
			if(response==JFileChooser.APPROVE_OPTION) {
				outputBrowseField.setForeground(Color.BLACK);
				outputBrowseField.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		}
		
		if(e.getSource()==approveButton){
			if(emptyBrowseField){
				JOptionPane.showMessageDialog(this, "Enter the path!!", "Empty Fields!!", JOptionPane.WARNING_MESSAGE);
				return;
			} else if(wrongPath){
				JOptionPane.showMessageDialog(this, (singleFile? "File" : "Folder") + " doesn't exist at the entered path!!", "Incorrect Path", JOptionPane.ERROR_MESSAGE); 
				return;
			}
			
			if(emptyOutputField){
				JOptionPane.showMessageDialog(this, "Enter the path!!", "Empty Fields!!", JOptionPane.WARNING_MESSAGE);
				return;
			} else if(wrongOutputPath){
				JOptionPane.showMessageDialog(this, "Folder doesn't exist at the entered path!!", "Incorrect Path", JOptionPane.ERROR_MESSAGE);
				return;
			}
			
			if(emptyNameField){
				JOptionPane.showMessageDialog(this, "Enter the name!!", "Empty Fields!!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(fontColorButton.getBackground().equals(canvasColorButton.getBackground())) {
				JOptionPane.showMessageDialog(this, "Choose different colors for font and canvas", "Same colors!!", JOptionPane.WARNING_MESSAGE);
				return;
			}
			
			if(single.isSelected()) {
				if (inputFile.exists() && outputFolder.isDirectory()){
					Img2Ascii asciiConvertor = new Img2Ascii(outputFile, (int) pixelateBy.getValue());
					asciiConvertor.convertToAscii(inputFile);
					
					TextToImage textConvertor = new TextToImage(outputFile);
					textConvertor.setStyle(monospacedFontNames[font.getSelectedIndex()]);
					textConvertor.setSize((int)fontSize.getValue());
					textConvertor.setColor(fontColorButton.getBackground());
					textConvertor.setBackground(canvasColorButton.getBackground());
					textConvertor.convertToImage(outputImage);
					
					JOptionPane.showOptionDialog(
					this,
					"Image succesfully converted!!", 
					"Completed!!", 
					JOptionPane.DEFAULT_OPTION, 
					0, 
					new ImageIcon(completedIcon.getImage().getScaledInstance(32, 32, Image.SCALE_AREA_AVERAGING)), 
					null, 
					null);
				}
			} else if(multiple.isSelected()) {
				if (inputFolder.isDirectory() && outputFolder.isDirectory()){
					Img2Ascii asciiConvertor;
					TextToImage textConvertor;
					File filesList[] = inputFolder.listFiles();
					File textFile;
					int j = 1;
					for(File file : filesList) {
						textFile = new File(outputFolder.getAbsolutePath() + "\\" + (conicalName.isSelected()? file.getName().replaceFirst("[.][^.]+$", "") : j) + ".txt");
						asciiConvertor = new Img2Ascii(textFile, (int) pixelateBy.getValue());
						asciiConvertor.convertToAscii(file);
						
						textConvertor = new TextToImage(textFile);
						textConvertor.setStyle(monospacedFontNames[font.getSelectedIndex()]);
						textConvertor.setSize((int)fontSize.getValue());
						textConvertor.setColor(fontColorButton.getBackground());
						textConvertor.setBackground(canvasColorButton.getBackground());
						textConvertor.convertToImage(new File(outputFolder.getAbsolutePath() + "\\" + (series.isSelected()? j : file.getName().replaceFirst("[.][^.]+$", "")) + ".png"));
						j++;
					}
					
					JOptionPane.showOptionDialog(
					this,
					"Image succesfully converted!!", 
					"Completed!!", 
					JOptionPane.DEFAULT_OPTION, 
					0, 
					new ImageIcon(completedIcon.getImage().getScaledInstance(32, 32, Image.SCALE_AREA_AVERAGING)), 
					null, 
					null);
				}
			}
		}
		
		//Changes
		if(e.getSource()==fontColorButton) {
			fontColorButton.setBackground(fontColor.showDialog(null, "Font Color", Color.BLACK));
		}
		
		if(e.getSource()==canvasColorButton) {
			canvasColorButton.setBackground(canvasColor.showDialog(null, "Canvas Color", Color.WHITE));
		}
	}
}
