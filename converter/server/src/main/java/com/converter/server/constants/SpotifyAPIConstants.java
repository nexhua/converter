package com.converter.server.constants;

public class SpotifyAPIConstants {

    public static final String client_id = "client_id";

    public static final String client_secret = "client_secret";

    public static final String response_type = "response_type";

    public static final String state = "state";

    public static final String scope = "scope";

    public static final String redirect_uri = "redirect_uri";

    public static final String code = "code";

    public static final String grant_type = "grant_type";

    public static final String authorization_code = "authorization_code";

    public static final String refresh_token = "refresh_token";


    //Spotify URLs

    public static final String spotify_auth_base = "https://accounts.spotify.com";

    public static final String spotify_api_base = "https://api.spotify.com/v1";

    public static final String authorize_path = "/authorize";

    public static final String api_token_path = "/api/token";

    public static final String current_user_playlist_path = "/me/playlists";

    //endregion


    //App Spotify URLs

    //must be added to recognized urls for spotify
    public static final String app_spotify_redirect_endpoint = "http://localhost:8080/auth/spotify/access";

    //endregion
}
