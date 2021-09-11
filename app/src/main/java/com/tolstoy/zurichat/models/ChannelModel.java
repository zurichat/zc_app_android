package com.tolstoy.zurichat.models;


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

public class ChannelModel {
    @Json(name = "_id")
    private String _id;
    @Json(name = "created_on")
    private String createdOn;
    @Json(name = "description")
    private String description;
    @Json(name = "name")
    private String name;
    @Json(name = "private")
    private boolean _private;
    @Json(name = "roles")
    private List<Object> roles = null;
    @Json(name = "slug")
    private String slug;
    //@Json(name = "users")
    //private String users;
    @Json(name = "members")
    private long members;

    private transient String type;
    private transient int viewType;
    private transient boolean read;

    ChannelModel(){
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

    /*public JSONObject getUsers() {
        return users;
    }

    public void setUsers(JSONObject users) {
        this.users = users;
    }*/

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
}
