package tech.yojigen.pixiv.network;

import java.io.IOException;


public interface PixivListener {
    public void onFailure(IOException e);

    public void onResponse(String json);
}
