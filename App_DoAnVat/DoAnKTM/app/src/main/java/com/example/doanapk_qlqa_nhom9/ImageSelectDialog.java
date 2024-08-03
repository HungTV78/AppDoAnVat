package com.example.doanapk_qlqa_nhom9;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.IOException;
import java.io.InputStream;

public class ImageSelectDialog extends Dialog {
    public interface OnImageSelectedListener {
        void onImageSelected(String imageName);
    }

    public ImageSelectDialog(Context context, OnImageSelectedListener listener) {
        super(context);
        setContentView(R.layout.dialog_select_image);

        RecyclerView rvImages = findViewById(R.id.rvImages);
        rvImages.setLayoutManager(new GridLayoutManager(context, 3));

        String[] imageFiles = getImageFilesFromAssets();
        rvImages.setAdapter(new ImageAdapter(context, imageFiles, listener));
    }

    private String[] getImageFilesFromAssets() {
        try {
            return getContext().getAssets().list(""); // List all files in the root of assets
        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{};
        }
    }

    private class ImageAdapter extends RecyclerView.Adapter<ImageAdapter.ImageViewHolder> {
        private Context context;
        private String[] imageFiles;
        private OnImageSelectedListener listener;

        public ImageAdapter(Context context, String[] imageFiles, OnImageSelectedListener listener) {
            this.context = context;
            this.imageFiles = imageFiles;
            this.listener = listener;
        }

        @Override
        public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_image, parent, false);
            return new ImageViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ImageViewHolder holder, int position) {
            String imageName = imageFiles[position];
            try {
                // Load the image as Bitmap from assets
                InputStream is = context.getAssets().open(imageName);
                Bitmap bitmap = BitmapFactory.decodeStream(is);
                holder.imageView.setImageBitmap(bitmap);
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            holder.itemView.setOnClickListener(v -> {
                listener.onImageSelected(imageName);
                dismiss();
            });
        }

        @Override
        public int getItemCount() {
            return imageFiles.length;
        }

        class ImageViewHolder extends RecyclerView.ViewHolder {
            ImageView imageView;

            public ImageViewHolder(View itemView) {
                super(itemView);
                imageView = itemView.findViewById(R.id.imageView);
            }
        }
    }
}

