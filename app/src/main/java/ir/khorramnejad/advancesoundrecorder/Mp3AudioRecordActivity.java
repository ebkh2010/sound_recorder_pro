/*
 * Copyright (C) 2016 Naman Dwivedi
 *
 * Licensed under the GNU General Public License v3
 *
 * This is free software: you can redistribute it and/or modify it
 * under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or (at your option) any later version.
 *
 * This software is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details.
 */

package ir.khorramnejad.advancesoundrecorder;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioRecord;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.Timer;
import java.util.TimerTask;

import ir.khorramnejad.androidlame.AndroidLame;
import ir.khorramnejad.waveform.WaveformView;

import static ir.khorramnejad.advancesoundrecorder.G.dirName;


public class Mp3AudioRecordActivity extends AppCompatActivity {


    int min=0,hur=0;
    int sec=0,dsec=0,btnRecId=0;
    Timer timer;
    Runnable runTimer;

    int fileCunter=0;

    boolean isRecording = false,
            timerPuse=false;


    AudioRecord audioRecord;
    AndroidLame androidLame;
    FileOutputStream outputStream;
    ImageView btnRec, btnSetting;
    String name="";


//    LogFragment logFragment;
    TextView statusText, dis_hr , dis_min ,dis_sec , dis_dsec ;
    TextView File_name,
            Samplerate,Chanels,BitDepth,
            Mp3_Bitrate,Codec_Quality,LPF,HPF,Comment
            ;
    private WaveformView mRealtimeWaveformView;
    private RecordingThread mRecordingThread;
    private static final int REQUEST_RECORD_AUDIO = 13;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(ir.khorramnejad.advancesoundrecorder.R.layout.activity_audio_record);


        btnRec          = (ImageView) findViewById(ir.khorramnejad.advancesoundrecorder.R.id.btn_rec);
        btnSetting      = (ImageView) findViewById(ir.khorramnejad.advancesoundrecorder.R.id.btn_setting);

//        statusText      = (TextView) findViewById(R.id.statusText);

        dis_hr          = (TextView) findViewById(ir.khorramnejad.advancesoundrecorder.R.id.hr);
        dis_min         = (TextView) findViewById(ir.khorramnejad.advancesoundrecorder.R.id.min);
        dis_sec         = (TextView) findViewById(ir.khorramnejad.advancesoundrecorder.R.id.sec);
        dis_dsec        = (TextView) findViewById(ir.khorramnejad.advancesoundrecorder.R.id.dsec);

        File_name       = (TextView) findViewById(ir.khorramnejad.advancesoundrecorder.R.id.record_info);
//        Samplerate      = (TextView) findViewById(R.id.samplerate);
//        Chanels         = (TextView) findViewById(R.id.chanels);
//        Mp3_Bitrate     = (TextView) findViewById(R.id.mp3Bitrate);
//        Codec_Quality   = (TextView) findViewById(R.id.quality);
//        LPF             = (TextView) findViewById(R.id.LPF);
//        HPF             = (TextView) findViewById(R.id.HPF);
//        Comment         = (TextView) findViewById(R.id.comment);
//        BitDepth        = (TextView) findViewById(R.id.bitDepth);

        mRealtimeWaveformView = (WaveformView) findViewById(R.id.waveformView);
        mRecordingThread = new RecordingThread(new AudioDataReceivedListener() {
            @Override
            public void onAudioDataReceived(short[] data) {
                mRealtimeWaveformView.setSamples(data);
            }
        });

        fileNameCreat();
        updateNote();


