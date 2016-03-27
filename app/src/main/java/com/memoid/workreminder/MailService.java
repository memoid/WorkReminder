package com.memoid.workreminder;

import android.app.Activity;
import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

public class MailService extends IntentService {

    public static final String NOTIFICATION = "com.memoid.workreminder";

    private String host, port, sport;
    private boolean debbugable, auth;
    private MimeMultipart multipart;
    private int result = Activity.RESULT_CANCELED;

    public MailService() {
        super("MailService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        host = "smtp.gmail.com";
        port = "465";
        sport = "465";
        debbugable = false;
        auth = true;

        Properties props = setProperties();

        Bundle extras = intent.getExtras();

        String from = extras.getString("from");
        final String user = extras.getString("user");
        final String pass = extras.getString("password");
        String to[] = extras.getStringArray("tos");
        String subject = extras.getString("subject");
        String body = extras.getString("body");

        multipart = new MimeMultipart();

        // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, pass);
            }
        });

        try {

            MimeMessage msg = new MimeMessage(session);
            msg.setFrom(new InternetAddress(from));

            InternetAddress[] adressTo = new InternetAddress[to.length];
            for (int i = 0; i < to.length; i++) {
                adressTo[i] = new InternetAddress(to[i]);
            }

            msg.setRecipients(MimeMessage.RecipientType.TO, adressTo);

            msg.setSubject(subject);
            msg.setSentDate(new Date());

            //setup message body
            BodyPart messageBodyPart = new MimeBodyPart();
            messageBodyPart.setText(body);
            multipart.addBodyPart(messageBodyPart);

            //put parts in message
            msg.setContent(multipart);

            //send mail
            Transport transport = session.getTransport("smtps");
            transport.connect("smtp.gmail.com", 465, user, pass);
            transport.sendMessage(msg, msg.getAllRecipients());
            transport.close();
            result = Activity.RESULT_OK;
            //Transport.send(msg);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            publishResult(result);
        }

    }

    private void publishResult(int result) {
        Intent intent = new Intent(NOTIFICATION);
        intent.putExtra("result", result);
        sendBroadcast(intent);
    }

    private Properties setProperties() {

        Properties props = new Properties();
        props.put("mail.smtp.host", host);

        if (debbugable)
            props.put("mail.debug", true);

        if (auth)
            props.put("mail.smtp.auth", true);

        props.put("mail.smtp.port", port);
        props.put("mail.smtp.socketFactory.port", sport);
        props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        props.put("mail.smtp.socketFactory.fallback", "false");
        props.put("mail.smtps.ssl.enable", "true");
        props.put("mail.smtps.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");

        return props;

    }

}
