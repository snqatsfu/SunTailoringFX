package Utils;

import org.jetbrains.annotations.Nullable;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Collections;
import java.util.List;

public abstract class MailSender {

    protected final String sourceEmail;

    MailSender(String sourceEmail) {
        this.sourceEmail = sourceEmail;
    }

    protected abstract Session createSession();

    protected abstract void send(Session session, Message message) throws MessagingException;

    public synchronized void sendMail(String destEmail,
                         List<String> ccEmails,
                         String subject,
                         String body,
                         @Nullable String attachmentFileName) throws FileNotFoundException, MessagingException {

        final Session session = createSession();

        // compose message

        Message message = new MimeMessage(session);
        message.setFrom(new InternetAddress(sourceEmail));
        message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destEmail));
        for (String ccEmail : ccEmails) {
            message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccEmail));
        }
        message.setSubject(subject);

        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(body, "text/html; charset=utf-8");

        Multipart multipart = new MimeMultipart();
        multipart.addBodyPart(bodyPart);

        if (attachmentFileName != null && !attachmentFileName.isEmpty()) {
            bodyPart = new MimeBodyPart();

            File file = new File(attachmentFileName);
            if (!file.exists()) {
                throw new FileNotFoundException("File '" + attachmentFileName + "' does not exist!");
            }

            DataSource source = new FileDataSource(attachmentFileName);
            bodyPart.setDataHandler(new DataHandler(source));

            String[] tokens = attachmentFileName.split("\\\\");
            String simpleAttachmentName = tokens[tokens.length - 1];

            bodyPart.setFileName(simpleAttachmentName);

            multipart.addBodyPart(bodyPart);
        }

        message.setContent(multipart);

        send(session, message);

    }

    public synchronized void sendMail(String destEmail,
                         String subject,
                         String body) throws FileNotFoundException, MessagingException {
        sendMail(destEmail, Collections.emptyList(), subject, body, "");
    }
}
