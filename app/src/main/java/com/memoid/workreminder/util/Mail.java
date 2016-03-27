package com.memoid.workreminder.util;

import java.util.Date;
import java.util.Properties;

import javax.activation.CommandMap;
import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.activation.MailcapCommandMap;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

/**
 * Created by gmorod on 3/22/2016.
 */
public class Mail  implements Runnable {

    private String user;
    private String pass;

    private String[] to;
    private String from;

    private String port;
    private String sport;

    private String host;

    private String subject;
    private String body;

    private boolean auth;

    private boolean debbugable;

    private Multipart multipart;

    @Override
    public void run() {
        try {
            send();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public Mail() {
        host = "smtp.gmail.com";
        port = "465";
        sport = "465";


        user = "";
        pass = "";
        from = "";
        subject = "";
        body = "";

        debbugable = false;
        auth = true;

        multipart = new MimeMultipart();

        // There is something wrong with MailCap, javamail can not find a handler for the multipart/mixed part, so this bit needs to be added.
        MailcapCommandMap mc = (MailcapCommandMap) CommandMap.getDefaultCommandMap();
        mc.addMailcap("text/html;; x-java-content-handler=com.sun.mail.handlers.text_html");
        mc.addMailcap("text/xml;; x-java-content-handler=com.sun.mail.handlers.text_xml");
        mc.addMailcap("text/plain;; x-java-content-handler=com.sun.mail.handlers.text_plain");
        mc.addMailcap("multipart/*;; x-java-content-handler=com.sun.mail.handlers.multipart_mixed");
        mc.addMailcap("message/rfc822;; x-java-content-handler=com.sun.mail.handlers.message_rfc822");
        CommandMap.setDefaultCommandMap(mc);

    }

    public Mail(String user, String pass) {
        this();
        this.user = user;
        this.pass = pass;
    }

    public boolean send() throws Exception {
        Properties props = setProperties();

        if (!user.equals("") && !pass.equals("") && to.length > 0 && !from.equals("") && !subject.equals("") && !body.equals("")) {

            Session session = Session.getInstance(props, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(user, pass);
                }
            });

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
            //Transport.send(msg);

            return true;

        } else {

            return false;

        }

    }

    public void addAttachment(String fileName) throws Exception {
        BodyPart messageBodyPart = new MimeBodyPart();
        DataSource source = new FileDataSource(fileName);
        messageBodyPart.setDataHandler(new DataHandler(source));
        messageBodyPart.setFileName(fileName);

        multipart.addBodyPart(messageBodyPart);
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

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String[] getTo() {
        return to;
    }

    public void setTo(String[] to) {
        this.to = to;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isAuth() {
        return auth;
    }

    public void setAuth(boolean auth) {
        this.auth = auth;
    }

    public boolean isDebbugable() {
        return debbugable;
    }

    public void setDebbugable(boolean debbugable) {
        this.debbugable = debbugable;
    }

    public Multipart getMultipart() {
        return multipart;
    }

    public void setMultipart(Multipart multipart) {
        this.multipart = multipart;
    }

}
