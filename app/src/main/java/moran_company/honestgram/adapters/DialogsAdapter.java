package moran_company.honestgram.adapters;

import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.fasterxml.jackson.databind.ser.std.ObjectArraySerializer;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;
import moran_company.honestgram.GlideApp;
import moran_company.honestgram.HonestApplication;
import moran_company.honestgram.R;
import moran_company.honestgram.data.Chats;
import moran_company.honestgram.data.Dialogs;
import moran_company.honestgram.data.Goods;
import moran_company.honestgram.data.PreferencesData;
import moran_company.honestgram.data.Users;
import moran_company.honestgram.utility.Utility;

/**
 * Created by roman on 14.01.2018.
 */

public class DialogsAdapter extends BaseAdapter<Chats, DialogsAdapter.ViewHolder> {

    private Filter currentFilter = Filter.USERS_DIALOGS;

    private List<Chats> mChats = new ArrayList<>();

    @Override
    protected int getItemLayout(int viewType) {
        return R.layout.list_item_dialog;
    }

    public void setFilter(Filter filter) {
        if (filter == currentFilter) return;
        currentFilter = filter;
        updateByFilter();
    }

    public List<Chats> getFilteredChats(Filter chatFilter) {
        List<Chats> filterChats = new ArrayList<>();
        for (Chats chat : mChats) {
            switch (chatFilter) {
                case USERS_DIALOGS:
                    if (chat.getProductId() == 0) filterChats.add(chat);
                    break;
                case PRODUCTS_DIALOGS:
                    if (chat.getProductId() != 0) filterChats.add(chat);
                    break;
            }
        }
        return filterChats;
    }

    @Override
    public void setItems(List<Chats> items) {
        mChats = items;
        updateByFilter();
    }

    private void updateByFilter() {
        List<Chats> filterChats = new ArrayList<>();
        for (Chats chat : mChats) {
            switch (currentFilter) {
                case USERS_DIALOGS:
                    if (chat.getProductId() == 0) filterChats.add(chat);
                    break;
                case PRODUCTS_DIALOGS:
                    if (chat.getProductId() != 0) filterChats.add(chat);
                    break;
            }
        }
        super.setItems(new ArrayList<>(filterChats));
        notifyDataSetChanged();
    }


    public enum Filter {USERS_DIALOGS, PRODUCTS_DIALOGS}

    @Override
    public void onBindViewHolder(DialogsAdapter.ViewHolder holder, int position) {
        /*Dialogs lastMessage = items.get(position).get(items.get(position).size() - 1);
        holder.lastMessage.setText(lastMessage.getMessage());
        Users myUser = PreferencesData.INSTANCE.getUser();
        Users otherUser = lastMessage.getUser_id()==myUser.getId()?
                myUser:
                Utility.getUserById(lastMessage.getUser_id(), PreferencesData.INSTANCE.getUsers());
        if (otherUser != null)
            GlideApp.with(holder.context)
                    .load(otherUser.getPhotoURL())
                    .placeholder(R.drawable.unknown)
                    .into(holder.avatarProfile);
        holder.whoWrite.setText(lastMessage.getUser_id() == myUser.getId() ? holder.context.getString(R.string.you_write) :
                otherUser!=null?otherUser.getNickname()+" : ":holder.context.getString(R.string.companion_write));*/
        Chats chat = items.get(position);
        Users myUser = PreferencesData.INSTANCE.getUser();
        long companionId;
        if (myUser.getId() == chat.getOwnerId())
            companionId = chat.getCompanionId();
        else
            companionId = chat.getOwnerId();

        //Users otherUser = Utility.getUserById(companionId, PreferencesData.INSTANCE.getUsers());

        Users otherUser = HonestApplication.getDb().getUserDao()
                .findUserById(companionId);


        int lastMsgIndex = chat.getDialogs().size() - 1;
        if (otherUser != null)
            GlideApp.with(holder.context)
                    .load(otherUser.getPhotoURL())
                    .placeholder(R.drawable.unknown)
                    .into(holder.avatarProfile);

        if (lastMsgIndex != -1) {
            Dialogs lastMessage = chat.getDialogs().get(lastMsgIndex);
            holder.lastMessage.setText(lastMessage.getMessage());
            holder.whoWrite.setText(lastMessage.getUser_id() == myUser.getId() ? holder.context.getString(R.string.you_write) :
                    otherUser != null ? otherUser.getNickname() + " : " : holder.context.getString(R.string.companion_write));
            holder.timestamp.setText(Utility.getFormattedDate(chat.getDialogs().get(lastMsgIndex).getTimestamp()));
            if (chat.getProductId() == 0) {
                if (otherUser != null) {
                    holder.name.setText(otherUser.getNickname());
                    GlideApp.with(holder.context)
                            .load(otherUser.getPhotoURL())
                            .placeholder(R.drawable.unknown)
                            .into(holder.avatarProfile);
                }
            } else {
                Goods good = Utility.getProductById(chat.getProductId());
                holder.name.setText(good.getTitle());
                GlideApp.with(holder.context)
                        .load(good.getUrl())
                        .placeholder(R.drawable.unknown)
                        .into(holder.avatarProfile);
            }
        } else {
            holder.whoWrite.setText(R.string.new_conversation);
        }

    }

    public class ViewHolder extends BaseViewHolder {
        @BindView(R.id.lastMessage)
        TextView lastMessage;
        @BindView(R.id.whoWrite)
        TextView whoWrite;
        @BindView(R.id.avatarProfile)
        CircleImageView avatarProfile;
        @BindView(R.id.timestamp)
        TextView timestamp;
        @BindView(R.id.name)
        TextView name;

        public ViewHolder(OnViewHolderEventListener listener, View itemView) {
            super(listener, itemView);
        }
    }
}
