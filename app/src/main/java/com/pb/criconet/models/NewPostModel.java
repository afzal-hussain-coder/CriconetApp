package com.pb.criconet.models;

import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class NewPostModel implements Parcelable {

    private String page_id;
    private String postText;
    private String postLink;
    private String postLinkTitle;
    private String postLinkImage;
    private String postLinkContent;
    private String postFile;
    private String postFileName;
    private String thumb;
    private String details_url;
    private String time;
    private String publisher_type;
    private String post_type;
    private String id;
    private String user_id;
    private String time_text;
    private Publisher publisher;
    private ArrayList<ImageModel> photo_multi;
    private Boolean is_liked_by_me = false;
    private Boolean is_wondered_by_me = false;
    private String count_post_comments = "0";
    private String count_post_shares = "0";
    private String count_post_likes = "0";
    private String count_post_wonders = "0";
    private ArrayList<CommentModel> get_post_comments;


    // Decodes NewPostModel json into NewPostModel model object
    public static NewPostModel fromJson(JSONObject jsonObject) {
        NewPostModel b = new NewPostModel();
        // Deserialize json into object fields
        try {
            try {
                b.get_post_comments = CommentModel.fromJson(jsonObject.optJSONArray("get_post_comments"));
            } catch (Exception a) {
//                a.printStackTrace();
            }
            if(jsonObject.has("tagsusers")) {
                try {
                    b.tagsusers = handleTagData(jsonObject.getJSONArray("tagsusers"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            b.page_id = jsonObject.optString("page_id");
            b.postText = jsonObject.optString("postText");
            b.Orginaltext = jsonObject.optString("Orginaltext");
            b.postText_API = jsonObject.optString("postText_API");

            b.postLink = jsonObject.optString("postLink");
            b.postLinkTitle = jsonObject.optString("postLinkTitle");
            b.postLinkImage = jsonObject.optString("postLinkImage");
            b.postLinkContent = jsonObject.optString("postLinkContent");
            b.postFile = jsonObject.optString("postFile");
            b.postFileName = jsonObject.optString("postFileName");
            b.thumb = jsonObject.optString("thumb");
            b.details_url = jsonObject.optString("details_url");
            b.time = jsonObject.optString("time");
            b.publisher_type = jsonObject.optString("publisher_type");
            b.post_type = jsonObject.optString("post_type");
            b.id = jsonObject.optString("id");
            b.user_id = jsonObject.optString("user_id");
            b.time_text = jsonObject.optString("time_text");
            b.is_liked_by_me = jsonObject.optBoolean("is_liked_by_me");
            b.is_wondered_by_me = jsonObject.optBoolean("is_wondered_by_me");
            b.count_post_comments = jsonObject.optString("count_post_comments");
            b.count_post_shares = jsonObject.optString("count_post_shares");
            b.count_post_likes = jsonObject.optString("count_post_likes");
            b.count_post_wonders = jsonObject.optString("count_post_wonders");

            b.publisher = Publisher.fromJson(jsonObject.optJSONObject("publisher"));
            b.photo_multi = ImageModel.fromJson(jsonObject.optJSONArray("photo_multi"));
        } catch (Exception e) {
//            e.printStackTrace();
            return b;
        }
        // Return new object
        return b;
    }

    // Decodes array of NewPostModel json results into NewPostModel model objects
    public static ArrayList<NewPostModel> fromJson(JSONArray jsonArray) {
        JSONObject modelJson;
        ArrayList<NewPostModel> modeles = new ArrayList<>(jsonArray.length());
        // Process each result in json array, decode and convert to CardModel object
        for (int i = 0; i < jsonArray.length(); i++) {
            try {
                modelJson = jsonArray.getJSONObject(i);
            } catch (Exception e) {
                e.printStackTrace();
                continue;
            }
            NewPostModel model = NewPostModel.fromJson(modelJson);
            if (model != null) {
                modeles.add(model);
            }
        }

        return modeles;
    }


    public final static Parcelable.Creator<NewPostModel> CREATOR = new Creator<NewPostModel>() {
        @SuppressWarnings({
                "unchecked"
        })
        public NewPostModel createFromParcel(Parcel in) {
            return new NewPostModel(in);
        }

        public NewPostModel[] newArray(int size) {
            return (new NewPostModel[size]);
        }

    };

    protected NewPostModel(Parcel in) {
        this.page_id = ((String) in.readValue((String.class.getClassLoader())));
        this.postText = ((String) in.readValue((String.class.getClassLoader())));
        this.Orginaltext = ((String) in.readValue((String.class.getClassLoader())));
        this.postText_API = ((String) in.readValue((String.class.getClassLoader())));
        this.postLink = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkTitle = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkImage = ((String) in.readValue((String.class.getClassLoader())));
        this.postLinkContent = ((String) in.readValue((String.class.getClassLoader())));
        this.postFile = ((String) in.readValue((String.class.getClassLoader())));
        this.postFileName = ((String) in.readValue((String.class.getClassLoader())));
        this.thumb = ((String) in.readValue((String.class.getClassLoader())));
        this.details_url = ((String) in.readValue((String.class.getClassLoader())));
        this.time = ((String) in.readValue((String.class.getClassLoader())));
        this.photo_multi = (ArrayList<ImageModel>) in.readValue((String.class.getClassLoader()));
        this.publisher_type = ((String) in.readValue((String.class.getClassLoader())));
        this.post_type = ((String) in.readValue((String.class.getClassLoader())));
        this.id = ((String) in.readValue((String.class.getClassLoader())));
        this.user_id = ((String) in.readValue((String.class.getClassLoader())));
        this.time_text = ((String) in.readValue((String.class.getClassLoader())));
        this.is_liked_by_me = (Boolean) in.readValue((String.class.getClassLoader()));
        this.is_wondered_by_me = (Boolean) in.readValue((String.class.getClassLoader()));
        this.count_post_comments = ((String) in.readValue((String.class.getClassLoader())));
        this.count_post_shares = ((String) in.readValue((String.class.getClassLoader())));
        this.count_post_likes = ((String) in.readValue((String.class.getClassLoader())));
        this.count_post_wonders = ((String) in.readValue((String.class.getClassLoader())));
        this.publisher = ((Publisher) in.readValue((Publisher.class.getClassLoader())));
        //this.tagsusers = ((TagModel) in.readValue((TagModel.class.getClassLoader())));
    }

    public NewPostModel() {
    }

    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(page_id);
        dest.writeValue(postText);
        dest.writeValue(Orginaltext);
        dest.writeValue(postText_API);
        dest.writeValue(postLink);
        dest.writeValue(postLinkTitle);
        dest.writeValue(postLinkImage);
        dest.writeValue(postLinkContent);
        dest.writeValue(postFile);
        dest.writeValue(postFileName);
        dest.writeValue(thumb);
        dest.writeValue(details_url);
        dest.writeValue(time);
        dest.writeValue(photo_multi);
        dest.writeValue(publisher_type);
        dest.writeValue(post_type);
        dest.writeValue(id);
        dest.writeValue(user_id);
        dest.writeValue(time_text);
        dest.writeValue(is_liked_by_me);
        dest.writeValue(is_wondered_by_me);
        dest.writeValue(count_post_comments);
        dest.writeValue(count_post_shares);
        dest.writeValue(count_post_likes);
        dest.writeValue(count_post_wonders);
        dest.writeValue(publisher);
        dest.writeValue(tagsusers);
    }

    public int describeContents() {
        return 0;
    }

    public String getPage_id() {
        return page_id;
    }

    public void setPage_id(String page_id) {
        this.page_id = page_id;
    }

    public String getPostText() {
        return postText;
    }

    public String getPostText_API() {
        return postText_API;
    }

    public void setPostText_API(String postText_API) {
        this.postText_API = postText_API;
    }

    private String postText_API;

    public String getOrginaltext() {
        return Orginaltext;
    }

    public void setOrginaltext(String orginaltext) {
        Orginaltext = orginaltext;
    }

    private String Orginaltext;


    public void setPostText(String postText) {
        this.postText = postText;
    }

    public String getPostLink() {
        return postLink;
    }

    public void setPostLink(String postLink) {
        this.postLink = postLink;
    }

    public String getPostLinkTitle() {
        return postLinkTitle;
    }

    public void setPostLinkTitle(String postLinkTitle) {
        this.postLinkTitle = postLinkTitle;
    }

    public String getPostLinkImage() {
        return postLinkImage;
    }

    public void setPostLinkImage(String postLinkImage) {
        this.postLinkImage = postLinkImage;
    }

    public String getPostLinkContent() {
        return postLinkContent;
    }

    public void setPostLinkContent(String postLinkContent) {
        this.postLinkContent = postLinkContent;
    }
    public ArrayList<TagModel> getTagsusers() {
        return tagsusers;
    }

    public void setTagsusers(ArrayList<TagModel> tagsusers) {
        this.tagsusers = tagsusers;
    }

    private ArrayList<TagModel>tagsusers;

    public String getPostFile() {
        return postFile;
    }

    public void setPostFile(String postFile) {
        this.postFile = postFile;
    }

    public String getPostFileName() {
        return postFileName;
    }

    public void setPostFileName(String postFileName) {
        this.postFileName = postFileName;
    }

    public String getThumb() {
        return thumb;
    }

    public void setThumb(String thumb) {
        this.thumb = thumb;
    }

    public String getDetails_url() {
        return details_url;
    }

    public void setDetails_url(String details_url) {
        this.details_url = details_url;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public ArrayList<ImageModel> getPhoto_multi() {
        return photo_multi;
    }

    public void setPhoto_multi(ArrayList<ImageModel> photo_multi) {
        this.photo_multi = photo_multi;
    }

    public String getPublisher_type() {
        return publisher_type;
    }

    public void setPublisher_type(String publisher_type) {
        this.publisher_type = publisher_type;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTime_text() {
        return time_text;
    }

    public void setTime_text(String time_text) {
        this.time_text = time_text;
    }

    public String getPublisherName() {
        if (publisher.getUser().getFirst_name() == null) {
            return publisher.getPageModel().getPage_title();
        } else if (publisher.getUser().getFirst_name().equals("")) {
            return publisher.getUser().getName();
        } else {
            return publisher.getUser().getFirst_name() + " " + publisher.getUser().getLast_name();
        }
    }

    public String getPublisherId() {
        if (publisher.getUser().getUser_id() == null) {
            return publisher.getPageModel().getPage_id();
        } else {
            return publisher.getUser().getUser_id();
        }
    }

    public String getPublisherAvatar() {
        if (publisher.getUser().getAvatar() == null) {
            return publisher.getPageModel().getAvatar();
        } else {
            return publisher.getUser().getAvatar();
        }
    }

    public String getPublisherCover() {
        if (publisher.getUser().getCover() == null) {
            return publisher.getPageModel().getCover();
        } else {
            return publisher.getUser().getCover();
        }
    }


    public Publisher getPublisher() {
        return publisher;
    }

    public void setPublisher(Publisher publisher) {
        this.publisher = publisher;
    }


    public Boolean getIs_liked_by_me() {
        return is_liked_by_me;
    }

    public void setIs_liked_by_me(Boolean is_liked_by_me) {
        this.is_liked_by_me = is_liked_by_me;
    }

    public Boolean getIs_wondered_by_me() {
        return is_wondered_by_me;
    }

    public void setIs_wondered_by_me(Boolean is_wondered_by_me) {
        this.is_wondered_by_me = is_wondered_by_me;
    }

    public String getCount_post_comments() {
        return count_post_comments;
    }

    public void setCount_post_comments(String count_post_comments) {
        this.count_post_comments = count_post_comments;
    }

    public String getCount_post_shares() {
        return count_post_shares;
    }

    public void setCount_post_shares(String count_post_shares) {
        this.count_post_shares = count_post_shares;
    }

    public String getCount_post_likes() {
        return count_post_likes;
    }

    public void setCount_post_likes(String count_post_likes) {
        this.count_post_likes = count_post_likes;
    }

    public String getCount_post_wonders() {
        return count_post_wonders;
    }

    public void setCount_post_wonders(String count_post_wonders) {
        this.count_post_wonders = count_post_wonders;
    }

    public ArrayList<CommentModel> getGet_post_comments() {
        return get_post_comments;
    }

    public void setGet_post_comments(ArrayList<CommentModel> get_post_comments) {
        this.get_post_comments = get_post_comments;
    }

    @Override
    public String toString() {
        return "NewPostModel{" +
                "page_id='" + page_id + '\'' +
                ", postText='" + postText + '\'' +
                ", Orginaltext='" + Orginaltext + '\'' +
                ", postText_API='" + postText_API + '\'' +
                ", postLink='" + postLink + '\'' +
                ", postLinkTitle='" + postLinkTitle + '\'' +
                ", postLinkImage='" + postLinkImage + '\'' +
                ", postLinkContent='" + postLinkContent + '\'' +
                ", postFile='" + postFile + '\'' +
                ", postFileName='" + postFileName + '\'' +
                ", thumb='" + thumb + '\'' +
                ", details_url='" + details_url + '\'' +
                ", time='" + time + '\'' +
                ", publisher_type='" + publisher_type + '\'' +
                ", post_type='" + post_type + '\'' +
                ", id='" + id + '\'' +
                ", user_id='" + user_id + '\'' +
                ", time_text='" + time_text + '\'' +
                ", publisher=" + publisher +
                ", photo_multi=" + photo_multi +
                ", is_liked_by_me=" + is_liked_by_me +
                ", is_wondered_by_me=" + is_wondered_by_me +
                ", count_post_comments='" + count_post_comments + '\'' +
                ", count_post_shares='" + count_post_shares + '\'' +
                ", count_post_likes='" + count_post_likes + '\'' +
                ", count_post_wonders='" + count_post_wonders + '\'' +
                ", get_post_comments=" + get_post_comments +
                ", tagsusers=" + tagsusers +

                '}';
    }

    public static ArrayList<TagModel> handleTagData(JSONArray jsonArray) throws JSONException {
        ArrayList<TagModel> marketPlaces = new ArrayList<>();
        for (int i = 0; i < jsonArray.length(); i++) {
            marketPlaces.add(new TagModel(jsonArray.getJSONObject(i)));
        }
        return marketPlaces;
    }
}

