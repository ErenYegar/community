package erenyegar.community.provider;

import com.alibaba.fastjson.JSON;
import erenyegar.community.dto.AccessTokenDTO;
import erenyegar.community.dto.GithubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class GithubProvider {
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
        MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();

        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString((accessTokenDTO)));
            Request request = new Request.Builder()
                    .url("https://github.com/login/oauth/access_token?client_id=b5b530c9c9ee14c475bb&client_secret=72e44e281c5a3d321f43e4c7a0e80f1487002b8b&code="+accessTokenDTO.getCode()+"&redirect_uri=http://localhost:8887/callback&state=1")
                    .post(body)
                    .build();
            try (Response response = client.newCall(request).execute()) {
                String string = response.body().string();
                String token = string.split("&")[0].split("=")[1];
                return token;
                //System.out.println(string);
                //return string;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
    }
    public GithubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GithubUser githubUser = JSON.parseObject(string,GithubUser.class);
            return githubUser;
        } catch (IOException e) {
            //e.printStackTrace();
        }
        return null;
    }
}
