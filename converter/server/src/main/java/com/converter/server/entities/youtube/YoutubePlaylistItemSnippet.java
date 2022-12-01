package com.converter.server.entities.youtube;

public class YoutubePlaylistItemSnippet {

    private String publishedAt;

    private String channelId;

    private String title;

    private String description;

    private YoutubeThumbnails thumbnails;

    private String channelTitle;

    private String playlistId;

    private String videoOwnerChannelTitle;

    private String videoOwnerChannelId;

    private int position;

    private YoutubeVideoResourceId resourceId;

    public YoutubePlaylistItemSnippet() {
    }

    //region Getters and Setters

    public String getPublishedAt() {
        return publishedAt;
    }

    public void setPublishedAt(String publishedAt) {
        this.publishedAt = publishedAt;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public YoutubeThumbnails getThumbnails() {
        return thumbnails;
    }

    public void setThumbnails(YoutubeThumbnails thumbnails) {
        this.thumbnails = thumbnails;
    }

    public String getChannelTitle() {
        return channelTitle;
    }

    public void setChannelTitle(String channelTitle) {
        this.channelTitle = channelTitle;
    }

    public String getPlaylistId() {
        return playlistId;
    }

    public void setPlaylistId(String playlistId) {
        this.playlistId = playlistId;
    }

    public String getVideoOwnerChannelTitle() {
        return videoOwnerChannelTitle;
    }

    public void setVideoOwnerChannelTitle(String videoOwnerChannelTitle) {
        this.videoOwnerChannelTitle = videoOwnerChannelTitle;
    }

    public String getVideoOwnerChannelId() {
        return videoOwnerChannelId;
    }

    public void setVideoOwnerChannelId(String videoOwnerChannelId) {
        this.videoOwnerChannelId = videoOwnerChannelId;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public YoutubeVideoResourceId getResourceId() {
        return resourceId;
    }

    public void setResourceId(YoutubeVideoResourceId resourceId) {
        this.resourceId = resourceId;
    }


    //endregion
}
