package com.util;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Component
public class MailUtil {

    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    @Autowired
    private JavaMailSender mailSender;

    public void sendActivationMail(String emailAddress, String activationToken) {

        MimeMessage mimeMessage = mailSender.createMimeMessage();
        try {
            mimeMessage.setSubject("Hesap Aktivasyonu");

            ClassPathResource imagePath = new ClassPathResource("static/images/unnamed.png");
            String imageId = "logo";
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);
            String htmlText =
                    "<div style=\"text-align:center; font-size:16px;\" >"+
                    "<img class=\"center\" src=\"cid:" +imageId+"\"/>" +
                    "<br></br>" +
                    //"<h3 style=\"text-align: center\">Hoşgeldiniz</h3>" +
                    "<h3>Merhaba,</h3>" +
                    "<p><b>Göster Bilgini</b> yarışma dünyasına hoşgeldiniz.</p>" +
                    "<p style=\"margin-bottom:20px;\">Hesabınızın aktifleştirilmesi için lütfen alttaki butona tıklayınız:</p>" +
                    "<a target=\"_blank\" href=\"http://localhost:8091/activateAccount?activationToken="+ activationToken + "\" " +
                              "style=\"background-color:#27c007; padding:15px; display:inline-block; color:black; text-decoration:none; border-style:solid; border-width:2px; border-color:black; border-radius:10px; font-weight:700;\">Hesabımı Aktifleştir</a>" +
                    //"<p>http://localhost:8091/activateAccount?activationToken=" + activationToken + "</p>" ;
                    //"<button id=\"myButton\" onclick=\"location.href='http://localhost:8091/activateAccount?activationToken='" + activationToken + ";\" tabindex=\"0\" style=\"background-color:rgb(39, 192, 7);border-radius: 4px;height: 55px;font-weight:700;\"><span>Hesabımı Aktifleştir</span></button>"+
                    //"<script type=\"text/javascript\">document.getElementById(\"myButton\").onclick = function () {location.href = \"http://localhost:8091/activateAccount?activationToken=123\";};</script>" +
                    "</div>";
            mimeMessageHelper.setText(htmlText, true);

            mimeMessageHelper.setTo(emailAddress);
            mimeMessageHelper.setFrom("unaldisevil@gmail.com");
            mimeMessageHelper.addInline(imageId,imagePath);

            //ATTACHMENT
            //Yöntem1:
//          try {
//              File file = new ClassPathResource("gameLogo.png").getFile();
//              mimeMessageHelper.addAttachment("Notes", file);
//          } catch (IOException e){
//              logger.error("Mail ERROR => " + e.getMessage());
//          }
            //Yöntem2:
//          FileSystemResource file = new FileSystemResource(new File("C:/Users/asus/Desktop/adam.txt"));
//          mimeMessageHelper.addAttachment("Notes", file);
        } catch (MessagingException e) {
            logger.error("Mail ERROR => " + e.getMessage());
        }
        mailSender.send(mimeMessage);
    }

}
