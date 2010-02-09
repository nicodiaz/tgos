package game.sound;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;

import com.jmex.audio.AudioSystem;
import com.jmex.audio.AudioTrack;
import com.jmex.audio.MusicTrackQueue;
import com.jmex.audio.MusicTrackQueue.RepeatType;

public class SapoSounds
{

	private ArrayList<String> tracks = new ArrayList<String>();
	private MusicTrackQueue musicQueue = AudioSystem.getSystem().getMusicQueue();

	private boolean isPlaying = false;
	
	public SapoSounds()
	{

		// The Track List
		tracks.add("sounds/maintheme.ogg");

		for (String track : tracks)
		{
			try
			{
				URL fileURL = new File(track).toURI().toURL();
				AudioTrack audioTrack = AudioSystem.getSystem().createAudioTrack(fileURL, true);
				MusicTrackQueue queue = AudioSystem.getSystem().getMusicQueue();
				queue.setCrossfadeinTime(0);
				queue.setRepeatType(RepeatType.ALL);
				queue.addTrack(audioTrack);
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
	}

	public void play()
	{
		musicQueue.play();
		isPlaying = true;
	}
	
	public void pause()
	{
		isPlaying = false;
		musicQueue.pause();
	}
	
	public void stop()
	{
		isPlaying = false;
		musicQueue.stop();
	}
	
	public boolean isPlaying()
	{
		return this.isPlaying;
	}
	
	
}
