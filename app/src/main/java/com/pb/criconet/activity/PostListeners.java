package com.pb.criconet.activity;

import com.pb.criconet.models.NewPostModel;

public interface PostListeners {

    void onLikeClickListener(NewPostModel post);

    void onDislikeClickListener(NewPostModel post);

    void onCommentClickListener(NewPostModel post);

    void onShareClickListener(NewPostModel post);

    void onProfileClickListener(NewPostModel post);

    void onReportFeedListener(String id,String message);

    void onDeleteFeedListener(String id);
    void onEditFeedListener(String id,String text);
}
