import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.HttpEntity;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class CognitiveServices {
    String key;
    CloseableHttpClient httpClient;

    public CognitiveServices(String key) {
        httpClient = HttpClients.createDefault();
        this.key = key;
    }

    public JsonObject postToFaceAPI(String imageURL) {
        HttpPost httpPost = new HttpPost(
        "https://api.projectoxford.ai/face/v1.0/detect?returnFaceId=true&returnFaceLandmarks=true");
        httpPost.setHeader("Content-Type", "application/json");
        httpPost.setHeader("Ocp-Apim-Subscription-Key", key);

        StringEntity entity = null;
        try {
            entity = new StringEntity("{\"url\":\"" + imageURL + "\"}");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        httpPost.setEntity(entity);

        CloseableHttpResponse response = null;
        try {
            response = httpClient.execute(httpPost);
            String str = EntityUtils.toString(response.getEntity());
            JsonParser parser = new JsonParser();
            JsonObject object = parser.parse(str.substring(1, str.length() - 1)).getAsJsonObject();
            closeResponse(response);
            return object;

        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    private void closeResponse(CloseableHttpResponse response) {
        try {
            HttpEntity entity = response.getEntity();
            EntityUtils.consume(entity);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                response.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
