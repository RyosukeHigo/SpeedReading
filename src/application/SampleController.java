package application;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.Character.UnicodeBlock;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.ResourceBundle;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.SAXException;

//import com.sun.javafx.fxml.expression.Expression.Parser.Token;

import javafx.concurrent.Service;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
public class SampleController implements Initializable{
	@FXML private Button Button1;
	@FXML private Label Label1;
	@FXML private Slider Slider1;
	@FXML private Slider Slider2;
	@FXML private TextArea TextArea1;
	@FXML private Label Speed;
	@FXML private Label Speed2;
	@FXML private Button test;
	@FXML private Label MinLength;
	@FXML private Button Random;
	@FXML private CheckBox proxyButton;
	@FXML private CheckBox spaceButton;
	@FXML private CheckBox constButton;;
	@FXML private Button toNext;
	@FXML private Label bNum;
	@FXML private TextField aField;
	@FXML private TextField bField;
	@FXML private TextField dField;
	private Display d;
	private ArrayList<String> NewsList = new ArrayList<>();
	File file = null;
	static PrintWriter pw = null;
	static boolean isNext = false;
	static int sumOfB=0;
	@FXML
	public void onClicked(ActionEvent event){
		try {
			pw = new PrintWriter(new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true),"UTF-8")));
		} catch (UnsupportedEncodingException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		} catch (FileNotFoundException e1) {
			// TODO 自動生成された catch ブロック
			e1.printStackTrace();
		}
		if(event.getSource() == Button1){
			d.setString(TextArea1.getText().replaceAll("\n", ""));
		}else if(event.getSource() == Random){
			java.util.Random rnd = new java.util.Random();
			String news = NewsList.get(rnd.nextInt(NewsList.size()));
			d.setString(news);
			TextArea1.setText(news);
		}
		//Random.setDisable(true);
		//Button1.setDisable(true);
		d.setLabel(Label1);
		d.setSlider(Slider1);
		d.set2Slider(Slider2);
		d.setProxyBox(proxyButton);
		d.setSpaceBox(spaceButton);
		d.setbNum(bNum);
		d.setaField(aField);
		d.setbField(bField);
		d.setdField(dField);
		d.setConstBox(constButton);
		d.restart();
		toNext.requestFocus();
	}
	@FXML
	public void onClickedValue(ActionEvent event){
		Slider1.setDisable(!Slider1.isDisable());
		Slider2.setDisable(!Slider2.isDisable());
	}

	@FXML
	public void onDragDetected() {
		Speed.setText(String.valueOf((int)Slider1.getValue()));
		Speed2.setText(String.valueOf((int)Slider2.getValue()));
	}
	@FXML
	public void onPressed() {
		isNext = true;
		//System.out.println(isNext);
		System.out.println(sumOfB
				);
		bNum.setText(String.valueOf(sumOfB));
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		d = new Display();
		System.setProperty("file.encoding", "UTF8");
		Calendar myCal = Calendar.getInstance();
		DateFormat myFormat = new SimpleDateFormat("yyyy-MMdd-HHmmss");
		TextInputDialog userNameDialog  = new TextInputDialog( "InputYourName" );
		userNameDialog.setTitle("Your Name");
		userNameDialog.setHeaderText("Input your name!");
		String userName = userNameDialog.showAndWait().orElse("");
		String myName = myFormat.format(myCal.getTime()) + userName +".csv";
		file = new File(myName);
		try{
			File file = new File("s0-5.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file),"UTF-8"));

			String str;
			while((str = br.readLine()) != null){
				//String tmp = new String(str.getBytes("UTF-8"), "UTF-8");
				NewsList.add(str);
			}

			br.close();
		}catch(FileNotFoundException e){
			System.out.println(e);
		}catch(IOException e){
			System.out.println(e);
		}
		Label1.textProperty().bind(d.messageProperty());

		//Speed.setText(String.valueOf((int)Slider1.getValue()))
	}
	static class Display extends Service<Void> {
		String inputText;
		Label l;
		Slider sld1;
		Slider sld2;
		Label min;
		CheckBox proxyBox;
		CheckBox spaceBox;
		CheckBox constBox;
		Label bNum;
		TextField aField;
		TextField bField;
		TextField dField;
		private String displayText = "";
		private void setString(String s) {
			this.inputText = s;
		}
		private void setLabel(Label l){
			this.l = l;
		}
		private void setSlider(Slider sld){
			this.sld1 = sld;
		}
		private void set2Slider(Slider sld){
			this.sld2 = sld;
		}
		private void setProxyBox(CheckBox proxyBox){
			this.proxyBox = proxyBox;
		}
		private void setSpaceBox(CheckBox spaceBox){
			this.spaceBox = spaceBox;
		}
		private void setbNum(Label bNum){
			this.bNum = bNum;
		}
		public TextField getaField() {
			return aField;
		}
		public void setaField(TextField aField) {
			this.aField = aField;
		}
		public TextField getbField() {
			return bField;
		}
		public void setbField(TextField bField) {
			this.bField = bField;
		}
		public TextField getdField() {
			return dField;
		}
		public void setdField(TextField dField) {
			this.dField = dField;
		}

		public CheckBox getConstBox() {
			return constBox;
		}
		public void setConstBox(CheckBox constBox) {
			this.constBox = constBox;
		}
		protected Task<Void> createTask() {
			return new Task<Void>() {
				protected Void call() throws InterruptedException {
					YahooAPI API = new YahooAPI(inputText,proxyBox.isSelected());
					try {
						API.textAPI("parse");
						API.textAPI("furigana");
					} catch (IOException e2) {
						e2.printStackTrace();
					}
					SAXParserFactory spfactory = SAXParserFactory.newInstance();
					SAXParser parser = null;
					try {
						parser = spfactory.newSAXParser();
					} catch (ParserConfigurationException | SAXException e1) {
						e1.printStackTrace();
					}
					ParseSax psax = new ParseSax();
					FuriganaSax fsax = new FuriganaSax();
					try {
						parser.parse(new File("parse.xml"), psax);
						//parser.parse(new File("furigana.xml"), fsax);
					} catch (SAXException | IOException e1) {
						e1.printStackTrace();
					}
					ArrayList<String> TextList = psax.getResult();
					ArrayList<Integer> ScoreList = psax.getScore();
					ArrayList<Integer> LengthList = psax.getTextReadLength();
					double a = Double.valueOf(aField.getText());
					double b = Double.valueOf(bField.getText());
					int baseTime = (int) (Double.valueOf(dField.getText())-50*sld1.getValue());
					if(baseTime<0) baseTime = 0;
					updateMessage("■スペースキーを押してスタート■");
					if(spaceBox.isSelected()==true){
						while(true){
							if(isNext){
								isNext = false;
								break;
							}
							Thread.sleep(1);
						}

					}
					updateMessage("3");
					Thread.sleep(500);
					updateMessage("2");
					Thread.sleep(500);
					updateMessage("1");
					Thread.sleep(500);
					int displayTime = 0;
					if(constBox.isSelected()){
						int i=0;
						while(true){
							int lg = 5;
							if((i+1)*lg<inputText.length()){
								displayText = inputText.substring(lg*i, lg*(i+1));
							}else{
								displayText = inputText.substring(lg*i, inputText.length());
							}
							displayTime = (int) (1050-50*(sld2.getValue()));
							updateMessage(displayText);
							try {
								Thread.sleep(displayTime);
							} catch (InterruptedException e) {
								e.printStackTrace();
							}
							if((i+1)*lg>=inputText.length()) break;
							i++;
						}
					}else{
						for (int i=0;i<TextList.size();i++) {
							displayText = TextList.get(i);
							int kanjiNum = 0;
							for(int k=0;k<displayText.length();k++){
								if(UnicodeBlock.of(displayText.charAt(k)) == UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS){
									kanjiNum++;
								}
							}
							long start = System.currentTimeMillis();
							updateMessage(displayText);
							int textLength = displayText.length();//LengthList.get(i);
							int textScore = ScoreList.get(i);
							System.out.println(displayText + " " + textLength + " " + textScore);
							//int displayTime = (int)(baseTime + lengthWeight*textLength + scoreWeight*textScore);
							displayTime = (int)(baseTime + a*(textLength-kanjiNum) + b*kanjiNum);
							sumOfB++;
							if(spaceBox.isSelected()==true){
								while(true){
									if(isNext){
										isNext = false;
										break;
									}
									Thread.sleep(1);
								}

							}else{
								try {
									Thread.sleep(displayTime);
								} catch (InterruptedException e) {
									e.printStackTrace();
								}
							}

							//計測したい処理を記述

							long end = System.currentTimeMillis();
							System.out.println((end - start)  + "ms");
							String dicScore = "";
							if(textScore<7&&textScore>0){
								dicScore = String.valueOf(textScore);
							}
							pw.println(end - start-1+","+textLength+","+LengthList.get(i)+","+kanjiNum+","+ textScore +","+ dicScore +","+displayText);
							//System.out.println(sld.getValue());
						}
						if(spaceBox.isSelected()){
							pw.close();
						}
					}
					updateMessage("");
					return null;
				}
			};
		}
	}
}
