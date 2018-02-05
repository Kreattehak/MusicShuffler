# MusicShuffler

This app can shuffle your music, it does it by adding a shuffle prefix.
Shuffle prefix is a number from 1 to number of files in your folder (inclusive!). 

Every time you run it, your music files get a new prefix.

## Getting Started
Simply download the MusicShuffler java file, compile it and run it.

Be aware that for properly work this app needs you to provide a path to your music folder.
```
    Let's assume that your working directory looks exactly the same as on this github page
    So you are in '../src/main/java'
    
    First you need to compile
    javac pl/home/MusicShuffler.java
    
    Then you need to run it and provide a path
    java pl.home.MusicShuffler C:\MusicFolder
```

## Limitations
Limitations which this app has are driven by radio in my car.
* No subfolders (sadly my radio doesn't read any music files in subfolders).
* You need to provide a path, it can't scan automatically for music.
* Name of your music files mustn't begin with a number, it's related to how this app clean prefixes on every new launch.


## How this app was created?
I have an old BP radio in my car, when you try to play songs with shuffle on it takes
horrendous amount of time to select one song, (for less than 200 songs, change time of a song
can take even to one minute, it depends on how far this song is in a list). 

The order in which my radio plays songs is dictated not by a song and their singer names in file tags,
but simply by a name of the file.

After driving my car and hearing the same first songs over and over again, without possibility to change it
in some acceptable time and after some information I gathered about how this radio works
I was enlightened with an idea: "How simple would it be to just add random numbers as prefix to file name".

"But why won't you simply buy a new, faster radio?". 
I have a fondness for this radio, especially for it's orange lcd.

There is also one of the downsides that i forgot to mention:

Every time you want to generate a new prefix you need to run this app.
(After few times you begin to create a coupling between you and this app and I hope it's a loose coupling.)

## Disclaimer
Music files that are used in tests are not really music files.
They are just some random char sequences saved as `.mp3`.

Sadly you can't listen to those files.