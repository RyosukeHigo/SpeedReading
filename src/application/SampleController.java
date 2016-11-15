package application;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
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
	private Display d;
	private ArrayList<String> NewsList = new ArrayList<>();

	@FXML
	public void onClicked(ActionEvent event){
		if(event.getSource() == Button1){
			d.setString(TextArea1.getText().replaceAll("\n", ""));
		}else if(event.getSource() == Random){
			java.util.Random rnd = new java.util.Random();
			
			String news = NewsList.get(rnd.nextInt(NewsList.size()));
			d.setString(news);
			TextArea1.setText(news);
		}
		d.setLabel(Label1);
		d.setSlider(Slider1);
		d.restart();
	}
	@FXML
	public void onDragDetected() {
		Speed.setText(String.valueOf((int)Slider1.getValue()));
	}
	@FXML
	public void onClikedPlus(){
		MinLength.setText(String.valueOf(Integer.valueOf(MinLength.getText())+1));
	}
	@FXML
	public void onClikedMinus(){
		MinLength.setText(String.valueOf(Integer.valueOf(MinLength.getText())-1));
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
		String s;
		Label l;
		Slider sld;
		Label min;
		private String tmp = "";
		private void setString(String s) {
			this.s = s;
		}
		private void setLabel(Label l){
			this.l = l;
		}
		private void setSlider(Slider sld){
			this.sld = sld;
		}
		protected Task<Void> createTask() {
			return new Task<Void>() {
				protected Void call() {
					YahooAPI API = new YahooAPI(s);
					try {
						API.textAPI();
					} catch (IOException e2) {
						// TODO 自動生成された catch ブロック
						e2.printStackTrace();
					}
					SAXParserFactory spfactory = SAXParserFactory.newInstance();
					SAXParser parser = null;
					try {
						parser = spfactory.newSAXParser();
					} catch (ParserConfigurationException | SAXException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
					SaxSample sax = new SaxSample();
					try {
						parser.parse(new File("test.xml"), sax);
					} catch (SAXException | IOException e1) {
						// TODO 自動生成された catch ブロック
						e1.printStackTrace();
					}
					final ArrayList<String> TextList = sax.getResult();
					final ArrayList<Integer> ScoreList = sax.getScore();
					for (int i=0;i<TextList.size();i++) {
						tmp = TextList.get(i);
						System.out.println(tmp);
						updateMessage(tmp);
						int l = tmp.length();
						int s = ScoreList.get(i);
						try {
							Thread.sleep((int)(540 - sld.getValue()*6 + l*10 + s*10));
						} catch (InterruptedException e) {
							// TODO 自動生成された catch ブロック
							e.printStackTrace();
						}
						//System.out.println(sld.getValue());
					}
					return null;
				}
			};
		}
	}
}
