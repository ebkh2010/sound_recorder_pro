/*
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package ir.khorramnejad.soundrecorderpro;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import ir.khorramnejad.androidlame.AndroidLame;
import ir.khorramnejad.androidlame.LameBuilder;

import static ir.khorramnejad.androidlame.LameBuilder.VbrMode.VBR_OFF;

public class RecordingThread {
    private static final String LOG_TAG = RecordingThread.class.getSimpleName();


    AndroidLame androidLame;
    FileOutputStream outputStream;

    public RecordingThread(AudioDataReceivedListener listener) {
        mListener = listener;
    }

    private boolean mShouldContinue;
    private AudioDataReceivedListener mListener;
    private Thread mThread;

    public boolean recording() {
        return mThread != null;
    }

    public void startRecording() {
        if (mThread != null)
            return;

        mShouldContinue = true;
        mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                record();
            }
        });
        mThread.start();
    }

    public void stopRecording() {
        if (mThread == null)
            return;

        mShouldContinue = false;
        mThread = null;
    }

    private void record() {
        Log.v(LOG_TAG, "Start");
        android.os.Process.setThreadPriority(android.os.Process.THREAD_PRIORITY_AUDIO);

        // buffer size in bytes
//        int bufferSize = AudioRecord.getMinBufferSize(
//                SAMPLE_RATE,
//                AudioFormat.CHANNEL_IN_MONO,
//                AudioFormat.ENCODING_PCM_16BIT);

        G.minBuffer = AudioRecord.getMinBufferSize(
                G.inSamplerate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT);

//        if (bufferSize == AudioRecord.ERROR || bufferSize == AudioRecord.ERROR_BAD_VALUE) {
//            bufferSize = SAMPLE_RATE * 2;
//        }

        if (G.minBuffer == AudioRecord.ERROR || G.minBuffer == AudioRecord.ERROR_BAD_VALUE) {
            G.minBuffer = G.inSamplerate * 2;
        }

//        short[] audioBuffer = new short[bufferSize*buferchanger];
//        short[] audioBuffer1 = new short[bufferSize/2];

        short[] audioBuffer = new short[G.minBuffer*com.newventuresoftware.waveform.WaveformView.buferchanger];
        short[] audioBuffer1 = new short[G.minBuffer/2];

        // 'mp3buf' should be at least 7200 bytes long
        // to hold all possible emitted data.
        byte[] mp3buffer = new byte[(int) (7200 + audioBuffer.length * 2 * 1.25)];


        AudioRecord record = new AudioRecord(
                MediaRecorder.AudioSource.MIC,
                G.inSamplerate,
                AudioFormat.CHANNEL_IN_MONO,
                AudioFormat.ENCODING_PCM_16BIT,
//                G.minBuffer);
                G.minBuffer*2);

        try {
            outputStream = new FileOutputStream(new File(G.filePath));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        androidLame = new LameBuilder()
                .setVbrMode(VBR_OFF)
                .setInSampleRate(G.inSamplerate)
                .setOutChannels(G.inChanels)
                .setOutBitrate(G.inMp3Bitrate)
                .setOutSampleRate(G.inSamplerate)
                .setId3tagComment(G.inComment)
                .setId3tagAlbum(G.inAlbum)
                .setId3tagTitle(G.inTitle)
                .setId3tagArtist(G.inArtist)
                .setQuality(G.inQuality)
//                .setLowpassFreqency(inLowFrq)
//                .setHighpassFreqency(inHighFrq)
                .build();

//        if (record.getState() != AudioRecord.STATE_INITIALIZED) {
//            Log.e(LOG_TAG, "Audio Record can't initialize!");
//            return;
//        }
//        record.startRecording();

//        Log.v(LOG_TAG, "Start recording");

        if (record.getState()== AudioRecord.STATE_INITIALIZED)
            record.startRecording();
//        else
//            Toast.makeText(this, "Please check the settings!",
//                    Toast.LENGTH_SHORT).show();


        long shortsRead = 0;
        while (mShouldContinue) {
            int numberOfShort = record.read(audioBuffer, 0, audioBuffer.length);
            shortsRead += numberOfShort;

            if (numberOfShort > 0) {

                int bytesEncoded = androidLame.encode(audioBuffer, audioBuffer, numberOfShort, mp3buffer);
                if (bytesEncoded > 0) {
                    try {
                        outputStream.write(mp3buffer, 0, bytesEncoded);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }

            int j=0;
            int k=0;
            for (int i=0;i<G.minBuffer*com.newventuresoftware.waveform.WaveformView.buferchanger;i++)
            {
               k++;
                if (k==com.newventuresoftware.waveform.WaveformView.buferchanger*2)
                {
                    audioBuffer1[j]=audioBuffer[i];
                    j++;
                    k=0;
                }
            }

            // Notify waveform
             mListener.onAudioDataReceived(audioBuffer1);
        }

        int outputMp3buf = androidLame.flush(mp3buffer);

        if (outputMp3buf > 0) {
            try {
                outputStream.write(mp3buffer, 0, outputMp3buf);
                outputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        record.stop();
        record.release();
        androidLame.close();

        mShouldContinue=false;

        Log.v(LOG_TAG, String.format("Recording stopped. Samples read: %d", shortsRead));
    }
}
