# via-media-player-

MP3 player V2.0 with a explorer.

I have 3 classes 
	ExplorerActivity : Acticity who contains the explorer to choose the music files
	MainActivity : The acticity who manage the SeekBar.
	Mp3Service : A service who manages the MediaPlayer on the background, this has a main task of the application

I got some bugs I manage to resolve
	-When the user come back frome the ExplorerActivity on the MainActivity the SeekBar is doing weird.
	-when the user press the "Home" and return to the app, if he open a new song, the app will play 2 songs a the same time.

I tried to do a better explorer, that is why there is méthods for changing folder..
