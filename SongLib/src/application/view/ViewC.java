package application.view;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.Scanner;



import application.view.Song;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;

public class ViewC implements Initializable{
	ArrayList<Song> Lib = new ArrayList<Song>();
	@FXML Button Add;
	@FXML Button Delete;
	@FXML Button Edit;
	@FXML Button Clear;
	@FXML Button Confirm;
	@FXML TextArea name;
	@FXML TextArea artist;
	@FXML TextArea album;
	@FXML TextArea year;
	@FXML Label Selected;
	@FXML Label Message;
	@FXML ListView<String> table;
	int globalint = 0;
	
	ObservableList<String> list = FXCollections.observableArrayList();
	
	public ObservableList<String> getObservableList(){
		ObservableList<String> l = FXCollections.observableArrayList();
		
		for(int i = 0; i < Lib.size(); i++){
			l.add(Lib.get(i).name);
		}
		return l;
	}
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		Clear.setText("Cancel/Clear");
		
		try {
			load();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		list = getObservableList();
		table.setItems(list);
		if(list.size() != 0){
		table.getSelectionModel().selectFirst();
		Selected.setText("Song Selected: "+Lib.get(0).name+" By: "+Lib.get(0).artist+" Album: "+Lib.get(0).album+" Year: "+Lib.get(0).year);
		}
		table.getSelectionModel().selectedItemProperty().addListener(
	            new ChangeListener<String>() {
	                public void changed(ObservableValue<? extends String> ov, 
	                    String old, String selected) {
	                        int g = list.indexOf(selected);
	                        if(g != -1){
	                        Selected.setText("Song Selected: "+Lib.get(g).name+" By: "+Lib.get(g).artist+" Album: "+Lib.get(g).album+" Year: "+Lib.get(g).year);
	                        
	                        name.setText(Lib.get(g).name);
	                        artist.setText(Lib.get(g).artist);
	                     album.setText(Lib.get(g).album);
	                        year.setText(Lib.get(g).year);
	                        
	                        
	                        }
	                }
	        });
	}
	
