package yu.cai.myview;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import yu.cai.myview.widget.ParallaxListView;

public class MainActivity extends AppCompatActivity {

    private ParallaxListView listView;
    String [] indexs = {"A","B","C","D","E","F","G","H","I","G","K","L","M","N","O","P"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }



}
