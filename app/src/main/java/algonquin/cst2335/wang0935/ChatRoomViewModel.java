package algonquin.cst2335.wang0935;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;

import algonquin.cst2335.wang0935.ChatMessage;

public class ChatRoomViewModel extends ViewModel {
    public MutableLiveData<ArrayList<ChatMessage>> messages = new MutableLiveData< >(null);

    public MutableLiveData<ChatMessage> selectedMessage = new MutableLiveData< >();
}