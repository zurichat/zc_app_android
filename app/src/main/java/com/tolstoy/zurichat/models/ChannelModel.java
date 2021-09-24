package com.tolstoy.zurichat.models;


import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.moshi.Json;

import java.util.List;
import java.util.Objects;

/******
 * Name: Name Of Channel
 * Privacy: Public Or Private. True for private, false for public
 * Read: If Channel has unread messages or not
 * Type: To be used with the view holder so different views can be held.
 *       (Types Include channel, channel_header_add and channel_header_unread)
 *       Others Can Be Added later on as need be.
 * id: To be used with the Diff Util to identify each item. Might be changed later
 * viewType: Only contains three values. 0 for the header, 1 for the channel body, 2 for divider
 */

public class ChannelModel implements Parcelable {
    @SerializedName("_id")
    @Expose
    private String _id;
    @SerializedName("created_on")
    @Expose
    private String createdOn;
    @SerializedName("description")
    @Expose
    private String description;
    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("private")
    @Expose
    private boolean _private;
    @SerializedName("roles")
    @Expose
    private List<Object> roles = null;
    @SerializedName("slug")
    @Expose
    private String slug;
    @SerializedName("members")
    @Expose
    private long members;

    private transient String type;
    private transient int viewType;
    private transient boolean read;

    /***
     * Do Not Remove Constructor
     * Removing constructor will make json ignore the transient variables
     */
    public ChannelModel(){
        type = "channel";
        viewType = 1;
    }

    public ChannelModel(String name, boolean _private, boolean read, String type,String _id, int viewType) {
        this._id = _id;
        this.name = name;
        this._private = _private;
        this.type = type;
        this.viewType = viewType;
        this.read = read;
    }

    protected ChannelModel(Parcel in) {
        _id = in.readString();
        createdOn = in.readString();
        description = in.readString();
        name = in.readString();
        _private = in.readByte() != 0;
        slug = in.readString();
        members = in.readLong();
    }

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(String createdOn) {
        this.createdOn = createdOn;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isPrivate() {
        return _private;
    }

    public void setPrivate(boolean _private) {
        this._private = _private;
    }

    public List<Object> getRoles() {
        return roles;
    }

    public void setRoles(List<Object> roles) {
        this.roles = roles;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public long getMembers() {
        return members;
    }

    public void setMembers(long members) {
        this.members = members;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getViewType() {
        return viewType;
    }

    public void setViewType(int viewType) {
        this.viewType = viewType;
    }

    public boolean isRead() {
        return read;
    }

    public void setRead(boolean read) {
        this.read = read;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChannelModel that = (ChannelModel) o;
        return viewType == that.viewType && _id.equals(that._id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(_id, viewType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(_id);
        dest.writeString(createdOn);
        dest.writeString(description);
        dest.writeString(name);
        dest.writeByte((byte) (_private ? 1 : 0));
        dest.writeString(slug);
        dest.writeLong(members);
    }

    public static final Creator<ChannelModel> CREATOR = new Creator<>() {
        @Override
        public ChannelModel createFromParcel(Parcel in) {
            return new ChannelModel(in);
        }

        @Override
        public ChannelModel[] newArray(int size) {
            return new ChannelModel[size];
        }
    };
}
