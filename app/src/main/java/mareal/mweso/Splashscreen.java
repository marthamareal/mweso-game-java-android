package mareal.mweso;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
//this handles the welcome screen that splashes the first time the app is started
public class Splashscreen extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);
        Thread timerThread = new Thread(){
            public void run(){
            try{
      //basing on the majority of the apps i have reduced the sleep time to 2 seconds
   sleep(2000);
}
            catch(InterruptedException e){
                e.printStackTrace();
 }
            finally{
 Intent intent = new Intent(Splashscreen.this,MwesoGame.class);
    startActivity(intent);
     }
    }
  };
        timerThread.start();
    }

    @Override
    protected void onPause() {
        super.onPause();
        finish();
    }
}
