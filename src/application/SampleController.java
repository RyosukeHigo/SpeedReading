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
public class SampleController implements Initializable{
	@FXML private Button Button1;
	@FXML private Label Label1;
	@FXML private Slider Slider1;
	@FXML private TextArea TextArea1;
	@FXML private Label Speed;
	@FXML private Button test;
	@FXML private Label MinLength;
	@FXML private Button Random;
	@FXML private Button slowish;
	@FXML private Button slow;
	@FXML private Button comfortable;
	@FXML private Button fastish;
	@FXML private Button fast;
	@FXML private CheckBox proxyButton;
	@FXML private CheckBox spaceButton;
	@FXML private Button toNext;
	private Display d;
	private ArrayList<String> NewsList = new ArrayList<>();
	Calendar myCal = Calendar.getInstance();
	DateFormat myFormat = new SimpleDateFormat("yyyy-MMdd-HHmmss");
	String myName = myFormat.format(myCal.getTime()) + ".csv";
	File file = new File(myName);
	static PrintWriter pw = null;
	static boolean isNext = false;
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
		d.setProxyBox(proxyButton);
		d.setSpaceBox(spaceButton);
		d.restart();
		toNext.requestFocus();
	}
	@FXML
	public void onClickedValue(ActionEvent event){
		if(event.getSource() == slow){
			pw.print(",0\n");
		}else if(event.getSource() == slowish){
			pw.print(",1\n");
		}else if(event.getSource() == comfortable){
			pw.print(",2\n");
		}else if(event.getSource() == fastish){
			pw.print(",3\n");
		}else if(event.getSource() == fast){
			pw.print(",4\n");
		}
		pw.close();
		//Random.setDisable(false);
		//Button1.setDisable(false);
	}

	@FXML
	public void onDragDetected() {
		Speed.setText(String.valueOf((int)Slider1.getValue()));
	}
	@FXML
	public void onPressed() {
		isNext = true;
		//System.out.println(isNext);
	}
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		d = new Display();
		System.setProperty("file.encoding", "UTF8");
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
		Speed.setText(String.valueOf((int)Slider1.getValue()));
	}
	static class Display extends Service<Void> {
		String inputText;
		Label l;
		Slider sld;
		Label min;
		CheckBox proxyBox;
		CheckBox spaceBox;
		private String displayText = "";
		private void setString(String s) {
			this.inputText = s;
		}
		private void setLabel(Label l){
			this.l = l;
		}
		private void setSlider(Slider sld){
			this.sld = sld;
		}
		private void setProxyBox(CheckBox proxyBox){
			this.proxyBox = proxyBox;
		}
		private void setSpaceBox(CheckBox spaceBox){
			this.spaceBox = spaceBox;
		}
		protected Task<Void> createTask() {
			return new Task<Void>() {
				protected Void call() throws InterruptedException {
					if(!spaceBox.isSelected()){
						System.out.println("OK");
					}
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
					java.util.Random rnd = new java.util.Random();
					int lengthWeight = 10*rnd.nextInt(11);//0~100の10刻み
					int scoreWeight = 10*rnd.nextInt(11);//0~50の5刻み
					int baseTime = 50*rnd.nextInt(6);
					if(spaceBox.isSelected()==false){
						pw.print(baseTime+","+lengthWeight+","+scoreWeight);
					}
					int a=0;
					for (int i=0;i<TextList.size();i++) {
						displayText = TextList.get(i);
						long start = System.currentTimeMillis();
						updateMessage(displayText);
						int textLength = displayText.length();//LengthList.get(i);
						int textScore = ScoreList.get(i);
						System.out.println(displayText + " " + textLength + " " + textScore);
						int displayTime = (int)(baseTime + lengthWeight*textLength + scoreWeight*textScore);
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
						pw.println(end - start+","+textLength+","+LengthList.get(i)+","+ textScore);
						//System.out.println(sld.getValue());
					}
					if(spaceBox.isSelected()){
						pw.close();
					}
					return null;
				}
			};
		}
	}
}
