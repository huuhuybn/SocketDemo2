package vn.e.socketdemo;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.github.nkzawa.emitter.Emitter;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class MainActivity extends AppCompatActivity {


    private Socket mSocket;

    {
        try {
            mSocket = IO.socket("https://socket-io-chat.now.sh");
        } catch (URISyntaxException e) {
        }
    }


    private EditText edtMessage;
    private Button btnSend;

    private String username = "Huy Nguyen";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mSocket.emit("add user", username);

        mSocket.on("new message", onNewMessage);

        mSocket.connect();


        edtMessage = findViewById(R.id.edtMessage);
        btnSend = findViewById(R.id.btnSend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String message = edtMessage.getText().toString().trim();

                if (message.equals("")) {

                    edtMessage.setError("Pls enter message first!!!");

                    return;

                } else {

                    edtMessage.setText("");
                    // cau lenh gui message
                    mSocket.emit("new message", message);

                }

            }
        });


    }

    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String username;
                    String message;
                    try {
                        username = data.getString("username");
                        message = data.getString("message");
                    } catch (JSONException e) {
                        return;
                    }

                    Toast.makeText(MainActivity.this,
                            message, Toast.LENGTH_SHORT).show();

                    // add the message to view
                    //addMessage(username, message);
                }
            });
        }
    };


}
