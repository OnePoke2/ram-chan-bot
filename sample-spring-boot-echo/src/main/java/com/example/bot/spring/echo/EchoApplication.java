/*
 * Copyright 2016 LINE Corporation
 *
 * LINE Corporation licenses this file to you under the Apache License,
 * version 2.0 (the "License"); you may not use this file except in compliance
 * with the License. You may obtain a copy of the License at:
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 */

package com.example.bot.spring.echo;

import java.net.URI;

import org.json.JSONObject;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpMethod;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.linecorp.bot.model.event.Event;
import com.linecorp.bot.model.event.MessageEvent;
import com.linecorp.bot.model.event.message.TextMessageContent;
import com.linecorp.bot.model.message.Message;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.spring.boot.annotation.EventMapping;
import com.linecorp.bot.spring.boot.annotation.LineMessageHandler;

@SpringBootApplication
@LineMessageHandler
public class EchoApplication {
    public static void main(String[] args) {
        SpringApplication.run(EchoApplication.class, args);
    }

    @EventMapping
    public Message handleTextMessageEvent(MessageEvent<TextMessageContent> event) {
        System.out.println("event: " + event);
        final String originalMessageText = event.getMessage().getText();
        return new TextMessage(searchDictionary(originalMessageText));
    }

    @EventMapping
    public void handleDefaultMessageEvent(Event event) {
        System.out.println("event: " + event);
    }

    public String searchDictionary(String text) {
        RestTemplate restTemplate = new RestTemplate();
        String url = "https://ja.wikipedia.org/w/api.php?action=query&prop=extracts&rvprop=content&format=json&formatversion=2&redirects&titles=" + text;
        RequestEntity<?> req = new RequestEntity<>(
                text, HttpMethod.GET, URI.create(url));
        ResponseEntity<String> r = restTemplate.exchange(req, String.class);
        System.out.println(r.getBody());
        JSONObject obj = new JSONObject(r.getBody());
        System.out.println(obj);
        if (obj.getJSONObject("query").getJSONArray("pages").getJSONObject(0).has("missing")) {
            return "そんなん知らん";
        }
        String raw = obj.getJSONObject("query").getJSONArray("pages").getJSONObject(0).getString("extract");
        String summary = raw.split("<h2>")[0];
        summary = summary.replace("<p>","");
        summary = summary.replace("<b>","");
        summary = summary.replace("<span>","");
        summary = summary.replace("<span lang=\"en\">","");
        summary = summary.replace("</span>","");
        summary = summary.replace("</p>","");
        summary = summary.replace("</b>","");
        summary = summary.replace("<li>"," ");
        summary = summary.replace("<ul>","");
        summary = summary.replace("</li>"," ");
        summary = summary.replace("</ul>","");
        summary = summary.replace("<br>","");
        summary = summary.replace("<span lang=\"th\">","");
        summary = summary.replace("\n","");
        summary = summary.replaceAll("<.>","");
        summary = summary.replaceAll("<..>","");
        summary = summary.replaceAll("<...>","");
        summary = summary.replaceAll("<....>","");
        summary = summary.replaceAll("<.....>","");
        summary = summary.replaceAll("<......>","");
        summary = summary.replaceAll("<.......>","");
        summary = summary.replaceAll("<........>","");
        summary = summary.replaceAll("<.........>","");
        summary = summary.replaceAll("<..........>","");
        summary = summary.replaceAll("<...........>","");
        summary = summary.replaceAll("<............>","");
        summary = summary.replaceAll("<.............>","");
        summary = summary.replaceAll("<..............>","");
        summary = summary.replaceAll("<...............>","");
        summary = summary.replaceAll("<................>","");
        summary = summary.replaceAll("<.................>","");
        summary = summary.replaceAll("<..................>","");
        System.out.println(summary);
        return summary;
    }
}
