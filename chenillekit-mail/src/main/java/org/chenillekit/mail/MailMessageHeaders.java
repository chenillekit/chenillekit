/**
 * 
 */
package org.chenillekit.mail;

import java.util.ArrayList;
import java.util.List;

/**
 * @author massimo
 *
 */
public final class MailMessageHeaders
{
	private String from = "";
	
	private String subject = "";
	
	private List<String> to = new ArrayList<String>();
	
	private List<String> cc = new ArrayList<String>();
	
	private List<String> bcc = new ArrayList<String>();
	
	// TODO There are any more headers suitable ?
	

	
	public String getFrom()
	{
		return from;
	}

	public void setFrom(String from)
	{
		this.from = from;
	}

	public String getSubject()
	{
		return subject;
	}

	public void setSubject(String subject)
	{
		this.subject = subject;
	}
	
	
	public void addTo(String email)
	{
		this.to.add(email);
	}
	
	
	public void addCc(String email)
	{
		this.cc.add(email);
	}
	
	public void addBcc(String email)
	{
		this.bcc.add(email);
	}

	
	public List<String> getTo()
	{
		return to;
	}

	public List<String> getCc()
	{
		return cc;
	}

	public List<String> getBcc()
	{
		return bcc;
	}

	
	public String getToAsEmails()
	{
		StringBuilder builder = new StringBuilder(this.to.size() * 8);
		
		for (String to : this.to)
		{
			builder.append(to + ", ");
		}
		
		return builder.toString();
	}
	
	public String getCcAsEmails()
	{
		StringBuilder builder = new StringBuilder(this.cc.size() * 8);
		
		for (String cc : this.cc)
		{
			builder.append(cc + ", ");
		}
		
		return builder.toString();
	}
	
	public String getBccAsEmails()
	{
		StringBuilder builder = new StringBuilder(this.bcc.size() * 8);
		
		for (String bcc : this.bcc)
		{
			builder.append(bcc + ", ");
		}
		
		return builder.toString();
	}
}
