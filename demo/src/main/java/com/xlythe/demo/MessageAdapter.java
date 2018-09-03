package com.xlythe.demo;

import android.content.Context;
import android.graphics.PorterDuff;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;


import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {
    private static final String TAG = MessageAdapter.class.getSimpleName();

    // Duration between considering a message to be part of the same message, or split into different messages
    private static final long SPLIT_DURATION = 60 * 1000;

    private static final int TYPE_TOP_RIGHT                 = 0;
    private static final int TYPE_MIDDLE_RIGHT              = 1;
    private static final int TYPE_BOTTOM_RIGHT              = 2;
    private static final int TYPE_SINGLE_RIGHT              = 3;
    private static final int TYPE_TOP_LEFT                  = 4;
    private static final int TYPE_MIDDLE_LEFT               = 5;
    private static final int TYPE_BOTTOM_LEFT               = 6;
    private static final int TYPE_SINGLE_LEFT               = 7;

    private static final SparseIntArray LAYOUT_MAP = new SparseIntArray();

    static {
        LAYOUT_MAP.put(TYPE_TOP_RIGHT, R.layout.right_top);
        LAYOUT_MAP.put(TYPE_MIDDLE_RIGHT, R.layout.right_middle);
        LAYOUT_MAP.put(TYPE_BOTTOM_RIGHT, R.layout.right_bottom);
        LAYOUT_MAP.put(TYPE_SINGLE_RIGHT, R.layout.right_single);
        LAYOUT_MAP.put(TYPE_TOP_LEFT, R.layout.left_top);
        LAYOUT_MAP.put(TYPE_MIDDLE_LEFT, R.layout.left_middle);
        LAYOUT_MAP.put(TYPE_BOTTOM_LEFT, R.layout.left_bottom);
        LAYOUT_MAP.put(TYPE_SINGLE_LEFT, R.layout.left_single);
    }

    private ArrayList<Message> mMessages;
    private Context mContext;
    private MessageViewHolder.ClickListener mClickListener;

    public static abstract class MessageViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {
        private Message mText;
        private Context mContext;
        private ClickListener mListener;

        public MessageViewHolder(View v, ClickListener listener) {
            super(v);
            mListener = listener;
            v.setOnClickListener(this);
            v.setOnLongClickListener(this);
        }

        public void setMessage(Context context, Message message) {
            mText = message;
            mContext = context;
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClicked(getMessage());
            }
        }

        @Override
        public boolean onLongClick(View v) {
            return mListener != null && mListener.onItemLongClicked(getMessage());
        }

        public interface ClickListener {
            void onItemClicked(Message message);
            boolean onItemLongClicked(Message message);
        }

        public Message getMessage() {
            return mText;
        }

        public Context getContext() {
            return mContext;
        }
    }

    public static class ViewHolder extends MessageViewHolder {
        TextView mTextView;
        FrameLayout mFrame;

        public ViewHolder(View v, ClickListener listener) {
            super(v, listener);
            mFrame = v.findViewById(R.id.frame);
            mTextView = v.findViewById(R.id.message);
        }

        public void setMessage(Context context, Message message) {
            super.setMessage(context, message);
            setBodyText(message.getBody());
            setColor(context.getResources().getColor(R.color.colorPrimary));
        }

        public void setColor(int color) {
            mFrame.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }

        public void setBodyText(String body) {
            mTextView.setText(body);
        }
    }

    public static class LeftViewHolder extends ViewHolder {
        public FrameLayout mFrame;

        public LeftViewHolder(View v, ClickListener listener) {
            super(v, listener);
            mFrame = v.findViewById(R.id.frame);
        }

        @Override
        public void setMessage(Context context, Message message) {
            super.setMessage(context, message);
            setColor(context.getResources().getColor(R.color.colorGrey));
        }

        public void setColor(int color) {
            mFrame.getBackground().setColorFilter(color, PorterDuff.Mode.MULTIPLY);
        }
    }

    public MessageAdapter(Context context, ArrayList<Message> messages) {
        mMessages = messages;
        mContext = context;
    }

    public void setOnClickListener(MessageViewHolder.ClickListener onClickListener) {
        mClickListener = onClickListener;
    }

    // Create new views (invoked by the layout manager)
    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(mContext).inflate(LAYOUT_MAP.get(viewType), parent, false);
        switch(viewType) {
            case TYPE_TOP_RIGHT:
            case TYPE_MIDDLE_RIGHT:
            case TYPE_BOTTOM_RIGHT:
            case TYPE_SINGLE_RIGHT:
                return new ViewHolder(layout, mClickListener);
            case TYPE_TOP_LEFT:
            case TYPE_MIDDLE_LEFT:
            case TYPE_BOTTOM_LEFT:
            case TYPE_SINGLE_LEFT:
                return new LeftViewHolder(layout, mClickListener);
            default:
                throw new IllegalStateException();
        }
    }

    @Override
    public int getItemViewType(int position) {
        Message message = getMessage(position);

        Message prevText = null;
        if (position > 0) {
            prevText = getMessage(position - 1);
        }

        Message nextText = null;
        if (position + 1 < mMessages.size()) {
            nextText = getMessage(position + 1);
        }

        // Get the date of the current, previous and next message.
        long dateCurrent = message.getTimestamp();
        long datePrevious = 0;
        long dateNext = 0;

        // Get the sender of the current, previous and next message. (returns true if you)
        boolean userCurrent = message.isIncoming();
        boolean userPrevious = message.isIncoming();
        boolean userNext = !message.isIncoming();

        // Check if previous message exists, then get the date and sender.
        if (prevText != null) {
            datePrevious = prevText.getTimestamp();
            userPrevious = prevText.isIncoming();
        }

        // Check if next message exists, then get the date and sender.
        if (nextText != null) {
            dateNext = nextText.getTimestamp();
            userNext = nextText.isIncoming();
        }

        // Calculate time gap.
        boolean largePC = dateCurrent - datePrevious > SPLIT_DURATION;
        boolean largeCN = dateNext - dateCurrent > SPLIT_DURATION;

        if (!userCurrent && (userPrevious || largePC) && (!userNext && !largeCN)) {
            return TYPE_TOP_RIGHT;
        } else if (!userCurrent && (!userPrevious && !largePC) && (!userNext && !largeCN)) {
            return TYPE_MIDDLE_RIGHT;
        } else if (!userCurrent && (!userPrevious && !largePC)) {
            return TYPE_BOTTOM_RIGHT;
        } else if (!userCurrent) {
            return TYPE_SINGLE_RIGHT;
        } else if ((!userPrevious || largePC) && (userNext && !largeCN)) {
            return TYPE_TOP_LEFT;
        } else if ((userPrevious && !largePC) && (userNext && !largeCN)) {
            return TYPE_MIDDLE_LEFT;
        } else if (userPrevious && !largePC) {
            return TYPE_BOTTOM_LEFT;
        } else {
            return TYPE_SINGLE_LEFT;
        }
    }

    private Message getMessage(int position) {
        return mMessages.get(position);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        holder.setMessage(mContext, getMessage(position));
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }
}