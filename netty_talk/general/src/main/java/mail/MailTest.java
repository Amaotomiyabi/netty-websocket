package mail;

import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.math.BigDecimal;
import java.util.Properties;

/**
 * @author xf
 * @date 2021-02-23-13-20
 * @since
 **/


public class MailTest {
/*    public static void main(String[] args) throws Exception {
        String from = "1084783417@qq.com";
        String pwd = "xqnuxwzplqipjadb";
        String to = "2667401791@qq.com";
        String host = "smtp.qq.com";
        Properties properties = System.getProperties();
        properties.put("mail.smtp.auth", "true");
        properties.setProperty("mail.smtp.host", host);
        Session session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(from, pwd);
            }
        });
        MimeMessage message = new MimeMessage(session);
        message.setFrom(new InternetAddress(from));
        message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));
        message.setSubject("是测试哦");
        message.setContent("""
                <h1 style="text-align:center">This is actual message</h1>
                <h2 style="color:red">火火火</h2>
                """, "text/html");
        Transport.send(message);
    }*/

    public static void main(String[] args) {
        double funds = 1.00;
        int itemsBought = 0;
        for (double price = 0.10; funds >= price; price +=0.10) {
            funds -= price;
            itemsBought++;
        }
        System.out.println(itemsBought + " items bought.");

        System.out.println("Change: $" + funds);
        System.out.println(new BigDecimal(0.2d));
        System.out.println(new BigDecimal(0.1d).add(new BigDecimal(0.1d)));

        System.out.println(new BigDecimal(0.2f));
        System.out.println(new BigDecimal(0.1f).add(new BigDecimal(0.1f)));

        System.out.println(new BigDecimal(0.3d));
        System.out.println(new BigDecimal(0.1d).add(new BigDecimal(0.1d)).add(new BigDecimal(0.1d)));

        System.out.println(new BigDecimal(0.3f));
        System.out.println(new BigDecimal(0.1f).add(new BigDecimal(0.1f)).add(new BigDecimal(0.1f)));
    }

}
