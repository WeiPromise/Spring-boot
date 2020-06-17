package com.promise.demo.util.http;

import com.promise.demo.util.Bodys;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

/**
 * Created by leiwei on 2020/4/3 17:00
 */
public class HttpUtils {

    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        String url="http://ws03.mlamp.cn:8848/fileshare";
        HttpHeaders headers = restTemplate.headForHeaders(url);
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        MultiValueMap<String, Object> map= new LinkedMultiValueMap<>();
        FileSystemResource resource = new FileSystemResource("F:\\develop\\demo\\spring-boot\\promise-web\\src\\main\\java\\com\\promise\\demo\\util\\PageBean.java");
        map.add("file", resource);
        HttpEntity<MultiValueMap<String, Object>> request = new HttpEntity<>(map, headers);
        ResponseEntity<Bodys> response = restTemplate.postForEntity( url, request , Bodys.class );
        System.out.println(response.getBody());

    }

}
