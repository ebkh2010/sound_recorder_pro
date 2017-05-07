package ir.khorramnejad.soundrecorderpro;

import android.app.Application;
import android.content.Context;
import android.os.Environment;
import android.view.LayoutInflater;

import java.io.File;

/**
 * Created by ebi on 4/28/17.
 */

public class G extends Application {

    public static Context               	context;
    public static LayoutInflater        	inflater;
    public static  int  minBuffer,
                        inSamplerate = 44100,
                        inChanels =1,
                        inMp3Bitrate =128,
                        inQuality =5,//0-9 zero is best
                        inLowFrq=0,//frequncy of low pass filter in Hr. defult is zero
                        inHighFrq=0;//frequncy of high pass filter in Hr. defult is zero
//                        inBitDept=16;
    public static String    inComment="",
                            inAlbum="",
                            inTitle="Recorded by Sound Recorder Pro",
                            inArtist="";
    public static String REC_DIR = Environment.getExternalStorageDirectory() + "/SoundRecorderPro";
    public static String FILE_NAME = "Audio";
    public static String filePath = REC_DIR + "/" + FILE_NAME +".mp3" ;

    @Override
    public void onCreate() {
        super.onCreate();

        new File(REC_DIR).mkdir();
        filePath = REC_DIR + "/" + FILE_NAME +".mp3" ;

        context = getApplicationContext();
        inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }
}