        File_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File_name.setText(RecordingSettingActivity.test);
            }
        });

        btnRec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!isRecording) {
                    fileNameCreat();
                    updateNote();
                    new Thread() {
                        @Override
                        public void run() {
                            isRecording = true;
                            startAudioRecordingSafe();
                        }
                    }.start();
                    timer= new Timer();
                    timerPuse=false;
                    hur=0;min=0;sec=0;dsec=0;
                    startTimer(timer);
                    btnRec.setImageResource(R.drawable.icn_stop1);
                    btnRecId=1;
                    btnSetting.setEnabled(false);
                    btnSetting.setImageResource(R.drawable.icn_setting_dis);

                }
                else
                {
                    stopRecording();
                    btnSetting.setEnabled(true);
                    btnSetting.setImageResource(R.drawable.icn_setting);
                }
            }
        });

        btnSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Mp3AudioRecordActivity.this,
                        RecordingSettingActivity.class);
                startActivity(intent);
                finish();
            }
        });

    }

    //
//    private void startRecording() {
//
////        fileNameCreat();
//        G.minBuffer = AudioRecord.getMinBufferSize(G.inSamplerate, AudioFormat.CHANNEL_IN_MONO,
//                AudioFormat.ENCODING_PCM_16BIT);
//
//
//            audioRecord = new AudioRecord(
//                    MediaRecorder.AudioSource.MIC,
//                    G.inSamplerate,
//                    AudioFormat.CHANNEL_IN_MONO,
//                    AudioFormat.ENCODING_PCM_16BIT,
//                    G.minBuffer * 2);
//
//
//
//        //5 seconds data
//        short[] buffer = new short[G.inSamplerate * 2 * 5];
//
//        // 'mp3buf' should be at least 7200 bytes long
//        // to hold all possible emitted data.
//        byte[] mp3buffer = new byte[(int) (7200 + buffer.length * 2 * 1.25)];
//
//        try {
//            outputStream = new FileOutputStream(new File(G.filePath));
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//        }
//
//        androidLame = new LameBuilder()
//                .setVbrMode(VBR_OFF)
//                .setInSampleRate(G.inSamplerate)
//                .setOutChannels(G.inChanels)
//                .setOutBitrate(G.inMp3Bitrate)
//                .setOutSampleRate(G.inSamplerate)
//                .setId3tagComment(G.inComment)
//                .setId3tagAlbum(G.inAlbum)
//                .setId3tagTitle(G.inTitle)
//                .setId3tagArtist(G.inArtist)
//                .setQuality(G.inQuality)
////                .setLowpassFreqency(inLowFrq)
////                .setHighpassFreqency(inHighFrq)
//                .build();
//
////        updateStatus("Recording...");
//        if (audioRecord.getState()== AudioRecord.STATE_INITIALIZED)
//        audioRecord.startRecording();
//        else
//            Toast.makeText(this, "Please check the settings!",
//                    Toast.LENGTH_SHORT).show();
//
//        int bytesRead = 0;
//
//        while (isRecording) {
//
//            bytesRead = audioRecord.read(buffer, 0, G.minBuffer);
//            if (bytesRead > 0) {
//
//                int bytesEncoded = androidLame.encode(buffer, buffer, bytesRead, mp3buffer);
//                if (bytesEncoded > 0) {
//                    try {
//                        outputStream.write(mp3buffer, 0, bytesEncoded);
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        }
//
////        updateStatus("Recording stopped");
//
////        addLog("flushing final mp3buffer");
//        int outputMp3buf = androidLame.flush(mp3buffer);
////        addLog("flushed " + outputMp3buf + " bytes");
//
//        if (outputMp3buf > 0) {
//            try {
////                addLog("writing final mp3buffer to outputstream");
//                outputStream.write(mp3buffer, 0, outputMp3buf);
////                addLog("closing output stream");
//                outputStream.close();
////                updateStatus("Output recording saved in " + filePath);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//
////        addLog("releasing audio recorder");
//        audioRecord.stop();
//        audioRecord.release();
//
////        addLog("closing android lame");
//        androidLame.close();
//
//        isRecording = false;
//    }

    private void stopRecording(){
        timerPuse = true;
        if (isRecording) timer.cancel();
        isRecording = false;
        btnRec.setImageResource(R.drawable.icn_rec);
        btnRecId=0;
        mRecordingThread.stopRecording();
    }

    //
