package com.operationsmiley.aarogyaecare;

public class NotificationModel {
    String notification_image,notification_name,notification_desc,notification_icon,notification_time,link,phone;

    public NotificationModel() {
    }

    public NotificationModel(String notification_image, String phone, String notification_name, String notification_desc, String notification_icon, String notification_time, String link) {
        this.notification_image = notification_image;
        this.notification_name = notification_name;
        this.notification_desc = notification_desc;
        this.notification_icon = notification_icon;
        this.notification_time = notification_time;
        this.link = link;
        this.phone = phone;

    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getNotification_image() {
        return notification_image;
    }

    public void setNotification_image(String notification_image) {
        this.notification_image = notification_image;
    }

    public String getNotification_name() {
        return notification_name;
    }

    public void setNotification_name(String notification_name) {
        this.notification_name = notification_name;
    }

    public String getNotification_desc() {
        return notification_desc;
    }

    public void setNotification_desc(String notification_desc) {
        this.notification_desc = notification_desc;
    }

    public String getNotification_icon() {
        return notification_icon;
    }

    public void setNotification_icon(String notification_icon) {
        this.notification_icon = notification_icon;
    }

    public String getNotification_time() {
        return notification_time;
    }

    public void setNotification_time(String notification_time) {
        this.notification_time = notification_time;
    }
}
