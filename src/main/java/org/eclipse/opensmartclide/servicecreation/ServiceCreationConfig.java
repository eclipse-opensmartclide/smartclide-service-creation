package org.eclipse.opensmartclide.servicecreation;

import org.eclipse.opensmartclide.servicecreation.functionality.mainFlow.LicenseFlow;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Scanner;


@Configuration
public class ServiceCreationConfig {

    @Bean
    CommandLineRunner commandLineRunner() {
        return args -> {
            LicenseFlow.licensesMap=new HashMap<>();

            URL url = new URL("https://gitlab.com/api/v4/templates/licenses");
            HttpURLConnection conn = (HttpURLConnection)url.openConnection();
            conn.setRequestMethod("GET");
            conn.connect();
            int responsecode = conn.getResponseCode();
            if(responsecode != 200)
                System.err.println(responsecode);
            else{
                Scanner sc = new Scanner(url.openStream());
                String inline="";
                while(sc.hasNext()){
                    inline+=sc.nextLine();
                }
                sc.close();

                JSONParser parse = new JSONParser();
                JSONArray jsonArray = (JSONArray)parse.parse(inline);

                for(int i=0; i<jsonArray.size(); i++){
                    JSONObject jsonObject = (JSONObject)jsonArray.get(i);
                    String key= jsonObject.get("key").toString();
                    String content= jsonObject.get("content").toString();
                    LicenseFlow.licensesMap.put(key,content);
                }
            }
        };
    }
}
