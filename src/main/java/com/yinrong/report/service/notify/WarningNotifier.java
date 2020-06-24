package com.yinrong.report.service.notify;

/**
 * @Classname WarningNotifier
 * @Description
 * @Date 2020/4/14 10:38 上午
 * @Created by yinrong
 */
public interface WarningNotifier {
    default void notify(String body) {
        notify(this.createTextBody(body));
    }

    void notify(NotifyBody notifyBody);

    NotifyBody createTextBody(String body);

}
