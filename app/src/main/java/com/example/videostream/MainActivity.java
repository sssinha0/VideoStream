package com.example.videostream;

import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.content.AsyncTaskLoader;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class MainActivity extends YouTubeBaseActivity {
    VideoView newvideo;
    TextView first,last;
    ImageView play;
    ProgressBar buffer;
    ProgressBar playpro;
    Uri videouri;
    Boolean ispalying;
    int duration=0;
    int current=0;
    YouTubePlayerView youTubePlayerView;
    Button youtubeplay;
    YouTubePlayer.OnInitializedListener onInitializedListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ispalying=false;
        youtubeplay=findViewById(R.id.playyoutube);
        newvideo=findViewById(R.id.videoView);
        first=findViewById(R.id.currenttimer);
        last=findViewById(R.id.endsong);
        play=findViewById(R.id.play);
        buffer=findViewById(R.id.bufferpro);
        playpro=findViewById(R.id.songpro);
        youTubePlayerView=findViewById(R.id.youtube);
        onInitializedListener=new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                youTubePlayer.loadVideo("SqWyg4HE2Ac");
            }

            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

            }
        };

        youtubeplay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                youTubePlayerView.initialize("AIzaSyDW6unJuIxyxRSUQw0JD8yv1GwbBDRCpMw",onInitializedListener);
            }
        });
        playpro.setMax(18);
        videouri=Uri.parse("https://firebasestorage.googleapis.com/v0/b/videostream-ad46d.appspot.com/o/rit%20(2).mp4?alt=media&token=bcef1ab3-d002-4e92-a1db-a94c4b546e70");
        newvideo.setVideoURI(videouri);
        newvideo.requestFocus();
        newvideo.start();
        ispalying=true;
        play.setImageResource(R.drawable.ic_baseline_pause_24);
        newvideo.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                duration=mp.getDuration()/1000;
                String ss=String.format("%02d:%02d",duration/60,duration % 60);
                last.setText(ss);
            }
        });
        newvideo.setOnInfoListener(new MediaPlayer.OnInfoListener() {
            @Override
            public boolean onInfo(MediaPlayer mp, int what, int extra) {
                if(what==mp.MEDIA_INFO_BUFFERING_START)
                    buffer.setVisibility(View.VISIBLE);
                else
                    buffer.setVisibility(View.INVISIBLE);
                return false;
            }
        });
        new VieoProgress().execute();
        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ispalying){
                    newvideo.pause();
                    ispalying=false;
                    play.setImageResource(R.drawable.ic_baseline_play_arrow_24);

                }else{
                    newvideo.start();
                    ispalying=true;
                    play.setImageResource(R.drawable.ic_baseline_pause_24);
                }
            }
        });

    }

    @Override
    protected void onStop() {
        super.onStop();
        ispalying=false;
    }

    public class VieoProgress extends AsyncTask<Void,Integer,Void>{

       @Override
       protected Void doInBackground(Void... voids) {
           do {
               if(ispalying) {
                   current = newvideo.getCurrentPosition() / 1000;
                   publishProgress(current);
               }

           }while (playpro.getProgress()<=18);
           return null;
       }

       @Override
       protected void onProgressUpdate(Integer... values) {
           super.onProgressUpdate(values);
           try {
               int currentpostion=values[0]*100/duration;
               playpro.setProgress(values[0]);
//               publishProgress(currentpostion);
               String ss=String.format("%02d:%02d",values[0]/60,values[0] % 60);
               first.setText(ss);
           }catch (Exception e){

           }

       }
   }
}