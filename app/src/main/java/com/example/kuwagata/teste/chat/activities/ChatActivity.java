package com.example.kuwagata.teste.chat.activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.kuwagata.teste.R;
import com.example.kuwagata.teste.chat.adapter.ChatAdapter;
import com.example.kuwagata.teste.chat.model.ChatUser;
import com.sendbird.android.BaseChannel;
import com.sendbird.android.BaseMessage;
import com.sendbird.android.OpenChannel;
import com.sendbird.android.OpenChannelListQuery;
import com.sendbird.android.PreviousMessageListQuery;
import com.sendbird.android.SendBird;
import com.sendbird.android.SendBirdException;
import com.sendbird.android.User;
import com.sendbird.android.UserMessage;

import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private static final String APP_ID = "DFB57D2D-92BE-42AC-B1F4-F4A9AE04E163";
    private static final String TAG = "SendBird_ChatApp";
    private static OpenChannel globalOpenChannel;

    private ChatAdapter chatAdapter;
    private PreviousMessageListQuery mPrevMessageListQuery;
    private ChatUser chatUser;

    private ListView chatListview;
    private EditText sendMessageEdtxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        SendBird.init(APP_ID, this);

        // Crio o adapter vazio
        chatAdapter = new ChatAdapter(this);
        // Crio o listview
        chatListview = (ListView) findViewById(R.id.previous_messages);
        // Crio o edittext
        sendMessageEdtxt = (EditText) findViewById(R.id.message_to_sent);

        // Atribuo ChatAdapter à listview (nesse momento a lista está vazia)
        chatListview.setAdapter(chatAdapter);

        // Recebo os campos de HomeActivity
        if (getIntent().hasExtra("chatUser")) {
            chatUser = (ChatUser) getIntent().getSerializableExtra("chatUser");
        }

        connect();
        receiveMessages();
    }

    /**
     * Recebe a mensagem que foi enviada por alguém.
     */
    private void receiveMessages() {
        SendBird.addChannelHandler("Interaje02", new SendBird.ChannelHandler() {
            @Override
            public void onMessageReceived(BaseChannel baseChannel, BaseMessage baseMessage) {
                chatAdapter.appendMessage(baseMessage);
                chatAdapter.notifyDataSetChanged();

                loadPreviousMessage(false);
            }
        });
    }

    /**
     * Conecta à API do SendBird e Atualiza o nome do usuário.
     */
    private void connect() {
        Log.d(TAG, "Conectando...");
        SendBird.connect(chatUser.getmUserId(), new SendBird.ConnectHandler() {
            @Override
            public void onConnected(User user, SendBirdException e) {
                // Log.d(TAG, "UserID cadastrado: " +user.getUserId());
                SendBird.updateCurrentUserInfo(chatUser.getmNickname(), null, new SendBird.UserInfoUpdateHandler() {
                    @Override
                    public void onUpdated(SendBirdException e) {
                        if (e != null) {
                            Toast.makeText(ChatActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        listOpenChannel();
                    }
                });
            }
        });
    }

    /**
     * Lista os canais públicos existentes na API.
     */
    private void listOpenChannel() {
        Log.d(TAG, "Listando...");
        OpenChannelListQuery mChannelListQuery = OpenChannel.createOpenChannelListQuery();
        mChannelListQuery.next(new OpenChannelListQuery.OpenChannelListQueryResultHandler() {
            @Override
            public void onResult(List<OpenChannel> channels, SendBirdException e) {
                if (e != null) {
                    Toast.makeText(ChatActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                if (channels.size() > 0) {
                    // Toast.makeText(ChatActivity.this, "Canais: " + channels.get(0).getName(), Toast.LENGTH_LONG).show();
                    String channelUrl = channels.get(0).getUrl();
                    enterOpenChannel(channelUrl);
                }
            }
        });
    }

    /**
     * Abre a conexão com um canal (chat) por meio de uma URL e tenta entrar nesse canal (chat).
     *
     * @param channelUrl
     */
    private void enterOpenChannel(String channelUrl) {
        Log.d(TAG, "Entrando em um chat...");
        OpenChannel.getChannel(channelUrl, new OpenChannel.OpenChannelGetHandler() {
            @Override
            public void onResult(final OpenChannel openChannel, SendBirdException e) {
                if (e != null) {
                    Toast.makeText(ChatActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }

                openChannel.enter(new OpenChannel.OpenChannelEnterHandler() {
                    @Override
                    public void onResult(SendBirdException e) {
                        if (e != null) {
                            Toast.makeText(ChatActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        globalOpenChannel = openChannel;
                        loadPreviousMessage(true);

                    }
                });
            }
        });
    }

    /**
     * Carrega as 30 últimas mensagens daquele canal.
     * Se refresh for @true, então ele lista as 30 últimas mensagens (somando com as outras que já haviam sido carregadas, se for o caso).
     * Senão ele só atualiza as mensagens existentes.
     *
     * @param refresh True: retorna 30 mensagens, senão só atualiza.
     */
    private void loadPreviousMessage(final Boolean refresh) {

        if (refresh || mPrevMessageListQuery == null) {
            mPrevMessageListQuery = globalOpenChannel.createPreviousMessageListQuery();
        }

        Log.d(TAG, "Carregando mensagens anteriores");
        mPrevMessageListQuery.load(30, true, new PreviousMessageListQuery.MessageListQueryResult() {
            @Override
            public void onResult(List<BaseMessage> messages, SendBirdException e) {
                if (e != null) {
                    Toast.makeText(ChatActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                if (messages.size() > 0) {
                    Log.d(TAG, "ID da primeira mensagem: " + messages.get(0).getMessageId());
                    for (BaseMessage message : messages) {
                        chatAdapter.insertMessage(message);
                    }
                    chatAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    public void prepareToSendMessage(View v) {
        if (!sendMessageEdtxt.getText().toString().equalsIgnoreCase("")) {
            sendMessage(sendMessageEdtxt.getText().toString());
        }
    }

    /**
     * Envia uma mensagem para a API.
     *
     * @param messageFromUser
     */
    private void sendMessage(String messageFromUser) {
        Log.d(TAG, "Enviando uma mensagem...");
        globalOpenChannel.sendUserMessage(messageFromUser, new BaseChannel.SendUserMessageHandler() {
            @Override
            public void onSent(UserMessage userMessage, SendBirdException e) {
                if (e != null) {
                    Toast.makeText(ChatActivity.this, "" + e.getCode() + ":" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    return;
                }
                chatAdapter.appendMessage(userMessage);
                chatAdapter.notifyDataSetChanged();
                Log.d(TAG, "Mensagem {" + userMessage.getMessage() + "} enviada!");
                sendMessageEdtxt.setText("");
            }
        });
    }
}
