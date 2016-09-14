package com.example.kuwagata.teste.chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.kuwagata.teste.R;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.UserMessage;

import java.util.ArrayList;

/**
 * Created by kuwagata on 14/09/16.
 */
public class ChatAdapter extends BaseAdapter {

    private final ArrayList<Object> mItemList;
    private Context context;
    private LayoutInflater mLayoutInflater;

    public ChatAdapter(Context context) {
        mItemList = new ArrayList<>();
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    /**
     * Insere um objeto BaseMessage de id zero ao listview (uso: É recomendado o seu uso junto ao método de mensagens anteriores)
     * @param message
     */
    public void insertMessage(BaseMessage message) {
        mItemList.add(0, message);
    }

    /**
     * Adiciona um objeto mensagem ao listview
     * @param message
     */
    public void appendMessage(BaseMessage message) {
        mItemList.add(message);
    }

    @Override
    public int getCount() {
        return mItemList.size();
    }

    @Override
    public Object getItem(int position) {
        return mItemList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = mLayoutInflater.inflate(R.layout.item_message_layout, parent, false);
        if (mItemList.size() > 0) {
            final Object item = getItem(position);
            final UserMessage message = (UserMessage) item;

            TextView username_txtv = (TextView) view.findViewById(R.id.name);
            TextView messageDesc_txtv = (TextView) view.findViewById(R.id.message_desc);

            username_txtv.setText(message.getSender().getNickname());
            messageDesc_txtv.setText(message.getMessage());
        } else {
            TextView username_txtv = (TextView) view.findViewById(R.id.name);
            TextView messageDesc_txtv = (TextView) view.findViewById(R.id.message_desc);

            username_txtv.setText("");
            messageDesc_txtv.setText("");
        }

        return view;
    }
}
