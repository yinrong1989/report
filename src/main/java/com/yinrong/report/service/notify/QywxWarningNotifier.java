package com.yinrong.report.service.notify;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.MessageFormat;

/**
 * @Classname QywxWarningNotifier
 * @Description
 * @Date 2020/4/14 10:52 上午
 * @Created by yinrong
 */
public class QywxWarningNotifier  implements WarningNotifier{

    private static final Logger logger = LoggerFactory.getLogger(QywxWarningNotifier.class);

    private OkHttpClient client;

    private String key;

    public QywxWarningNotifier(OkHttpClient client, String key) {
        this.client = client;
        this.key = key;
    }

    @Override
    public void notify(NotifyBody notifyBody) {

        try {
            String warningNotifyUrl = "https://qyapi.weixin.qq.com/cgi-bin/webhook/send";
            String baseUrl = warningNotifyUrl + "?key={0}";

            String url = MessageFormat.format(baseUrl, key);
            RequestBody body = notifyBody.toRequestBody();
            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .build();
            client.newCall(request).execute();
        } catch (Exception e) {
            logger.error("warning通知异常", e);
        }

    }

    @Override
    public NotifyBody createTextBody(String body) {
        return new QywxTextNotifyBody(body);
    }
}
