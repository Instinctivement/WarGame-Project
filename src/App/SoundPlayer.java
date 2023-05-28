package App;


import javax.sound.sampled.*;

import javax.sound.sampled.*;

public class SoundPlayer implements Runnable {
    private String soundFilePath;

    @Override
    public void run() {
        try {
            while (true) {
                // Create an AudioInputStream from the sound file
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(SoundPlayer.class.getResource("Wargame_Sound.wav"));

                // Get the audio format of the sound file
                AudioFormat audioFormat = audioInputStream.getFormat();

                // Create a DataLine.Info object with the desired format
                DataLine.Info dataLineInfo = new DataLine.Info(SourceDataLine.class, audioFormat);

                // Open the data line for playback
                SourceDataLine sourceDataLine = (SourceDataLine) AudioSystem.getLine(dataLineInfo);
                sourceDataLine.open(audioFormat);

                // Start playing the sound
                sourceDataLine.start();

                // Create a buffer to read data from the audio input stream
                byte[] buffer = new byte[4096];
                int bytesRead = 0;

                // Read from the audio input stream and write to the data line
                while ((bytesRead = audioInputStream.read(buffer)) != -1) {
                    sourceDataLine.write(buffer, 0, bytesRead);
                }

                // Clean up resources
                sourceDataLine.drain();
                sourceDataLine.close();
                audioInputStream.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

