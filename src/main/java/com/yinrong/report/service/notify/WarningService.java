package com.yinrong.report.service.notify;

import okhttp3.OkHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class WarningService implements WarningNotifier, InitializingBean {

    private static final Logger logger = LoggerFactory.getLogger(WarningService.class);

    private static final OkHttpClient client;

    static {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        builder.connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .retryOnConnectionFailure(true);

        client = builder.build();
    }

    private WarningNotifier warningNotifier;

    private String accessToken;


    @Override
    public void notify(NotifyBody notifyBody) {
        this.warningNotifier.notify(notifyBody);
    }

    @Override
    public NotifyBody createTextBody(String body) {
        return this.warningNotifier.createTextBody(body);
    }

    @Value("${warning.access.token:54e5c44b-5d83-4cd6-a206-e5c978738532}")
    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }


    @Override
    public void afterPropertiesSet() throws Exception {
        this.warningNotifier = new QywxWarningNotifier(client, accessToken);
    }
    public static void main(String[] args) throws Exception {
        //n ==  https://qyapi.weixin.qq.com/cgi-bin/webhook/send?key=54e5c44b-5d83-4cd6-a206-e5c978738532
        WarningService service = new WarningService();
        service.setAccessToken("0314397c-3af5-46ed-aa1c-722e282faa33");
        service.afterPropertiesSet();

        service.notify("预警测试");
    }
}
