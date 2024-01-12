package uk.co.bigsoft.greenmail.mailx;

import java.net.URL;

import java.util.ArrayList;
import java.util.HashMap;

import javax.activation.DataSource;
import javax.activation.DataHandler;
import javax.activation.URLDataSource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Multipart;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMultipart;

public class MimeMessageBuilder {
	private MimeMessage o;
	private Multipart multipart;
	private HashMap<RecipientType, ArrayList<InternetAddress>> recips = new HashMap<>();

	public MimeMessageBuilder(Session smtpSession) {
		o = new MimeMessage(smtpSession);
		recips.put(RecipientType.TO, new ArrayList<InternetAddress>());
		recips.put(RecipientType.CC, new ArrayList<InternetAddress>());
		recips.put(RecipientType.BCC, new ArrayList<InternetAddress>());
		multipart = new MimeMultipart();
	}

	public MimeMessageBuilder withSubject(String x) throws MessagingException {
		o.setSubject(x);
		return this;
	}

	public MimeMessageBuilder withBody(String x) throws MessagingException {
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		mimeBodyPart.setText(x);
		multipart.addBodyPart(mimeBodyPart);
		return this;
	}

	public MimeMessageBuilder withFrom(String x) throws MessagingException {
		o.setFrom(x);
		return this;
	}

	public MimeMessageBuilder withTo(String x) throws MessagingException {
		withRecipient(RecipientType.TO, x);
		return this;
	}

	public MimeMessageBuilder withCc(String x) throws MessagingException {
		withRecipient(RecipientType.CC, x);
		return this;
	}

	public MimeMessageBuilder withBcc(String x) throws MessagingException {
		withRecipient(RecipientType.BCC, x);
		return this;
	}

	public MimeMessageBuilder withAttachment(String name, URL file) throws MessagingException {
		MimeBodyPart mimeBodyPart = new MimeBodyPart();
		DataSource source = new URLDataSource(file);
		mimeBodyPart.setDataHandler(new DataHandler(source));
		mimeBodyPart.setFileName(name);
		multipart.addBodyPart(mimeBodyPart);
		return this;
	}

	public MimeMessage build() throws MessagingException {
		o.setRecipients(RecipientType.TO, toInternetAddressArray(RecipientType.TO));
		o.setRecipients(RecipientType.CC, toInternetAddressArray(RecipientType.CC));
		o.setRecipients(RecipientType.BCC, toInternetAddressArray(RecipientType.BCC));
		o.setContent(multipart);
		return o;
	}

	private MimeMessageBuilder withRecipient(RecipientType who, String x) throws MessagingException {
		InternetAddress[] a = InternetAddress.parse(x, false);
		recips.get(who).add(a[0]);
		return this;
	}

	private InternetAddress[] toInternetAddressArray(RecipientType who) {
		ArrayList<InternetAddress> l = recips.get(who);
		InternetAddress[] a = l.toArray(new InternetAddress[l.size()]);
		return a;
	}

}
