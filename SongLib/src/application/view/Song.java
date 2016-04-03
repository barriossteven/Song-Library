package application.view;

import javafx.beans.property.SimpleStringProperty;

public class Song {

	String name;
	String artist;
	String album;
	String year;
	public Song(String name, String artist, String album, String year){
		this.name = name;
		this.artist = artist;
		this.album = album;
		this.year = year;
	}
	
}
