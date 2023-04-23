package com.Task.ChatGPT;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
@org.springframework.stereotype.Service
public class Service {

    @Autowired
    mongoRepository mongoRepository;
    public String getMessage(Content content) throws IOException, JSONException {
        Store temp = mongoRepository.findById(content.getTitle()).get();
        if(temp==null){
            URL url = new URL("https://api.openai.com/v1/completions");
            HttpURLConnection con = (HttpURLConnection)url.openConnection();

            // default method is get
            con.setRequestMethod("GET");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("Accept", "application/json");
            con.setDoOutput(true);
            con.setRequestProperty("Authorization", "Bearer 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");
            // connection established
            String queryString = "Random user response on a lie stream with title: "+content.getTitle()+" and description: "+content.getDesc();
            // now we need request body to make a request in chat gpt as json body
            JSONObject data = new JSONObject()
                    .put("model", "text-davinci-003")
                    .put("prompt", queryString)
                    .put("max_tokens", 4000)
                    .put("temperature", 1)
                    .put("n" , 10); // we need 10 comment every time
            // chnage this json to string
            String jsonString = data.toString();

            try(OutputStream os = con.getOutputStream()) {
                byte[] input = jsonString.getBytes("utf-8");
                os.write(input, 0, input.length);
            }

            StringBuilder response = new StringBuilder();
            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), "utf-8"))) {

                String responseLine = null;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                //System.out.println(response.toString());
            }
            JSONObject jsonObject = new JSONObject(response.toString());
            // make a json object array in term of choices
            JSONArray array = jsonObject.getJSONArray("choices");

            ArrayList<String> arr = new ArrayList<>();
            for(int i=0;i<array.length();i++){
                JSONObject json = (JSONObject) array.get(i);
                arr.add(json.get("text").toString());
            }

            StringBuilder str = new StringBuilder();
            str.append("<h1>"+content.getTitle()+"</h1>").append("</br>");
            str.append("<h3>"+content.getDesc()+"</h3>").append("</br>");
            for(int i=0;i<arr.size();i++){
                str.append(arr.get(i)).append("</br>").append("</br>");
            }
            Store store = new Store(content.getTitle(),content.getDesc(),arr);
            return str.toString();
        }
        else{
            List<String> arr = temp.getCommentList();
            StringBuilder str = new StringBuilder();
            str.append("<h1>"+content.getTitle()+"</h1>").append("</br>");
            str.append("<h3>"+content.getDesc()+"</h3>").append("</br>");
            for(int i=0;i<arr.size();i++){
                str.append(arr.get(i)).append("</br>").append("</br>");
            }
            return str.toString();
        }
    }
}
