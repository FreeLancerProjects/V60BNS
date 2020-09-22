package com.v60BNS.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;


import com.v60BNS.R;
import com.v60BNS.databinding.ChatMessageLeftRowBinding;
import com.v60BNS.databinding.ChatMessageRightRowBinding;
import com.v60BNS.databinding.LoadMoreBinding;
import com.v60BNS.models.MessageModel;

import java.util.List;

public class Chat_Adapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private final int ITEM_MESSAGE_LEFT = 1;
    private final int ITEM_MESSAGE_RIGHT = 2;
    private final int ITEM_IMAGE_LEFT = 3;
    private final int ITEM_IMAGE_RIGHT = 4;
    private final int ITEM_LOADMORE = 5;


    private List<MessageModel> messageModelList;
    private int current_user_id;
    private String chat_user_image;
    private Context context;

    public Chat_Adapter(List<MessageModel> messageModelList, int current_user_id, String chat_user_image, Context context) {
        this.messageModelList = messageModelList;
        this.current_user_id = current_user_id;
        this.chat_user_image = chat_user_image;
        this.context = context;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_MESSAGE_LEFT) {

            ChatMessageLeftRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_message_left_row, parent, false);
            return new MsgLeftHolder(binding);

        } else if (viewType == ITEM_MESSAGE_RIGHT) {

            ChatMessageRightRowBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.chat_message_right_row, parent, false);
            return new MsgRightHolder(binding);

        } else  {

            LoadMoreBinding binding = DataBindingUtil.inflate(LayoutInflater.from(context), R.layout.load_more, parent, false);
            return new LoadMoreHolder(binding);

        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {

        if (holder instanceof MsgLeftHolder) {

            MsgLeftHolder msgLeftHolder = (MsgLeftHolder) holder;
            MessageModel messageModel = messageModelList.get(msgLeftHolder.getAdapterPosition());
            msgLeftHolder.binding.setMessagemodel(messageModel);
            msgLeftHolder.binding.setEndpoint(chat_user_image);


        } else if (holder instanceof MsgRightHolder) {
            MsgRightHolder msgRightHolder = (MsgRightHolder) holder;
            MessageModel messageModel = messageModelList.get(msgRightHolder.getAdapterPosition());

            msgRightHolder.binding. setMessagemodel(messageModel);


        }else if (holder instanceof LoadMoreHolder) {
            LoadMoreHolder typingHolder = (LoadMoreHolder) holder;


        }


    }

    @Override
    public int getItemCount() {
        return messageModelList.size();
    }

    public class MsgLeftHolder extends RecyclerView.ViewHolder {

        private ChatMessageLeftRowBinding binding;

        public MsgLeftHolder(ChatMessageLeftRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;


        }

    }

    public class MsgRightHolder extends RecyclerView.ViewHolder {
        private ChatMessageRightRowBinding binding;

        public MsgRightHolder(ChatMessageRightRowBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

        }


    }





    public class LoadMoreHolder extends RecyclerView.ViewHolder {

        private LoadMoreBinding binding;

        public LoadMoreHolder(LoadMoreBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }


    }



    @Override
    public int getItemViewType(int position) {
        MessageModel messageModel = messageModelList.get(position);

        if (messageModel == null) {

            return ITEM_LOADMORE;

        } else {


            if (messageModel.getTo_user_id() == current_user_id) {
                    return ITEM_MESSAGE_LEFT;

            } else {


                    return ITEM_MESSAGE_RIGHT;

            }
        }


    }
}
