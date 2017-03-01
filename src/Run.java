import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.*;
import javax.swing.*;

class data
{
	double code, pro[] = new double [128];
	int ln;
}
public class Run 
{
	public static void main (String arg[])
	{
		new Run().Gui(); 
	}
	
	void Gui()
	{
		JFrame frm = new JFrame ("LZW");
		frm.setSize(500,300);
		frm.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		JPanel pnl = new JPanel();
		frm.add(pnl);
		
		pnl.setLayout(null);
		
		JLabel l = new JLabel("File path");
		l.setBounds(211, 51, 71, 31);
		pnl.add(l);
		
		JTextField tf = new JTextField();
		tf.setBounds(99, 91, 277, 33);
		pnl.add(tf);
		
		JButton b1 = new JButton("Compress");
		b1.setBounds(33, 177, 111, 51);
		pnl.add(b1);
		
		JButton b2 = new JButton("Decompress");
		b2.setBounds(333, 177, 111, 51);
		pnl.add(b2);
		
		b1.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				Comp(tf.getText());
				JOptionPane.showMessageDialog(null, "Done!!");
			}
		});
		
		b2.addActionListener(new ActionListener()
		{
			
			@Override
			public void actionPerformed(ActionEvent arg0)
			{
				// TODO Auto-generated method stub
				Decomp(tf.getText());
				JOptionPane.showMessageDialog(null, "Done!!");
			}
		});

		frm.setVisible(true);
		
	}
	
	void Comp (String path)
	{
		String doc = new Run().ReadFromFile(path);
		double arr[] = new double[128], pro[] = new double [128], ans, f=0, l=1;
		
		for(int i=0 ; i<doc.length() ; i++)
			arr[(int)doc.charAt(i)]++;
		
		for(int i=0 ; i<128 ; i++)
		{
			arr[i]=arr[i]/doc.length();
			if(i>0)
				arr[i] += arr[i-1];
			pro[i]=arr[i];
		}
		
		for(int i=0 ; i<doc.length() ; i++)
		{
			l = arr[(int)doc.charAt(i)];
			f = ((int)doc.charAt(i)==0 ? f : arr[(int)doc.charAt(i)-1]);
			
			for(int j=0 ; j<128 ; j++)
				arr[j] = f + (l-f)*pro[j];
		}
		
		ans=(l+f)/2;
		
		new Run().WriteToFile(ans, doc.length(), pro);
	}
	
	String ReadFromFile (String path)
	{
		String doc="", s;
		
		try
		{
			Scanner in = new Scanner (new File(path));
			
			s=in.nextLine();
			doc+=s;

			while (in.hasNextLine())
			{
				s=in.nextLine();
				doc+= '\n' + s;
			}
			
			in.close();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Invalid path");
		}
		
		return doc;
	}

	void WriteToFile (double ans, int ln, double pro[])
	{
		File comFile  = new File ("comFile.txt");
		
		try
		{
			comFile.createNewFile();
			FileWriter writer = new FileWriter (comFile);
			
			writer.write(ans + " " + ln + " ");
			
			for(int i=0 ; i<128 ; i++)
				writer.write(pro[i] + " ");
			
			writer.close();
			
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Invalid path");
		}
	}
	
	void Decomp (String path)
	{
		data tmp = new Run().ReadFromComFile(path);
		double arr[] = new double [128], f=0, l=1;
		String doc="";
		
		for(int i=0 ; i<128 ; i++)
			arr[i] = tmp.pro[i];
		
		while (tmp.ln-->0)
		{
			for(int i=0 ; i<128 ; i++)
				if(tmp.code < arr[i])
				{
					doc += (char)i;
					l = arr[i];
					f = (i==0 ? f : arr[i-1]);

					for(int j=0 ; j<128 ; j++)
						arr[j] = f + (l-f)*tmp.pro[j];
					
					break;
				}
		}
				
		File comFile  = new File ("decomData.txt");
		
		try
		{
			comFile.createNewFile();
			FileWriter writer = new FileWriter (comFile);
			
			writer.write(doc);

			writer.close();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Invalid path");
		}

	}
	
	data ReadFromComFile (String path)
	{
		data tmp = new data();
		
		try
		{
			Scanner in = new Scanner (new File(path));
			
			tmp.code = in.nextDouble();
			tmp.ln = in.nextInt();
			
			for(int i=0 ; i<128 ; i++)
				tmp.pro[i] = in.nextDouble();
			
			in.close();
		}
		catch (Exception ex)
		{
			JOptionPane.showMessageDialog(null, "Invalid path");
		}

		return tmp;
	}
}