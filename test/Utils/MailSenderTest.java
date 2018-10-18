package Utils;

import Data.CustomerInfo;
import Data.Invoice;
import Data.Item;
import Html.Element;
import Html.InvoiceHtml;
import org.junit.Test;

import javax.mail.MessagingException;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: CLASS JAVA DOC HERE
 */
public class MailSenderTest {

    @Test
    public void sendHTMLMail() throws FileNotFoundException, MessagingException {
        GmailSender.DEFAULT.sendMail("nathanzheng87@gmail.com",
                new ArrayList<>(),
                "Test",
                "<h1>I'm a heading</h1>",
                null);
    }

    @Test
    public void sendPlainMail() throws FileNotFoundException, MessagingException {
        GmailSender.DEFAULT.sendMail("nathanzheng87@gmail.com",
                new ArrayList<>(),
                "Test",
                "hello",
                null);
    }

    @Test
    public void sendInvoice() throws FileNotFoundException, MessagingException {
        CustomerInfo customerInfo = new CustomerInfo("Nathan", "604-657-7930", "nathanzheng87@gmail.com");
        List<Item> items = new ArrayList<>();
        items.add(new Item("Item 1", 1, 10));
        items.add(new Item("Item 2", 2, 20));

        final Invoice invoice = new Invoice("TestInvoiceNumber", LocalDate.now(), LocalDate.now().plusDays(3),
                customerInfo, items, 0, false, false, false);
        Element html = new Element("html");
        InvoiceHtml.buildHead(html);
        InvoiceHtml.buildBody(html, invoice);
        GmailSender.DEFAULT.sendMail("nathanzheng87@gmail.com",
                new ArrayList<>(),
                "Test Invoice",
                html.print(),
                null);
    }
}
