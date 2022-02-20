package com.thimat.sockettelkarnet.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.thimat.sockettelkarnet.models.contactModel;
import com.thimat.sockettelkarnet.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;

public class RealmAdapterContactUs extends RecyclerView.Adapter<RealmAdapterContactUs.contactus> {
    Context context;
    LayoutInflater layoutInflater;
    OrderedRealmCollection<contactModel> data;

    public RealmAdapterContactUs(@Nullable OrderedRealmCollection<contactModel> data, boolean autoUpdate, Context context) {
        this.data=data;
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @NonNull
    @Override
    public contactus onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.realm_adapter_contactus, null, false);
        return new contactus(view);
    }

    @Override
    public void onBindViewHolder(@NonNull contactus holder, int position) {
        contactModel currentmodel = data.get(position);
        holder.modir.setText(currentmodel.getModir());
        holder.poshtibani.setText(currentmodel.getPoshtibani());
        //---------------------------------------------------------------- link telegram
        SpannableString Telagram = new SpannableString(currentmodel.getTelegram());
        Telagram.setSpan(new UnderlineSpan(), 0, Telagram.length(), 0);
        holder.telegram.setText(Telagram);
        //--------------------------------------------------------------- link instagram
        SpannableString Instagram = new SpannableString(currentmodel.getInstagram());
        Instagram.setSpan(new UnderlineSpan(), 0, Instagram.length(), 0);
        holder.instagram.setText(Instagram);
        //----------------------------------------------------------------link site
        SpannableString Site = new SpannableString(currentmodel.getSait());
        Site.setSpan(new UnderlineSpan(), 0, Site.length(), 0);
        holder.site.setText(Site);
        //---------------------------------------------------------------
        holder.address.setText(currentmodel.getAddress());
        holder.Phonenumber.setText(currentmodel.getPhone());
    }

    public class contactus extends RecyclerView.ViewHolder {
        @BindView(R.id.modir)
        TextView modir;
        @BindView(R.id.poshtibani)
        TextView poshtibani;
        @BindView(R.id.telegram)
        TextView telegram;
        @BindView(R.id.instagram)
        TextView instagram;
        @BindView(R.id.lineartelegram)
        LinearLayout lineartelegram;
        @BindView(R.id.linearinsragram)
        LinearLayout linearinsragram;
        @BindView(R.id.linearsite)
        LinearLayout linearsite;
        @BindView(R.id.site)
        TextView site;
        @BindView(R.id.address)
        TextView address;
        @BindView(R.id.Phonenumber)
        TextView Phonenumber;

        public contactus(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            //-------------------------------------------------- telegram
            lineartelegram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!telegram.getText().toString().equals("")) {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://telegram.me/" +
                                telegram.getText().toString().substring(1)));
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                }
            });
            //------------------------------------------------------ linearinsragram
            linearinsragram.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!instagram.getText().toString().equals("")) {
                        Uri uri = Uri.parse("http://instagram.com/_u/" + instagram.getText().toString());
                        Intent likeIng = new Intent(Intent.ACTION_VIEW, uri);

                        likeIng.setPackage("com.instagram.android");

                        try {
                            context.startActivity(likeIng);
                        } catch (ActivityNotFoundException e) {
                            context.startActivity(new Intent(Intent.ACTION_VIEW,
                                    Uri.parse("http://instagram.com/" + instagram.getText().toString())));
                        }
                    }
                }
            });
            //----------------------------------------------------------- linearsite
            linearsite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!site.getText().toString().equals("")) {
                        Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www." + site.getText().toString()));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }
                }
            });

        }
    }
}
