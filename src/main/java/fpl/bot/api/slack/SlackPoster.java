package fpl.bot.api.slack;

import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.jaxrs.client.ClientConfiguration;
import org.apache.cxf.jaxrs.client.WebClient;
import org.apache.cxf.jaxrs.ext.multipart.Attachment;
import org.apache.cxf.jaxrs.ext.multipart.ContentDisposition;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.InvocationCallback;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Collections;

@Component
public class SlackPoster {
    private static final Logger log = Logger.getLogger(SlackPoster.class);

    @Value("${postToSlack}")
    boolean postToSlack;

    @Value("${slackAuthToken}")
    String slackAuthToken;

    public void sendMessage(String message, String channel) {
        if (!postToSlack) {
            log.info("postToSlack was disabled, not posting to Slack");
            log.info("The following message would have been sent to #" + channel + ": " + message);
            return;
        }

        log.info("Sending the following message to #" + channel + ": " + message);

        WebClient webClient = WebClient.create("https://slack.com/api/chat.postMessage", Collections.singletonList(new JacksonJaxbJsonProvider()));
        webClient.type(MediaType.APPLICATION_JSON_VALUE);
        ClientConfiguration config = WebClient.getConfig(webClient);
        config.getInInterceptors().add(new LoggingInInterceptor());
        config.getOutInterceptors().add(new LoggingOutInterceptor());
        webClient.query("token", slackAuthToken);
        webClient.query("channel", channel);
        webClient.query("text", message);
        webClient.query("mrkdwn", true);
        webClient.query("as_user", true);
        webClient.get();
    }

    @Async
    public void uploadFile(File message, String channelId) throws FileNotFoundException {
        if (!postToSlack) {
            log.info("postToSlack was disabled, not uploading to Slack");
            log.info("The following file would have been sent to #" + channelId + ": " + message.getName());
            return;
        }

        log.info("Sending the following file to #" + channelId + ": " + message.getName());

        WebClient webClient = WebClient.create("https://slack.com/api/files.upload");
        ClientConfiguration config = WebClient.getConfig(webClient);
        config.getInInterceptors().add(new LoggingInInterceptor());
        config.getOutInterceptors().add(new LoggingOutInterceptor());
        webClient.query("token", slackAuthToken);
        webClient.query("channels", channelId);
        InputStream is = new FileInputStream(message);
        ContentDisposition cd = new ContentDisposition("form-data; name=file; filename=" + message.getName());
        Attachment att = new Attachment("id", is, cd);
        MultipartBody body = new MultipartBody(att);

        webClient.async().post(Entity.entity(body, MediaType.MULTIPART_FORM_DATA_VALUE), new InvocationCallback<Object>() {
            @Override
            public void completed(Object o) {
                if (message.delete()) {
                    log.info("Uploaded file " + message.getName() + " deleted");
                } else {
                    log.error("Failed to delete uploaded file " + message.getName());
                }
            }

            @Override
            public void failed(Throwable throwable) {

            }
        });
    }
}
