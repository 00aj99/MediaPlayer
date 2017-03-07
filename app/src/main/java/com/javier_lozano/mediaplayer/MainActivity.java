package com.javier_lozano.mediaplayer;

import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.io.FileDescriptor;
import java.io.IOException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity  implements MediaPlayer.OnCompletionListener{
    private final int TIME = 2000;
    private MediaPlayer mediaPlayer;
    AssetFileDescriptor descriptor;
    AssetManager assetManager;
    ArrayList<String> songs = new ArrayList<>();
    TextView lbl_name ;
    int actualSong = 0;

    private ListView lista;
    private Button play ;
    MediaMetadataRetriever mmr = new MediaMetadataRetriever();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        play = (Button)  findViewById(R.id.btn_play);
        lbl_name = (TextView) findViewById(R.id.lbl_SongTitle);
        mediaPlayer = new MediaPlayer();
        assetManager = getAssets();
        songs.add("01_Love Me Do.mp3");
        songs.add("02_From Me To You.mp3");
        songs.add("03_She Loves You.mp3");
        songs.add("04_I Want To Hold Your Hand.mp3");
        songs.add("05_Can't Buy Me Love.mp3");
        songs.add("06_A Hard Day's Night.mp3");
        songs.add("07_I Feel Fine.mp3");
        songs.add("08_Eight Days A Week.mp3");
        songs.add("09_Ticket To Ride.mp3");
        songs.add("10_Help!.mp3");
        songs.add("11_Yesterday.mp3");
        songs.add("12_kiss-i-was-made-for-love-in-you.mp3");
        songs.add("13-Senbon Sakura.mp3");
        songs.add("14_Modern old Japanese.mp3");
        lista = (ListView) findViewById(R.id.lv_canciones);
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,songs);
        lista.setAdapter(adapter);
        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mediaPlayer.stop();
                initSong(songs.get(position));
                mediaPlayer.start();
            }
        });
        initSong(songs.get(actualSong));
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.start();
        play.setBackgroundResource(android.R.drawable.ic_media_pause);

    }

    private void initSong(String song) {
        try {
            mediaPlayer = new MediaPlayer();
            descriptor = assetManager.openFd(song);
            mediaPlayer.setDataSource(
                    descriptor.getFileDescriptor(),
                    descriptor.getStartOffset(),
                    descriptor.getLength());
            mediaPlayer.prepare();
            mmr.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength());


            String songTitle = mmr.extractMetadata(MediaMetadataRetriever.METADATA_KEY_TITLE);
            lbl_name.setText(songTitle);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ff (View v){
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()+TIME);
    }
    public void rw (View v){
        mediaPlayer.seekTo(mediaPlayer.getCurrentPosition()-TIME);
    }
    @Override
    protected void onPause() {
        super.onPause();
        mediaPlayer.release();
    }

    public void play(View v){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            play.setBackgroundResource(android.R.drawable.ic_media_play);
        }else {
            play.setBackgroundResource(android.R.drawable.ic_media_pause);
        mediaPlayer.start();}
    }
    public void pause(View v){
        if(mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
        }
    }
    public void stop(View v){
        mediaPlayer.stop();
        mediaPlayer.release();
        initSong(songs.get(actualSong));
        play.setBackgroundResource(android.R.drawable.ic_media_play);
    }
    public void next(View v){
        mediaPlayer.stop();
        mediaPlayer.release();
        actualSong = actualSong==songs.size()-1? 0: actualSong+1;
        initSong(songs.get(actualSong));
        mediaPlayer.start();
        play.setBackgroundResource(android.R.drawable.ic_media_pause);
    }
    public void previous(View v){
        mediaPlayer.stop();
        mediaPlayer.release();
        actualSong = actualSong==0 ? songs.size()-1: actualSong-1;
        initSong(songs.get(actualSong));
        mediaPlayer.start();
        play.setBackgroundResource(android.R.drawable.ic_media_pause);
    }

    @Override
    public void onCompletion(MediaPlayer mp) {
        mediaPlayer.stop();
        actualSong = actualSong==songs.size()-1? 0: actualSong+1;
        initSong(songs.get(actualSong));
        mediaPlayer.start();
    }
}
