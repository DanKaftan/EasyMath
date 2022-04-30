import android.content.Context;
import android.media.AudioManager;
import android.media.SoundPool;

import com.dan.kaftan.game.R;

/**
 * Created by alonk on 23/04/2018.
 */

public class SoundPlayer {
    public  static SoundPool soundPool;
    public static int hitSound;
    public static int overSound;
    public SoundPlayer(Context context){
        //soundPool (int MaxStreams, int streamType, int srcQuality)
        soundPool = new SoundPool(2, AudioManager.STREAM_MUSIC, 0);
        hitSound =soundPool.load(context, R.raw.wrong_answer_sound, 1);
    }
    public  void  playHitSound(){
       //play(int soundID, float leftVolume, float rightVolume, int priority, int loop, float rate)

        soundPool.play(hitSound, 1.0f, 1.0f, 1, 0, 1.0f);
    }

}
