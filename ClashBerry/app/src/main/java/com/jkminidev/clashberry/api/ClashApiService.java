package com.jkminidev.clashberry.api;

import com.jkminidev.clashberry.models.ClanInfo;
import com.jkminidev.clashberry.models.WarData;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ClashApiService {
    
    @GET("api/war/{clanTag}")
    Call<WarData> getWarData(@Path("clanTag") String clanTag);
    
    @GET("api/clan/{clanTag}")
    Call<ClanInfo> getClanInfo(@Path("clanTag") String clanTag);
}