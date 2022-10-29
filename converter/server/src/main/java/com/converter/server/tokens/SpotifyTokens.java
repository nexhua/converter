package com.converter.server.tokens;

import java.util.Calendar;
import java.util.Date;

public class SpotifyTokens {
    private String access_token;

    private String refresh_token;

    private String token_type;

    private String scope;

    private Integer expires_in;

    private Calendar tokenExpireDate;

    public SpotifyTokens() {
    }

    public SpotifyTokens(String access_token, String refresh_token, String token_type, String scope, Integer expires_in) {
        this.access_token = access_token;
        this.refresh_token = refresh_token;
        this.token_type = token_type;
        this.scope = scope;
        this.expires_in = expires_in;
    }

    //region getters end setters

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getToken_type() {
        return token_type;
    }

    public void setToken_type(String token_type) {
        this.token_type = token_type;
    }

    public String getScope() {
        return scope;
    }

    public void setScope(String scope) {
        this.scope = scope;
    }

    public Integer getExpires_in() {
        return expires_in;
    }

    public void setExpires_in(Integer expires_in) {
        this.expires_in = expires_in;

        this.tokenExpireDate = Calendar.getInstance();
        this.tokenExpireDate.add(Calendar.SECOND, this.expires_in);
    }

    public Calendar getTokenExpireDate() {
        return tokenExpireDate;
    }

    public void setTokenExpireDate(Calendar tokenExpireDate) {
        this.tokenExpireDate = tokenExpireDate;
    }

    //endregion

    public String toBearerTokenString() {
        return "Bearer " + this.access_token;
    }

    public boolean shouldRefreshToken() {
        if(this.tokenExpireDate != null) {
            return this.tokenExpireDate.compareTo(Calendar.getInstance()) <= 0;
        }

        return false;
    }

    public void updateToken(SpotifyTokens tokens) {
        this.setAccess_token(tokens.getAccess_token());
        this.setToken_type(tokens.getToken_type());
        this.setScope(tokens.getScope());
        this.setExpires_in(tokens.getExpires_in());
    }
}