//    private void updateStatus(final String status) {
//        runOnUiThread(new Runnable() {
//            @Override
//            public void run() {
//                statusText.setText(status);
//                File_name.setText("File Name: " + name);
//            }
//        });
//    }

    private void startTimer(Timer t){

        t.scheduleAtFixedRate(new TimerTask(){


            @Override
            public void run() {

                runOnUiThread(new Runnable() {
                    public void run() {

                        if (!timerPuse)
                            dsec++;
                        if (dsec>=100){
                            dsec=0;
                            sec++;
                            if (btnRecId==0){
                                btnRec.setImageResource(R.drawable.icn_stop1);
                                btnRecId=1;
                            } else {
                                btnRec.setImageResource(R.drawable.icn_stop2);
                                btnRecId=0;
                            }
                        }

                        if (sec>=60) {
                            sec=0;
                            min++;

                        }
                        if (min>=60){
                            min=0;
                            hur++;
                        }

                        dis_hr.setText(String.valueOf(hur));
                        dis_min.setText(String.valueOf(min));
                        dis_sec.setText(String.valueOf(sec));
                        dis_dsec.setText(String.valueOf(dsec));
                    }
                });

            }

        }, 10, 10);

    }

    public void  updateNote(){
        File_name.setText("../"+dirName+"/"+name +".mp3");
//        Samplerate.setText("Sample Rate: "+String.valueOf(inSamplerate));
//        Mp3_Bitrate.setText("MP3 CBR Bitrate: " + String.valueOf(inMp3Bitrate));
//        BitDepth.setText("Bit Depth: "+ String.valueOf(inBitDept)+" Bit");
//        Comment.setText("File Comment: " + inComment);
//        if (inChanels==1)
//            Chanels.setText("File is Mono.");
//        else
//            Chanels.setText("File is Stereo.");
//        if (inQuality<=3)
//            Codec_Quality.setText("High Quality Coding.");
//        else if (inQuality>3 && inQuality<6)
//            Codec_Quality.setText("Normal Quality Coding");
//        else if (inQuality>6)
//            Codec_Quality.setText("Low Quality Coding");
//        if (inLowFrq!=0)
//            LPF.setText("Low Pass Filter Frequency: " + inLowFrq);
//        else
//            LPF.setText("");
//        if (inHighFrq!=0)
//            HPF.setText("High Pass Filter Frequency: " + inHighFrq);
//        else
//            HPF.setText("");
    }

    private void fileNameCreat(){

        File audio = new File(G.REC_DIR + "/" + G.FILE_NAME +".mp3");
        name= G.FILE_NAME;
        while (audio.exists()) {
            fileCunter++;
            name = G.FILE_NAME + "(" + fileCunter + ")";
            audio = new File(G.REC_DIR + "/" + name +".mp3");
        }
            G.filePath = G.REC_DIR + "/" + name + ".mp3";
    }

    private void startAudioRecordingSafe() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.RECORD_AUDIO)
                == PackageManager.PERMISSION_GRANTED)
        {
            mRecordingThread.startRecording();
        } else {
            requestMicrophonePermission();
        }
    }

    private void requestMicrophonePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.RECORD_AUDIO)) {
            // Show dialog explaining why we need record audio
            Snackbar.make(mRealtimeWaveformView, "Microphone access is required in order to record audio",
                    Snackbar.LENGTH_INDEFINITE).setAction("OK", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ActivityCompat.requestPermissions(Mp3AudioRecordActivity.this, new String[]{
                            android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
                }
            }).show();
        } else {
            ActivityCompat.requestPermissions(Mp3AudioRecordActivity.this, new String[]{
                    android.Manifest.permission.RECORD_AUDIO}, REQUEST_RECORD_AUDIO);
        }
    }

    public void onStop(){
        super.onStop();
       stopRecording();

    }

    public void onPause(){
        super.onPause();
        stopRecording();
    }

    public void onDestroy(){
        super.onDestroy();
        stopRecording();
    }

}