	public void click(ActionEvent e) throws IOException{
		Button x = (Button) e.getSource();
		
		if(x == Add){
			Delete.setDisable(true);
			Edit.setDisable(true);
			Message.setText("Adding mode");
		}
		if(x == Delete){
			Add.setDisable(true);
			Edit.setDisable(true);
			Message.setText("Deleting mode");
		}
		if(x == Edit){
			Delete.setDisable(true);
			Add.setDisable(true);
			Message.setText("Editing mode");
		}
		if(x == Clear){
			Delete.setDisable(false);
			Edit.setDisable(false);
			Add.setDisable(false);
			name.setText("");
			artist.setText("");
			album.setText("");
			year.setText("");
			Message.setText("");
		}
		if(x == Confirm){
			if(Add.isDisabled() == false && Delete.isDisabled() == false && Edit.isDisabled() == false){
				Message.setText("");
				return;
			}
			//actions happen here
			if(Add.isDisabled() == false){
				String Name = name.getText().toLowerCase();
				String Artist = artist.getText().toLowerCase();
				String Album = album.getText().toLowerCase().trim();
				String Year = year.getText().toLowerCase().trim();
				if(Name.length() == 0 || Artist.length() == 0){
					Message.setText("Song must have a name and artist");
					return;
				}
				boolean r = addSong(Name, Artist, Album, Year);
				if(r){
					list = getObservableList();
					table.setItems(list); 
					Message.setText("Song added: "+Name+" By: "+Artist+" Album: "+Album+" Year: "+Year);
					table.getSelectionModel().select(globalint);
					Selected.setText("Song Selected: "+Lib.get(globalint).name+" By: "+Lib.get(globalint).artist+" Album: "+Lib.get(globalint).album+" Year: "+Lib.get(globalint).year);
					
				}else{
					Message.setText("A Song with the same name and artist may already exist");
					
				}
				write();
				Delete.setDisable(false);
				Edit.setDisable(false);
				table.getSelectionModel().selectedItemProperty().addListener(
			            new ChangeListener<String>() {
			                public void changed(ObservableValue<? extends String> ov, 
			                    String old, String selected) {
			                        int g = list.indexOf(selected);
			                        if(g != -1){
			                        Selected.setText("Song Selected: "+Lib.get(g).name+" By: "+Lib.get(g).artist+" Album: "+Lib.get(g).album+" Year: "+Lib.get(g).year);
			                        
			                        name.setText(Lib.get(g).name);
			                        artist.setText(Lib.get(g).artist);
			                        album.setText(Lib.get(g).album);
			                        year.setText(Lib.get(g).year);
			                      
			                        
			                        }
			                }
			        });
				return;
			}
			if(Delete.isDisabled() == false){
				String Name = name.getText().trim().toLowerCase();
				String Artist = artist.getText().trim().toLowerCase();
				
				if(Name.length() == 0 || Artist.length() == 0){
					Message.setText("Song must have a name and artist");
					return;
				}
				boolean r = deleteSong(Name, Artist);
				if(r){
					list = getObservableList();
					table.setItems(list);
					Message.setText("Song deleted: "+Name+" By: "+Artist);
					if(Lib.size() != 0){
						if(globalint == Lib.size()){
							globalint--;
						}
					table.getSelectionModel().select(globalint);
					Selected.setText("Song Selected: "+Lib.get(globalint).name+" By: "+Lib.get(globalint).artist+" Album: "+Lib.get(globalint).album+" Year: "+Lib.get(globalint).year);
					}
				}else{
					Message.setText("Failed to delete Song: "+Name+" By: "+Artist);
					
				}
				write();
				Add.setDisable(false);
				Edit.setDisable(false);
				table.getSelectionModel().selectedItemProperty().addListener(
			            new ChangeListener<String>() {
			                public void changed(ObservableValue<? extends String> ov, 
			                    String old, String selected) {
			                        int g = list.indexOf(selected);
			                        if(g != -1){
			                        Selected.setText("Song Selected: "+Lib.get(g).name+" By: "+Lib.get(g).artist+" Album: "+Lib.get(g).album+" Year: "+Lib.get(g).year);
			                        
			                        name.setText(Lib.get(g).name);
			                        artist.setText(Lib.get(g).artist);
			                        album.setText(Lib.get(g).album);
			                        year.setText(Lib.get(g).year);
			                       
			                        
			                        }
			                }
			        });
				return;
			}
			if(Edit.isDisabled() == false){
				String Name = name.getText().toLowerCase().trim();
				String Artist = artist.getText().toLowerCase().trim();
				String Album = album.getText().toLowerCase().trim();
				String Year = year.getText().toLowerCase().trim();
				
				if(Name.length() == 0 || Artist.length() == 0){
					Message.setText("Song must have a name and artist");
					return;
				}
				int index = table.getSelectionModel().getSelectedIndex();
				if(index == -1){
					Message.setText("To Edit select a song from the list");
					return;
				}
				Song old = Lib.get(index);
				Song newsong = new Song(Name, Artist, Album, Year);
				boolean r = editSong(old, newsong);
				if(r){
					Message.setText("Edited Song: "+Name+" By: "+Artist);
					list = getObservableList();
					table.setItems(list);
					table.getSelectionModel().select(globalint);
					Selected.setText("Song Selected: "+Lib.get(globalint).name+" By: "+Lib.get(globalint).artist+" Album: "+Lib.get(globalint).album+" Year: "+Lib.get(globalint).year);
				
				}else{
					Message.setText("Failed to Edit Song: "+Name+" By: "+Artist);
				}
				write();
				Delete.setDisable(false);
				Add.setDisable(false);
				table.getSelectionModel().selectedItemProperty().addListener(
			            new ChangeListener<String>() {
			                public void changed(ObservableValue<? extends String> ov, 
			                    String old, String selected) {
			                        int g = list.indexOf(selected);
			                        if(g != -1){
			                        Selected.setText("Song Selected: "+Lib.get(g).name+" By: "+Lib.get(g).artist+" Album: "+Lib.get(g).album+" Year: "+Lib.get(g).year);
			                        
			                        name.setText(Lib.get(g).name);
			                        artist.setText(Lib.get(g).artist);
			                        album.setText(Lib.get(g).album);
			                        year.setText(Lib.get(g).year);
			                        
			                        
			                        }
			                }
			        });
				return;
			}
			
		}
	}
	public void load() throws FileNotFoundException{
		Scanner c = new Scanner(new File("Backend.txt"));
		
		while(c.hasNextLine()){
			String artist2 = "";
			String album2 = "";
			String year2 = "";
			String line = c.nextLine();
			int a = line.indexOf("+");
			String name2 = line.substring(0, a);
			int b = line.indexOf("+", a+1);
			if(b == -1){
				artist2 = line.substring(a+1);
				Lib.add(new Song(name2, artist2, "", ""));
				continue;
			}else{
				artist2 = line.substring(a+1, b);
			}
			int d = line.indexOf("+", b+1);
			if(d == -1){
				
				if(".".compareToIgnoreCase(line.substring(b+1,b+2)) == 0){
					String ya = line.substring(b+2);
					
				Lib.add(new Song(name2, artist2, "", ya));
				
				continue;
				}else{
					String y = line.substring(b+1);
					Lib.add(new Song(name2, artist2, "", y));
				
				continue;
				}
			}else{
				album2 = line.substring(b+1, d);
				year2 = line.substring(d+1);
				Lib.add(new Song(name2, artist2, album2, year2));
			}
			
		}
		c.close();
	}
	public void write() throws IOException{
		FileWriter file = new FileWriter("Backend.txt");
		PrintWriter writer = new PrintWriter(file);
		int i = 0;
		while(i != Lib.size()){
			Song temp = Lib.get(i);
			String NAME = temp.name;
			String ARTIST = temp.artist;
			String ALBUM = temp.album;
			String YEAR = temp.year;
			i++;
			if(ALBUM == "" && YEAR == ""){
				writer.println(NAME+"+"+ARTIST);
			}else if(ALBUM != "" && YEAR == ""){
				writer.println(NAME+"+"+ARTIST+"+."+ALBUM);
			}else if(ALBUM == "" && YEAR != ""){
				writer.println(NAME+"+"+ARTIST+"+"+YEAR);
			}else{
				writer.println(NAME+"+"+ARTIST+"+"+ALBUM+"+"+YEAR);
			}
			
		}
		writer.close();

	}
	/*All songs are stored in an arraylist call Lib, 
	 * addSong should check if a song with the same name and artist exists
	 * if it does return false
	 * if not then add it to the arraylist Lib in the right position
	 * deleteSong should check if a song with the same name and artist exists
	 * if it does just delete and arraylist will do the work of shifting
	 * and return true, if not exists than return false
	 * editSong should check(name, artist) if oldsong exists in Lib if not return false
	 * check(name, artist) if new song exists in Lib if it does return false
	 * if not than that remove oldsong and add newsong in the correct position and return true
	*/
	public boolean addSong(String NAME, String ARTIST, String ALBUM, String YEAR){
		NAME = NAME.toLowerCase();
		ARTIST = ARTIST.toLowerCase();
		ALBUM = ALBUM.toLowerCase();
		YEAR = YEAR.toLowerCase();
		if(Lib.size() == 0){
			Lib.add(new Song (NAME,ARTIST,ALBUM,YEAR));
			setint(0);
			return true;
		}
		for(int i = 0; i< Lib.size(); i++){
			if(Lib.get(i).name.compareTo(NAME) == 0 && Lib.get(i).artist.compareTo(ARTIST)==0){
					//same song and artist is being added, return false
					return false;
				}
		}
		for(int i = 0; i < Lib.size(); i++){
			if(Lib.get(i).name.compareTo(NAME) < 0){
				continue;
			}else{
				Lib.add(i, new Song(NAME,ARTIST,ALBUM,YEAR));
				setint(i);
				return true;
			}
		}
		
		//reached end of list
		Lib.add(new Song (NAME,ARTIST,ALBUM,YEAR));
		setint(Lib.size()-1);
		return true;
	}

