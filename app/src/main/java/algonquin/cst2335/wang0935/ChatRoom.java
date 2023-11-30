package algonquin.cst2335.wang0935;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.snackbar.Snackbar;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import algonquin.cst2335.wang0935.databinding.ActivityChatRoomBinding;
import algonquin.cst2335.wang0935.databinding.ReceiveMessageBinding;
import algonquin.cst2335.wang0935.databinding.SentMessageBinding;

public class ChatRoom extends AppCompatActivity {
    ActivityChatRoomBinding binding;
    ArrayList<ChatMessage> messages = new ArrayList<>();

    ChatRoomViewModel chatModel ;
    ChatMessageDAO mDAO;
    private RecyclerView.Adapter myAdapter = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        chatModel = new ViewModelProvider(this).get(ChatRoomViewModel.class);
        chatModel.selectedMessage.observe(this, (selectedMessage) -> {
            MessageDetailsFragment newFragment = new MessageDetailsFragment(selectedMessage);

            FragmentManager fMgr = getSupportFragmentManager();
            FragmentTransaction tx = fMgr.beginTransaction();

            tx.addToBackStack("");
            tx.replace(R.id.frameLayout, newFragment);
            tx.commit();

        });

        MessageDatabase db = Room.databaseBuilder(getApplicationContext(), MessageDatabase.class, "databaseFileOnPhone").build();
        mDAO = db.cmDAO();

        messages = chatModel.messages.getValue();
        if(messages == null)
        {
            chatModel.messages.setValue(messages = new ArrayList<>());

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(() ->
            {
                messages.addAll( mDAO.getAllMessages() ); //Once you get the data from database

                runOnUiThread( () ->  binding.recyclerView.setAdapter( myAdapter )); //You can then load the RecyclerView
            });
        }


        //database


        binding = ActivityChatRoomBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        binding.sendButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            String inputMessage = binding.textInput.getText().toString();
            boolean sentButton = true;
            ChatMessage thisMessage = new ChatMessage(inputMessage, currentDateandTime, sentButton);
            messages.add(thisMessage);

            // clear teh previous text
            binding.textInput.setText("");
            myAdapter.notifyDataSetChanged();

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(new Runnable() {
                @Override
                public void run() {
                    long id = mDAO.insertMessage(thisMessage);
                    thisMessage.id = id;
                }
            });


            runOnUiThread(() ->{myAdapter.notifyItemInserted(messages.size() - 1);});

            binding.textInput.setText("");
        });


        binding.receiveButton.setOnClickListener(clk -> {
            SimpleDateFormat sdf = new SimpleDateFormat("EEEE, dd-MMM-yyyy hh-mm-ss a");
            String currentDateandTime = sdf.format(new Date());
            String inputMessage = binding.textInput.getText().toString();
            boolean sentButton = false;

            ChatMessage thisMessage = new ChatMessage(inputMessage, currentDateandTime, sentButton);
            messages.add(thisMessage);

            // clear teh previous text
            binding.textInput.setText("");
            myAdapter.notifyDataSetChanged();

            Executor thread = Executors.newSingleThreadExecutor();
            thread.execute(new Runnable() {
                @Override
                public void run() {
                    long id = mDAO.insertMessage(thisMessage);
                    thisMessage.id = id;
                }
            });


            //you must update the screen, redraw the whole list
            runOnUiThread(() ->{myAdapter.notifyDataSetChanged();});

            //clear the previous text:
            binding.textInput.setText("");
        });

        binding.recyclerView.setAdapter(myAdapter = new RecyclerView.Adapter<MyRowHolder>() {
            @NonNull
            @Override
            public MyRowHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                // viewType will either be 0 or 1

                if (viewType == 0) {
                    // 1. load a XML layout
                    SentMessageBinding binding =                            // parent is incase matchparent
                            SentMessageBinding.inflate(getLayoutInflater(), parent, false);

                    // 2. call our constructor below
                    return new MyRowHolder(binding.getRoot()); // getRoot returns a ConstraintLayout with TextViews inside
                }
                else{
                    // 1. load a XML layout
                    ReceiveMessageBinding binding =                            // parent is incase matchparent
                            ReceiveMessageBinding.inflate(getLayoutInflater(), parent, false);

                    // 2. call our constructor below
                    return new MyRowHolder(binding.getRoot()); // getRoot returns a ConstraintLayout with TextViews inside

                }
            }

            public int  getItemViewType(int position){
                // determine which layout to load at row position
                if (messages.get(position).isSentButton() == true) // for the first 5 rows
                {
                    return 0;
                }
                else return 1;
            }
            @Override
            public void onBindViewHolder(@NonNull MyRowHolder holder, int position) {
                ChatMessage obj = messages.get(position);
                holder.messageText.setText(obj.getMessage());
//                holder.messageText.setText("");
                holder.timeText.setText(obj.getTimeSent());

            }

            @Override
            public int getItemCount() {
                return messages.size();
            }
        });

        binding.recyclerView.setLayoutManager(new LinearLayoutManager(this));
    }

    class MyRowHolder extends RecyclerView.ViewHolder {

        TextView messageText;
        TextView timeText;

        public MyRowHolder(@NonNull View itemView) {
            super(itemView);

            itemView.setOnClickListener(clk -> {
                int position = getAbsoluteAdapterPosition();
                ChatMessage clickedMessage = messages.get(position);


                AlertDialog.Builder builder = new AlertDialog.Builder(ChatRoom.this);
                builder.setMessage("Do you want to delete the message:" + messageText.getText());
                builder.setTitle("Question:")
                        .setPositiveButton("Yes", (dialog, cl) -> {
                            ChatMessage m = messages.get(position);
                           Executors.newSingleThreadExecutor().execute(() -> {
                                mDAO.deleteMessage(clickedMessage);//delete the msg from database
                            });
                            messages.remove(position); //delete the msg from the msg list
                            // update the RecycleView list
                            myAdapter.notifyItemRemoved(position);
                            Snackbar.make(messageText, "You deleted message #" + position, Snackbar.LENGTH_LONG)
                                    .setAction("Undo", clk2 -> {

                                        Executors.newSingleThreadExecutor().execute(() -> {
                                            mDAO.insertMessage(clickedMessage);
                                            messages.add(position, clickedMessage);
                                            runOnUiThread(() -> {myAdapter.notifyItemInserted(position);});
                                        });

                                    })
                                    .show();
                        })
                        .setNegativeButton("No", (dialog, cl) -> {})
                        .create().show();

                ChatMessage selected = messages.get(position);

                chatModel.selectedMessage.postValue(selected);
            });
            messageText = itemView.findViewById(R.id.message);
            timeText =itemView.findViewById(R.id.time);
        }
    }
}