	public boolean deleteSong(String NAME, String ARTIST) {
		NAME = NAME.toLowerCase();
		ARTIST = ARTIST.toLowerCase();
		
		if (Lib.size() == 0) {
			return false;
		}
		for (int i = 0; i < Lib.size(); i++) {
			if (Lib.get(i).name.compareTo(NAME) < 0) {
				continue;
			} else if (Lib.get(i).name.compareTo(NAME) == 0) {
				if (Lib.get(i).artist.compareTo(ARTIST) == 0) {
					Lib.remove(i);
					if(Lib.size() < i){
						i = i-1;
					setint(i);
					}else if(Lib.size() == 1){
						setint(0);
					}else{
						setint(i);
					}
					return true;
				} else if (Lib.get(i).artist.compareTo(ARTIST) < 0) {
					continue;
				} else {
					// not in library
					return false;
				}
			} else {
				// not found
				return false;
			}
		}
		return false;
	}

	public boolean editSong(Song oldsong, Song newsong) {
		if (Lib.size() == 0) {
			return false;
		}
		for (int i = 0; i < Lib.size(); i++) {
			if (Lib.get(i).name.compareTo(oldsong.name) < 0) {
				continue;
			} else if (Lib.get(i).name.compareTo(oldsong.name) == 0) {
				if (Lib.get(i).artist.compareTo(oldsong.artist) == 0) {
					deleteSong(oldsong.name, oldsong.artist);
					
					boolean r = addSong(newsong.name, newsong.artist, newsong.album, newsong.year);
					if(r){
						return true;
					}
					addSong(oldsong.name, oldsong.artist, oldsong.album, oldsong.year);
					return false;
				} else if (Lib.get(i).artist.compareTo(oldsong.artist) < 0) {
					continue;
				} else {
					// not in library
					return false;
				}
			} else {
				// not found
				return false;
			}
		}
		return false;
	}
	public void setint(int s){
		globalint = s;
	}
	
}